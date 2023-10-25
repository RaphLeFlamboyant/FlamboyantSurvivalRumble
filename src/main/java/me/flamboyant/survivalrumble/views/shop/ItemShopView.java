package me.flamboyant.survivalrumble.views.shop;

import me.flamboyant.gui.view.builder.InventoryGuiBuilder;
import me.flamboyant.gui.view.builder.ItemGroupingMode;
import me.flamboyant.gui.view.common.InventoryGui;
import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.survivalrumble.models.ShopItemData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemShopView {
    private InventoryGui inventoryGui;

    public ItemShopView(String viewName, List<ShopItemData> shopItems, ShopActionCallback onItemBought) {
        List<IIconItem> icons = new ArrayList<>();
        for (ShopItemData shopItem : shopItems) {
            IIconItem icon = new ShopIconItem(onItemBought, shopItem.categoryName, shopItem.item, shopItem.itemId, shopItem.price);
            icons.add(icon);
        }

        inventoryGui = InventoryGuiBuilder.getInstance().buildView(icons, viewName, 9*6, ItemGroupingMode.PARTED, false);
    }

    public void open(Player player) {
        inventoryGui.open(player);
    }
}
