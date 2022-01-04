package flamboyant.survivalrumble.views;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TeamHQParametersView implements Listener {
    private static TeamHQParametersView instance;
    private Inventory view;

    protected TeamHQParametersView() {
    }

    public static TeamHQParametersView getInstance() {
        if (instance == null) {
            instance = new TeamHQParametersView();
        }

        return instance;
    }

    public static String getViewID() {
        return "Team parameters view";
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public Inventory getViewInstance() {
        if (view == null) {
            Inventory myInventory = Bukkit.createInventory(null, 45, getViewID());

            for (String teamName : TeamHelper.teamNames) {
                ItemStack spawn = getTeamSpawnItem(teamName);
                myInventory.setItem(TeamHelper.teamNames.indexOf(teamName) * 2 + 10, spawn);
                ItemStack flag = getTeamHQItem(teamName);
                myInventory.setItem(TeamHelper.teamNames.indexOf(teamName) * 2 + 28, flag);
            }

            view = myInventory;
        }

        return view;
    }

    private ItemStack getTeamSpawnItem(String teamName) {
        return ItemHelper.generateItem(TeamHelper.getTeamBedMaterial(teamName), 1, teamName + " team 0 spawn location", Arrays.asList(teamName), false, null, false, false);
    }

    private ItemStack getTeamHQItem(String teamName) {
        return ItemHelper.generateItem(TeamHelper.getTeamConcreteMaterial(teamName), 1, teamName + " team HQ location", Arrays.asList(teamName), false, null, false, false);
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

        Location playerLocation = player.getLocation();
        Location location = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());

        if (clicked.getType().toString().contains("BED"))
            data().teamSpawnLocation.put(teamName, location);
        else
            data().teamHeadquarterLocation.put(teamName, location);
        data().saveData();

        ItemMeta meta = clicked.getItemMeta();
        String displayBase = meta.getDisplayName().split(" | ")[0];
        meta.setDisplayName(displayBase + " | " + location.getBlockX() + " - " + location.getBlockY() + " - " + location.getBlockZ());
        clicked.setItemMeta(meta);
    }

    public void unregisterEvents() {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
