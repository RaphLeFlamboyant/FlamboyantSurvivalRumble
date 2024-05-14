package me.flamboyant.survivalrumble.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemStackShop {
    private IMoneyManager moneyManager;

    private HashMap<ShopItem, ItemStack> shopItemToItemStack = new HashMap<>();

    public ItemStackShop(IMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    public List<IShopChangesListener> shopChangesListeners = new ArrayList<>();

    public void addShopContentChangeListener(IShopChangesListener shopChangesListener) {
        shopChangesListeners.add(shopChangesListener);
    }

    public void removeShopContentChangeListener(IShopChangesListener shopChangesListener) {
        shopChangesListeners.remove(shopChangesListener);
    }

    public void addItemStackToShop(ItemStack itemStack, int unitPrice, int quantity) {
        ShopItem shopItem = GenerateShopItem(itemStack, unitPrice, quantity);

        shopItemToItemStack.put(shopItem, itemStack);

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemAdded(shopItem);
        }
    }

    public List<ShopItemController> getAllShopItemControllers() {
        List<ShopItemController> res = new ArrayList<>();

        for (ShopItem shopItem : shopItemToItemStack.keySet()) {
            ShopItemController controller = new ShopItemController();

            ItemStack itemStack = shopItemToItemStack.get(shopItem).clone();
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(Arrays.asList("Prix unitaire : " + shopItem.getUnitaryPrice()));
            itemStack.setItemMeta(meta);

            controller.setRepresentation(itemStack);
            controller.setTryBuyAll((Player p) -> tryBuyItem(shopItem, p, shopItem.getQuantity()));
            controller.setTryBuyOne((Player p) -> tryBuyItem(shopItem, p, 1));

            res.add(controller);
        }

        return res;
    }

    private boolean tryBuyItem(ShopItem shopItem, Player player, int quantity) {
        Bukkit.getLogger().info("[ItemStackShop.tryBuyItem] Item : " + shopItem.getItemName() + " for player " + player.getDisplayName() + " and quantity = " + quantity);
        if (quantity > shopItem.getQuantity()) return false;

        if (!moneyManager.trySpendMoney(player, shopItem.getUnitaryPrice() * quantity)) {
            return false;
        }
        Bukkit.getLogger().info("[ItemStackShop.tryBuyItem] Succeed");

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
        Bukkit.getLogger().info("[ItemStackShop.GenerateShopItem] With ItemStack of type " + itemStack.getType() + " and display name " + itemStack.getItemMeta().getDisplayName());
        shopItem.setItemName(itemStack.getItemMeta().getDisplayName());
        shopItem.setUnitaryPrice(unitPrice);
        shopItem.setQuantity(quantity);
        return shopItem;
    }
}
