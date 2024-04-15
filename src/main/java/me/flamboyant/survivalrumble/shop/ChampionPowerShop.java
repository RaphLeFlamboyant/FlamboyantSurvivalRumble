package me.flamboyant.survivalrumble.shop;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.powers.shop.ChampionPowerShopItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChampionPowerShop {
    private IMoneyManager moneyManager;

    private HashMap<ShopItem, ChampionPowerShopItem> shopItemToChampionPower = new HashMap<>();

    public ChampionPowerShop(IMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    public List<IShopChangesListener> shopChangesListeners = new ArrayList<>();

    public void addShopContentChangeListener(IShopChangesListener shopChangesListener) {
        shopChangesListeners.add(shopChangesListener);
    }

    public void addChampionPowerToShop(ChampionPowerShopItem championPowerShopItem) {
        ShopItem shopItem = generateShopItem(championPowerShopItem);

        shopItemToChampionPower.put(shopItem, championPowerShopItem);

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemAdded(shopItem);
        }
    }

    public List<ShopItemController> getAllShopItemControllers() {
        List<ShopItemController> res = new ArrayList<>();

        for (ShopItem shopItem : shopItemToChampionPower.keySet()) {
            ShopItemController controller = new ShopItemController();

            ChampionPowerShopItem championPowerShopItem = shopItemToChampionPower.get(shopItem);
            ItemStack itemStack = new ItemStack(championPowerShopItem.getPowerAppearance());

            ItemMeta meta = itemStack.getItemMeta();
            if (championPowerShopItem.hasReachedMaxLevel()) {
                meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setAmount(championPowerShopItem.getCurrentPowerLevel());
            }
            else {
                itemStack.setAmount(championPowerShopItem.getCurrentPowerLevel() + 1);
            }
            meta.setDisplayName(championPowerShopItem.getPowerName());
            meta.setLore(championPowerShopItem.getDescription());
            itemStack.setItemMeta(meta);

            controller.setRepresentation(itemStack);
            controller.setTryBuyAll((Player p) -> tryBuyPower(shopItem, p));
            controller.setTryBuyOne((Player p) -> tryBuyPower(shopItem, p));

            res.add(controller);
        }

        return res;
    }

    private boolean tryBuyPower(ShopItem shopItem, Player player) {
        ChampionPowerShopItem championPowerShopItem = shopItemToChampionPower.get(shopItem);

        if (championPowerShopItem.hasReachedMaxLevel()) return false;
        if (!moneyManager.trySpendMoney(player, championPowerShopItem.getPrice())) return false;

        championPowerShopItem.levelUp();

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String teamName = data.getPlayerTeam(player);
        data.setChampionPowerTypeLevel(teamName, championPowerShopItem.getChampionPowerType(), championPowerShopItem.getCurrentPowerLevel());

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemUpdated(shopItem);
        }

        return true;
    }

    private ShopItem generateShopItem(ChampionPowerShopItem championPowerShopItem) {
        ShopItem shopItem = new ShopItem();
        shopItem.setItemName(championPowerShopItem.getPowerName());
        return shopItem;
    }
}
