package me.flamboyant.survivalrumble.views.shop;

import me.flamboyant.gui.view.IconController;
import me.flamboyant.gui.view.InventoryView;
import me.flamboyant.survivalrumble.shop.ShopItemController;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShopView {
    private InventoryView inventoryView;

    public void open(Player player) {
        inventoryView.openPlayerView(player);
    }

    public void close(Player player) {
        inventoryView.closePlayerView(player);
    }

    public void setItemControllerList(List<ShopItemController> shopItemControllers) {
        List<IconController> iconControllers = new ArrayList<IconController>();

        int i = 0;
        for (ShopItemController shopItemController : shopItemControllers) {
            IconController iconController = new IconController(i++);
            iconController.setItemIcon(shopItemController.getRepresentation());
            iconController.setLeftClickCallback((Player p) -> shopItemController.getTryBuyOne().tryRunOnPlayer(p));
            iconController.setShiftClickCallback((Player p) -> shopItemController.getTryBuyAll().tryRunOnPlayer(p));
        }
    }
}
