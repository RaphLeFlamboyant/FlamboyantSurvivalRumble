package me.flamboyant.survivalrumble.views;

import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class PlayerSelectionView implements Listener {
    private Inventory view;
    private Player selectedPlayer;
    private Player owner;
    private List<Player> exclusion;

    public PlayerSelectionView(Player owner, List<Player> exclusion) {
        this.owner = owner;
        this.exclusion = exclusion;
    }

    public static String getViewID() {
        return "Sélectionne un joueur";
    }

    public Player getSelectedPlayer() { return selectedPlayer; }

    public Inventory getViewInstance() {
        if (view == null) {
            Inventory myInventory = Bukkit.createInventory(null, 9*3, getViewID());

            int pos = 9 * 3 / 2 - (Common.server.getOnlinePlayers().size() - 1) / 2;
            for (Player p : Common.server.getOnlinePlayers()) {
                if (p == owner || exclusion.contains(p)) continue;
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
                skull.setDisplayName(p.getDisplayName());
                skull.setOwningPlayer(p);
                playerHead.setItemMeta(skull);
                myInventory.setItem(pos++, playerHead);
            }

            view = myInventory;
        }

        return view;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != owner) return;
        if (event.getClickedInventory() != view) return;
        if (event.getCurrentItem().getType() != Material.PLAYER_HEAD) return;

        String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
        selectedPlayer = Common.server.getPlayer(playerName);

        owner.closeInventory();
    }
}
