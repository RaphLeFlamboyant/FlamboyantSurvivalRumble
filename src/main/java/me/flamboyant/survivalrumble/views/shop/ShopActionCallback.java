package me.flamboyant.survivalrumble.views.shop;

import org.bukkit.entity.Player;

public interface ShopActionCallback {
    void onShopAction(Player player, int itemId, int quantity);
}
