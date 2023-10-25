package me.flamboyant.survivalrumble.views.shop;

import me.flamboyant.gui.view.icons.IIconItem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShopIconItem implements IIconItem {
    private ShopActionCallback buyItemCallback;
    private String category;
    private ItemStack item;
    private int itemId;

    public ShopIconItem(ShopActionCallback buyItemCallback, String category, ItemStack item, int itemId, int unitPrice) {
        this.category = category;
        this.item = item.clone();
        this.itemId = itemId;
        this.buyItemCallback = buyItemCallback;

        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Arrays.asList("Price : " + unitPrice + "/u"));
        this.item.setItemMeta(meta);
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean closeViewOnClick() {
        return false;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;

        Player player = (Player)event.getWhoClicked();

        int quantity = event.isShiftClick() ? item.getAmount() : 1;
        item.setAmount(item.getAmount() - quantity);

        buyItemCallback.onShopAction(player, itemId, quantity);
    }
}
