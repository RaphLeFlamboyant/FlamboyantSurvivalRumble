package me.flamboyant.survivalrumble.delegates;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ShopActionCallback {
    void onStoreAction(int itemId, int quantity, Player player);
}
