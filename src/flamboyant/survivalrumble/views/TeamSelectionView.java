package flamboyant.survivalrumble.views;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TeamSelectionView implements Listener
{
    private Inventory view;
    private SurvivalRumbleData data()
    {
        return SurvivalRumbleData.getSingleton();
    }

    private static TeamSelectionView instance;
    public static TeamSelectionView getInstance()
    {
        if (instance == null)
        {
            instance = new TeamSelectionView();
        }

        return instance;
    }

    protected TeamSelectionView ()
    {
    }

    public static String getViewID()
    {
        return "Team selection view";
    }

    public Inventory getViewInstance()
    {
        if (view == null) {
            Inventory myInventory = Bukkit.createInventory(null, 9, getViewID());

            for (String teamName : TeamHelper.teamNames)
            {
                ItemStack flag = getTeamFlag(teamName);
                myInventory.setItem(TeamHelper.teamNames.indexOf(teamName) * 2 + 1, flag);
            }

            view = myInventory;
        }

        return view;
    }

    private ItemStack getTeamFlag(String teamName)
    {
        return ItemHelper.generateItem(TeamHelper.getTeamFlagMaterial(teamName), 1, teamName + " team", Arrays.asList(teamName), false, null, false, false);
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

        ScoreboardBricklayer scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
        System.out.println("Team Name : " + teamName);
        System.out.println("Team got : " + (scoreboardBricklayer.getTeam(teamName) != null));
        scoreboardBricklayer.getTeam(teamName).addPlayer(player);
        data().playersTeam.put(player.getUniqueId(), teamName);

        if (!data().players.containsKey(player.getDisplayName()))
            data().players.put(player.getDisplayName(), player.getUniqueId());

        data().saveData();

        player.sendMessage("You go to team " + TeamHelper.getTeamColor(teamName) + teamName);
    }

    public void unregisterEvents()
    {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
