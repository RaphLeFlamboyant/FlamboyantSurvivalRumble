package me.flamboyant.survivalrumble.views;

import me.flamboyant.gui.view.icons.IIconItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CallbackIconItem implements IIconItem {
    private Runnable callback;
    private String category;
    private ItemStack item;

    public CallbackIconItem(Runnable callback, String category, ItemStack item) {
        this.callback = callback;
        this.category = category;
        this.item = item;
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
    public void onClick(InventoryClickEvent event) {
        callback.run();
    }
}
