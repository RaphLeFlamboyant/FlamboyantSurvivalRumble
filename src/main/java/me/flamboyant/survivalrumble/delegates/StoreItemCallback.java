package me.flamboyant.survivalrumble.delegates;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface StoreItemCallback {
    void onItemEvent(int itemId, ItemStack itemStack);
}
