package flamboyant.survivalrumble.listeners;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.playerclass.managers.GameTimeManager;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RunToBaseListener implements Listener
{
    private JavaPlugin plugin;
    private Server server;
    private World world;

    private List<UUID> playerReachedHQ = new ArrayList<>();
    private int pointsTick = 0;
    private BukkitTask checkPlayersPositionToHQTask = null;

    private SurvivalRumbleData data()
    {
        return SurvivalRumbleData.getSingleton();
    }

    public RunToBaseListener(JavaPlugin plugin, Server server)
    {
        this.plugin = plugin;
        this.server = server;
        world = server.getWorld("world");
    }

    // Scheduling

    public void initListener()
    {
        Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 1, 0);

        List<ItemStack> stuff = baseStuff(); // TODO : donner le stuff correspondant aux paramètres

        for (UUID playerId : data().players.values())
        {
            Player player = server.getPlayer(playerId);

            player.teleport(zeroLocation);
            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            for(ItemStack item : stuff)
                inventory.addItem(item);
            player.updateInventory();
        }

        world.setSpawnLocation(zeroLocation);
        world.setGameRuleValue("universalAnger", "true");
        world.setGameRuleValue("keepInventory", "true");
        world.setPVP(false);
        world.setDifficulty(Difficulty.EASY);

        scheduleCheckPlayersPositionToHQTask();
        GameTimeManager gameTimeManager = new GameTimeManager();
        gameTimeManager.launchGameTimeManagement(plugin);
    }

    public void scheduleCheckPlayersPositionToHQTask()
    {
        checkPlayersPositionToHQTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkPlayersPositionToHQ();
        }, 0, 20L);
    }

    private void checkPlayersPositionToHQ()
    {
        Map<String, Integer> reachesByTeam = new HashMap<>();

        for (String teamName : data().teams)
            reachesByTeam.put(teamName, 0);

        for(UUID playerId : data().players.values())
        {
            Player player = server.getPlayer(playerId);
            String teamName = data().playersTeam.get(playerId);
            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            double distance = hqLocation.distance(player.getLocation());

            if (distance < 50)
            {
                if (!playerReachedHQ.contains(playerId))
                {
                    playerReachedHQ.add(playerId);
                    player.sendMessage("Tu es arrivé aux coordonnées de la base de ton équipe ! Reste dans cette zone pour marquer quelques premiers points !");
                }

                reachesByTeam.put(teamName, reachesByTeam.get(teamName) + 1);
            }
        }

        handleScore(reachesByTeam);
        setupNextStepIfAllTeamReached(reachesByTeam);
    }

    private void handleScore(Map<String, Integer> reachesByTeam)
    {
        if (pointsTick == 9)
        {
            pointsTick = 0;
            ScoreboardBricklayer sb = ScoreboardBricklayer.getSingleton();
            for (String teamName : data().teams)
            {
                int score = data().teamScores.get(teamName) + reachesByTeam.get(teamName);
                sb.setTeamScore("Score", teamName, score);
                data().teamScores.put(teamName, score);
            }

            data().saveData();
        }
        else
            pointsTick++;
    }

    private void setupNextStepIfAllTeamReached(Map<String, Integer> reachesByTeam)
    {
        for(String teamName : reachesByTeam.keySet())
        {
            System.out.println("Reaches for team " + teamName + " is " + reachesByTeam.get(teamName) + " and team size is " + data().playersByTeam.get(teamName).size());
            if (reachesByTeam.get(teamName) < data().playersByTeam.get(teamName).size())
                return;
        }

        for(UUID playerId : data().playersTeam.keySet())
        {
            Player player = server.getPlayer(playerId);

            player.sendMessage( ChatColor.LIGHT_PURPLE + "Chaque joueur a atteint la base de son équipe ! Vous pouvez " +
                    "de nouveau poser et casser des blocs. Marquez des points en complétant votre quête principale ou en posant des blocs de " +
                    "construction dans votre base (plus vous êtes proches de la couche 64 plus ça marque de points). ");
            player.sendMessage( ChatColor.LIGHT_PURPLE + "Pour détecter les bases adverses, utilisez une boussole !");
        }

        Bukkit.getScheduler().cancelTask(checkPlayersPositionToHQTask.getTaskId());
        world.setGameRuleValue("keepInventory", "false");
        world.setPVP(true);
        world.setDifficulty(Difficulty.HARD);

        createHQStructure();
        unregisterEvents();

        MainGameListener listener = new MainGameListener(plugin, server);
        server.getPluginManager().registerEvents(listener, plugin);
        listener.initListener();
    }

    private void createHQStructure()
    {
        Player opPlayer = server.getPlayer(data().opPlayer);
        for(String teamName : data().teams)
        {
            Location location = data().teamHeadquarterLocation.get(teamName);
            Bukkit.dispatchCommand(opPlayer, "fill " + (location.getBlockX() - 25) + " " + location.getBlockY() + " " + (location.getBlockZ() - 25) + " " + (location.getBlockX() - 25) + " " + location.getBlockY() + " " + (location.getBlockZ() + 25) + " minecraft:bedrock");
            Bukkit.dispatchCommand(opPlayer, "fill " + (location.getBlockX() + 25) + " " + location.getBlockY() + " " + (location.getBlockZ() - 25) + " " + (location.getBlockX() + 25) + " " + location.getBlockY() + " " + (location.getBlockZ() + 25) + " minecraft:bedrock");
            Bukkit.dispatchCommand(opPlayer, "fill " + (location.getBlockX() - 25) + " " + location.getBlockY() + " " + (location.getBlockZ() - 25) + " " + (location.getBlockX() + 25) + " " + location.getBlockY() + " " + (location.getBlockZ() - 25) + " minecraft:bedrock");
            Bukkit.dispatchCommand(opPlayer, "fill " + (location.getBlockX() - 25) + " " + location.getBlockY() + " " + (location.getBlockZ() + 25) + " " + (location.getBlockX() + 25) + " " + location.getBlockY() + " " + (location.getBlockZ() + 25) + " minecraft:bedrock");
            Bukkit.dispatchCommand(opPlayer, "fill " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + location.getBlockX() + " " + (location.getBlockY() + 3) + " " + location.getBlockZ() + " minecraft:bedrock");
        }
    }

    // Listening

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

    public void unregisterEvents()
    {
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    // Utils

    private List<ItemStack> baseStuff()
    {
        List<ItemStack> items = Arrays.asList(
                ItemHelper.generateItem(Material.STONE_AXE, 1, "Hache du schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.ACACIA_BOAT, 1, "Bateau du schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.WATER_BUCKET, 1, "Flotte pour schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.COOKED_BEEF, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.TORCH, 32, "Lumière du schlag", Arrays.asList(), false, null, false, false)
        );

        return items;
    }
}
