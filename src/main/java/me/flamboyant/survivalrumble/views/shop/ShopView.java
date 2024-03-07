package me.flamboyant.survivalrumble.views.shop;

import me.flamboyant.gui.GuiActionCallback;
import me.flamboyant.gui.view.IconController;
import me.flamboyant.gui.view.InventoryView;
import me.flamboyant.survivalrumble.delegates.RunOnPlayerCallback;
import me.flamboyant.survivalrumble.shop.ShopItemController;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopView {
    private InventoryView inventoryView;
    private HashMap<RunOnPlayerCallback, GuiActionCallback> playerCallbackToGuiCallback = new HashMap<>();

    public ShopView(String viewName, List<ShopItemController> shopItemControllers) {
        inventoryView = new InventoryView(viewName, ConvertControllers(shopItemControllers));
    }

    public void open(Player player) {
        inventoryView.openPlayerView(player);
    }

    public void close(Player player) {
        inventoryView.closePlayerView(player);
    }

    public void resetItemControllerList(List<ShopItemController> shopItemControllers) {
        List<IconController> itemControllers = ConvertControllers(shopItemControllers);
        inventoryView.initializeFrom(itemControllers);
    }

    private List<IconController> ConvertControllers(List<ShopItemController> shopItemControllers) {
        List<IconController> itemControllers = new ArrayList<>();
        int i = 0;
        for (ShopItemController shopItemController : shopItemControllers) {
            IconController iconController = new IconController(i++);
            iconController.setItemIcon(shopItemController.getRepresentation());
            iconController.setLeftClickCallback((Player p) -> shopItemController.getTryBuyOne().tryRunOnPlayer(p));
            iconController.setShiftClickCallback((Player p) -> shopItemController.getTryBuyAll().tryRunOnPlayer(p));

            itemControllers.add(iconController);
        }

        return itemControllers;
    }

    public void addPlayerCloseShopCallback(RunOnPlayerCallback closeShopActionCallback) {
        GuiActionCallback guiActionCallback = p -> closeShopActionCallback.runOnPlayer(p);
        playerCallbackToGuiCallback.put(closeShopActionCallback, guiActionCallback);
        inventoryView.addCloseViewCallback(guiActionCallback);
    }

    public void removePlayerCloseShopCallback(RunOnPlayerCallback closeShopActionCallback) {
        playerCallbackToGuiCallback.remove(closeShopActionCallback);
    }
}
