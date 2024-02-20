package me.flamboyant.survivalrumble.shop;

import java.util.List;

public interface IShopChangesListener {
    void ItemAdded(ShopItem shopItem);
    void ItemsAdded(List<ShopItem> shopItems);
    void ItemRemoved(ShopItem shopItem);
    void ItemsRemoved(List<ShopItem> shopItems);
    void ItemUpdated(ShopItem shopItem);
    void ItemsUpdated(List<ShopItem> shopItems);
}
