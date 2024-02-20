package me.flamboyant.survivalrumble.views.shop;

import me.flamboyant.gui.view.InventoryView;
import org.bukkit.entity.Player;

public class ShopView {
    private InventoryView inventoryView;


    public void open(Player player) {
        inventoryView.openPlayerView(player);
    }

    public void close(Player player) {
        inventoryView.closePlayerView(player);
    }
}
