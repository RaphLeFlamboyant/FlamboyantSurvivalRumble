package flamboyant.survivalrumble.views;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TeamHQParametersView implements Listener
{
    private Inventory view;

    private SurvivalRumbleData data()
    {
        return SurvivalRumbleData.getSingleton();
    }

    private static TeamHQParametersView instance;
    public static TeamHQParametersView getInstance()
    {
        if (instance == null)
        {
            instance = new TeamHQParametersView();
        }

        return instance;
    }

    protected TeamHQParametersView ()
    {
    }

    public static String getViewID()
    {
        return "Team HQ parameters view";
    }

    public Inventory getViewInstance()
    {
        if (view == null) {
            Inventory myInventory = Bukkit.createInventory(null, 9, getViewID());

            for (String teamName : TeamHelper.teamNames)
            {
                ItemStack flag = getTeamHQItem(teamName);
                myInventory.setItem(TeamHelper.teamNames.indexOf(teamName) * 2 + 1, flag);
            }

            view = myInventory;
        }

        return view;
    }

    private ItemStack getTeamHQItem(String teamName)
    {
        return ItemHelper.generateItem(TeamHelper.getTeamConcreteMaterial(teamName), 1, teamName + " team HQ", Arrays.asList(teamName), false, null, false, false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
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

        data().teamHeadquarterLocation.put(teamName, location);
        data().saveData();

        ItemMeta meta = clicked.getItemMeta();
        meta.setDisplayName(teamName + " team HQ " + location.getBlockX() + " - " + location.getBlockY() + " - " + location.getBlockZ());
        clicked.setItemMeta(meta);
        player.sendMessage("L'équipe " + TeamHelper.getTeamColor(teamName) + teamName + ChatColor.WHITE + " a désormais sa base en " + location.getBlockX() + " - " +  location.getBlockY() + " - " +  location.getBlockZ());
    }

    public void unregisterEvents()
    {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
