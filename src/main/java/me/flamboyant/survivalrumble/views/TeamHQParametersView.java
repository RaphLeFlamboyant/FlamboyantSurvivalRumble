package me.flamboyant.survivalrumble.views;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.StructureHelper;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class TeamHQParametersView implements Listener {
    private Inventory view;
    private HashMap<String, Location> teamHeadquarterLocation = new HashMap<>();

    public static String getViewID() {
        return "Team parameters view";
    }

    public Inventory getViewInstance() {
        if (view == null) {
            Inventory myInventory = Bukkit.createInventory(null, 45, getViewID());

            for (String teamName : TeamHelper.teamNames) {
                ItemStack hqLocationItem = getTeamHQItem(teamName);
                myInventory.setItem(TeamHelper.teamNames.indexOf(teamName) * 2 + 19, hqLocationItem);
            }

            view = myInventory;

            Common.server.getPluginManager().registerEvents(this, Common.plugin);
        }

        return view;
    }

    public void close() {
        view = null;
        teamHeadquarterLocation.clear();
        InventoryClickEvent.getHandlerList().unregister(this);
    }

    public HashMap<String, Location> getTeamHeadquarterLocation() {
        return teamHeadquarterLocation;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != getViewInstance()) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir())
            return;

        String teamName = clicked.getItemMeta().getLore().get(0);

        if (teamHeadquarterLocation.containsKey(teamName)) {
            Location l = teamHeadquarterLocation.get(teamName);
            Bukkit.broadcastMessage(ChatColors.debugMessage("Cette équipe déjà une base en " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ()));
            return;
        }

        Location playerLocation = player.getLocation();
        Location location = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());

        placeTeamHeadquarter(teamName, location);

        ItemMeta meta = clicked.getItemMeta();
        String displayBase = meta.getDisplayName().split(" | ")[0];
        meta.setDisplayName(displayBase + " | " + location.getBlockX() + " - " + location.getBlockY() + " - " + location.getBlockZ());
        clicked.setItemMeta(meta);
    }

    private void placeTeamHeadquarter(String teamName, Location location) {
        teamHeadquarterLocation.put(teamName, location);
        Location loc = new Location(location.getWorld(), location.getX() - 4, location.getY() - 2, location.getZ() - 5);
        StructureHelper.spawnStructure(TeamHelper.getTeamStructureName(teamName), loc);
    }

    private ItemStack getTeamHQItem(String teamName) {
        return ItemHelper.generateItem(TeamHelper.getTeamConcreteMaterial(teamName), 1, "Generate " + teamName + " team HQ", Arrays.asList(teamName), false, null, false, false);
    }
}
