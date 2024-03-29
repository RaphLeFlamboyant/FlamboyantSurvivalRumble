package me.flamboyant.survivalrumble.gamecontrollers.setup;

import me.flamboyant.configurable.gui.ParameterUtils;
import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.EnumParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.gui.view.builder.ItemGroupingMode;
import me.flamboyant.gui.view.common.InventoryGui;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.survivalrumble.views.TeamHQParametersView;
import me.flamboyant.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetupListener implements IParametrable, Listener {
    private static SetupListener instance;
    private ITriggerVisitor visitor;
    private TeamHQParametersView hqParameters;
    private InventoryGui parametersSelectionView;
    private HashMap<Player, String> playersTeam = new HashMap<>();
    private EnumParameter<StartStuffKind> stuffParameter;
    private IntParameter timeParameter;

    protected SetupListener() {
    }

    public static SetupListener getInstance() {
        if (instance == null) {
            instance = new SetupListener();
        }

        return instance;
    }

    public void launch(Player opPlayer, ITriggerVisitor visitor) {
        this.visitor = visitor;

        opPlayer.getInventory().clear();
        opPlayer.getInventory().setItem(0, getParametersItem());
        opPlayer.getInventory().setItem(1, getTeamHQItem());
        opPlayer.getInventory().setItem(5, getLaunchItem());
        opPlayer.getInventory().setItem(8, getCancelItem());

        parametersSelectionView = ParameterUtils.createParametersGui(this, ItemGroupingMode.PARTED, false);

        for (Player player : Common.server.getOnlinePlayers()) {
            player.getPlayer().getInventory().clear();
            String team = TeamHelper.teamNames.get(0);
            playersTeam.put(player, TeamHelper.teamNames.get(0));
            ItemStack item = ItemHelper.generateItem(TeamHelper.getTeamBannerMaterial(team), 1, "Equipe " + team, new ArrayList<>(), false, null, false, true);
            player.getPlayer().getInventory().setItem(2, item);
        }

        hqParameters = new TeamHQParametersView();

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(true);

        if (event.getItem().getType().toString().contains("BANNER")) {
            changePlayerTeam(event.getPlayer(),
                    event.getItem().getType().toString().split("_")[0],
                    event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);
        }
        else if (ItemHelper.isExactlySameItemKind(event.getItem(), getParametersItem())) {
            parametersSelectionView.open(event.getPlayer());
        } else if (ItemHelper.isExactlySameItemKind(event.getItem(), getTeamHQItem())) {
            Inventory teamHQView = hqParameters.getViewInstance();
            event.getPlayer().openInventory(teamHQView);
        }
        else if (ItemHelper.isExactlySameItemKind(event.getItem(), getLaunchItem())) {
            launchGame(event.getPlayer());
        }
        else if (ItemHelper.isExactlySameItemKind(event.getItem(), getCancelItem())) {
            close();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        System.out.println("Swap canceled");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void resetParameters() {
        stuffParameter = new EnumParameter<>(Material.IRON_SWORD, "Stuff de départ", "Stuff de départ", StartStuffKind.class);
        timeParameter = new IntParameter(Material.CLOCK, "Durée avant final", "En quarts d'heure", 2, 32, 16);
    }

    @Override
    public List<AParameter> getParameters() {
        return Arrays.asList(stuffParameter, timeParameter);
    }

    private void changePlayerTeam(Player player, String teamColor, boolean goForward) {
        List<String> teams = TeamHelper.teamNames;
        String selectedTeam = "";
        int i = 0;
        while (playersTeam.get(player) == teamColor) {
            int next = goForward ? (i + 1) % teams.size() : i == 0 ? teams.size() : i - 1;
            if (teams.get(i) == teamColor) {
                selectedTeam = teams.get(next);
                playersTeam.put(player, selectedTeam);
            }
            i = next;
        }

        ItemStack item = ItemHelper.generateItem(TeamHelper.getTeamBannerMaterial(selectedTeam), 1, "Equipe " + selectedTeam, new ArrayList<>(), false, null, false, true);
        player.getPlayer().getInventory().setItem(2, item);
    }

    private void launchGame(Player sender) {
        System.out.println("Le plugin est lancé");
        sender.sendMessage(ChatHelper.feedback("Le plugin est lancé"));

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        data.minutesBeforeEnd = 15 * timeParameter.getValue();

        for (Player player : playersTeam.keySet()) {
            String teamName = playersTeam.get(player);
            data.addTeam(teamName);
            data.addPlayerTeam(player, teamName);
        }

        HashMap<String, Location> hqByTeam = hqParameters.getTeamHeadquarterLocation();
        for (String team : data.getTeams()) {
            data.setHeadquarterLocation(team, hqByTeam.get(team));
        }

        if (hqByTeam.size() < data.getTeams().size()) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Certaines équipes n'ont pas de base enregistrée"));
            Bukkit.getLogger().warning("There are teams without headquarter !");
            return;
        }

        selectTeamChampions();
        StartStuffHelper.givePlayersStartStuff(stuffParameter.getSelectedValue());

        visitor.onAction();

        close();
    }

    private void selectTeamChampions() {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String teamChampionsAnnouncement = "";

        for (String teamName : data.getTeams()) {
            List<Player> players = data.getPlayers(teamName);
            Player champion = players.get(Common.rng.nextInt(players.size()));
            data.setTeamChampion(teamName, champion);
            teamChampionsAnnouncement += "- " + teamName + " : " + champion.getDisplayName() + "\n";
        }

        ChatHelper.titledMessage("LE CHAMPION DE CHAQUE EQUIPE", teamChampionsAnnouncement);
    }

    public void close() {
        if (playersTeam.size() > 0) {
            hqParameters.close();
            unregister();
            playersTeam.clear();
        }
    }

    private void unregister() {
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerDropItemEvent.getHandlerList().unregister(this);
        PlayerSwapHandItemsEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockDamageEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }

    public static ItemStack getTeamHQItem() {
        return ItemHelper.generateItem(Material.COMPASS, 1, "Sélection du QG des teams", new ArrayList<String>(), true, Enchantment.LUCK, true, true);
    }

    private static ItemStack getParametersItem() {
        return ItemHelper.generateItem(Material.REDSTONE, 1, "Paramètres", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }

    private static ItemStack getCancelItem() {
        return ItemHelper.generateItem(Material.ZOMBIE_HEAD, 1, "Annuler", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }

    private static ItemStack getLaunchItem() {
        return ItemHelper.generateItem(Material.TIPPED_ARROW, 1, "Lancer la partie", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }
}
