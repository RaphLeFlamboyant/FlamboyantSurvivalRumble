package me.flamboyant.survivalrumble.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemStackShop {
    private IMoneyManager moneyManager;

    private HashMap<ShopItem, ItemStack> shopItemToItemStack = new HashMap<>();

    public ItemStackShop(IMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    public List<IShopChangesListener> shopChangesListeners = new ArrayList<>();

    public void AddShopContentChangeListener(IShopChangesListener shopChangesListener) {
        shopChangesListeners.add(shopChangesListener);
    }

    public void AddItemStackToShop(ItemStack itemStack, int unitPrice, int quantity) {
        ShopItem shopItem = GenerateShopItem(itemStack, unitPrice, quantity);

        shopItemToItemStack.put(shopItem, itemStack);

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemAdded(shopItem);
        }
    }

    public HashMap<ShopItem, ShopItemController> getAllShopItemController() {
        HashMap<ShopItem, ShopItemController> res = new HashMap<>();

        for (ShopItem shopItem : shopItemToItemStack.keySet()) {
            ShopItemController controller = new ShopItemController();
            controller.setRepresentation(shopItemToItemStack.get(shopItem));
            controller.setTryBuyAll((Player p) -> TryBuyItem(shopItem, p, shopItem.getQuantity()));
            controller.setTryBuyOne((Player p) -> TryBuyItem(shopItem, p, 1));

            res.put(shopItem, controller);
        }

        return res;
    }

    private boolean TryBuyItem(ShopItem shopItem, Player player, int quantity) {
        if (quantity > shopItem.getQuantity()) return false;

        if (!moneyManager.trySpendMoney(player, shopItem.getUnitaryPrice() * quantity)) {
            return false;
        }

        ItemStack shopItemStack = shopItemToItemStack.get(shopItem);
        ItemStack boughtItem = shopItemStack.clone();
        boughtItem.setAmount(quantity);
        player.getInventory().addItem(boughtItem);

        if (shopItem.getQuantity() - quantity > 0) {
            shopItem.setQuantity(shopItem.getQuantity() - quantity);
            shopItemStack.setAmount(shopItem.getQuantity());

            for(IShopChangesListener shopChangesListener : shopChangesListeners) {
                shopChangesListener.ItemUpdated(shopItem);
            }
        }
        else {
            shopItemToItemStack.remove(shopItem);
            for(IShopChangesListener shopChangesListener : shopChangesListeners) {
                shopChangesListener.ItemRemoved(shopItem);
            }
        }

        return true;
    }

    private ShopItem GenerateShopItem(ItemStack itemStack, int unitPrice, int quantity) {
        ShopItem shopItem = new ShopItem();
        shopItem.setItemName(itemStack.getItemMeta().getDisplayName());
        shopItem.setUnitaryPrice(unitPrice);
        shopItem.setQuantity(quantity);
        return shopItem;
    }
}
