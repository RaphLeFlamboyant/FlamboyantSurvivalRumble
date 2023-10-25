package me.flamboyant.survivalrumble.views;

import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.survivalrumble.utils.PlayerCallback;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CallbackIconItem implements IIconItem {
    private PlayerCallback callback;
    private String category;
    private ItemStack item;
    private boolean closeOnClick;

    public CallbackIconItem(PlayerCallback callback, String category, ItemStack item, boolean closeOnClick) {
        this.callback = callback;
        this.category = category;
        this.item = item;
        this.closeOnClick = closeOnClick;
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
        return closeOnClick;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;

        callback.runOnPlayer((Player)event.getWhoClicked());
    }
}
