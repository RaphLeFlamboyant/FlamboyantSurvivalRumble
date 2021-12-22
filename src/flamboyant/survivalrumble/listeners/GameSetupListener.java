package flamboyant.survivalrumble.listeners;

import flamboyant.survivalrumble.data.PlayerClassMetadata;
import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.playerclass.managers.PlayerClassSelectionManager;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import flamboyant.survivalrumble.utils.TeamHelper;
import flamboyant.survivalrumble.views.GameSetupView;
import flamboyant.survivalrumble.views.PlayerClassSelectionView;
import flamboyant.survivalrumble.views.TeamHQParametersView;
import flamboyant.survivalrumble.views.TeamSelectionView;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GameSetupListener implements Listener
{
    private JavaPlugin plugin;
    private Server server;
    private SurvivalRumbleData data()
    {
        return SurvivalRumbleData.getSingleton();
    }

    public GameSetupListener(JavaPlugin plugin, Server server)
    {
        this.plugin = plugin;
        this.server = server;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (isTeamSelectionItem(item))
        {
            Inventory teamSelectionView = TeamSelectionView.getInstance().getViewInstance();
            player.openInventory(teamSelectionView);
        }
        else if (ItemHelper.isExactlySameItemKind(item, ItemHelper.getGameSetupItem()))
        {
            Inventory gameSetupView = GameSetupView.getInstance().getViewInstance();
            player.openInventory(gameSetupView);
        }
        else if (ItemHelper.isExactlySameItemKind(item, ItemHelper.getTeamHQItem()))
        {
            Inventory teamHQView = TeamHQParametersView.getInstance().getViewInstance();
            player.openInventory(teamHQView);
        }
        else if (ItemHelper.isExactlySameItemKind(item, ItemHelper.getLaunchItem()))
        {
            if (data().playersClass.size() == 0)
                lanchClassSelectionStep(player);
            else
                launchGame();
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        System.out.println("Drop canceled");
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        System.out.println("Swap canceled");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        event.setCancelled(true);
    }

    private boolean isTeamSelectionItem(ItemStack item)
    {
        return ItemHelper.isExactlySameItemKind(item, ItemHelper.getTeamSelectionItem());
    }

    private void lanchClassSelectionStep(Player opPlayer)
    {
        cleanEmptyTeams();
        if (isHeadquarterMissing())
        {
            opPlayer.sendMessage( ChatColor.RED + "Headquarter location are missing");
            return;
        }

        fillPlayersByTeamData();
        setupScores();

        TeamSelectionView.getInstance().unregisterEvents();
        GameSetupView.getInstance().unregisterEvents();
        TeamHQParametersView.getInstance().unregisterEvents();

        Collection<UUID> playerIds = data().players.values();
        for (UUID playerId : playerIds)
        {
            Player player = server.getPlayer(playerId);
            Inventory inv = player.getInventory();
            inv.clear();
        }

        server.getPluginManager().registerEvents(PlayerClassSelectionView.getInstance(), plugin);
        PlayerClassSelectionManager manager = new PlayerClassSelectionManager(plugin, server, opPlayer);
        server.getPluginManager().registerEvents(manager, plugin);
        announceClassSelection();
        Bukkit.getScheduler().runTaskLater(plugin, () -> manager.dispatchSelectionView(), 100L);
    }

    private void announceClassSelection()
    {
        for(UUID playerId : data().playersTeam.keySet())
        {
            Player player = server.getPlayer(playerId);
            Location playerLocation = player.getLocation();

            player.playSound(playerLocation, Sound.EVENT_RAID_HORN, SoundCategory.MASTER, 1, 1);
            player.sendTitle("Sélection des classes", "Chacun votre tour, sélectionnez une classe", 10, 80, 10);
        }
    }

    private void fillPlayersByTeamData()
    {
        for (String teamName : TeamHelper.teamNames)
        {
            data().playersByTeam.put(teamName, new ArrayList<>());

            for (UUID playerId : data().playersTeam.keySet())
            {
                // System.out.println("Player " + playerId + " has team from data  : " + data().playersTeam.get(playerId) + " compared to " + teamName + " and result is " + data().playersTeam.get(playerId).equals(teamName));
                if (data().playersTeam.get(playerId).equals(teamName))
                    data().playersByTeam.get(teamName).add(playerId);
            }
        }
    }

    private void cleanEmptyTeams()
    {
        for(int i = 0; i < data().teams.size(); i++)
        {
            String name = data().teams.get(i);
            if (!data().playersTeam.containsValue(name))
            {
                System.out.println("Removing team " + name + " because it doesn't contain any player");
                data().teams.remove(i);
                if (data().teamHeadquarterLocation.containsKey(name))
                    data().teamHeadquarterLocation.remove(name);
                ScoreboardBricklayer.getSingleton().deleteTeam(name);
                i--;
            }
        }
    }

    private boolean isHeadquarterMissing()
    {
        for(String teamName : data().teams)
        {
            System.out.println("isHeadquarterMissing for " + teamName + " ?");
            if (!data().teamHeadquarterLocation.containsKey(teamName))
                return true;
            System.out.println("OK");
        }

        return false;
    }

    private void setupScores()
    {
        ScoreboardBricklayer scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
        Objective scoreObj = scoreboardBricklayer.createObjective("Score", "Score", DisplaySlot.SIDEBAR);
        for (String teamName : data().teams)
        {
            scoreObj.getScore(teamName).setScore(0);
            data().teamScores.put(teamName, 0);
        }
    }

    private void launchGame()
    {
        for(UUID playerId : data().playersTeam.keySet())
        {
            Player player = server.getPlayer(playerId);
            Location target = data().teamHeadquarterLocation.get(data().playersTeam.get(playerId));
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Tu es dans l'équipe " + data().playersTeam.get(playerId) + ChatColor.GOLD +
                    ", tu dois te rendre aux coordonnées " + target.getBlockX() + " " + target.getBlockY() + " " + target.getBlockZ() +
                    " avec tous les membres de ton équipe !");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Tu ne peux pas poser ou détruire de bloc pendant cette phase");
            player.sendMessage(ChatColor.AQUA + "La partie va commencer !");
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> launchGameCountDown(5), 20L);
    }

    private void launchGameCountDown(Integer countDown)
    {
        if (countDown-- == 0)
            launchGameListeners();
        else {
            int finalCountDown = countDown;

            for(UUID playerId : data().playersTeam.keySet())
            {
                Player player = server.getPlayer(playerId);
                player.sendTitle(ChatColor.RED + countDown.toString(), "", 0, 20, 0);
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> launchGameCountDown(finalCountDown), 20L);
        }
    }

    private void launchGameListeners()
    {
        this.unregisterEvents();

        PlayerClassMechanicsHelper.getSingleton().initClassesAtGameLaunch(server, plugin);

        RunToBaseListener listener = new RunToBaseListener(plugin, server);
        server.getPluginManager().registerEvents(listener, plugin);
        listener.initListener();
    }

    public void unregisterEvents()
    {
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerDropItemEvent.getHandlerList().unregister(this);
        PlayerSwapHandItemsEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }
}
