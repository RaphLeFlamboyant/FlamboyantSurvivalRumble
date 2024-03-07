package me.flamboyant.survivalrumble.shop;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.powers.ChampionPower;
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

    private HashMap<ShopItem, ChampionPower> shopItemToChampionPower = new HashMap<>();

    public ChampionPowerShop(IMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    public List<IShopChangesListener> shopChangesListeners = new ArrayList<>();

    public void addShopContentChangeListener(IShopChangesListener shopChangesListener) {
        shopChangesListeners.add(shopChangesListener);
    }

    public void addChampionPowerToShop(ChampionPower championPower) {
        ShopItem shopItem = generateShopItem(championPower);

        shopItemToChampionPower.put(shopItem, championPower);

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemAdded(shopItem);
        }
    }

    public List<ShopItemController> getAllShopItemControllers() {
        List<ShopItemController> res = new ArrayList<>();

        for (ShopItem shopItem : shopItemToChampionPower.keySet()) {
            ShopItemController controller = new ShopItemController();

            ChampionPower championPower = shopItemToChampionPower.get(shopItem);
            ItemStack itemStack = new ItemStack(championPower.getPowerAppearance());

            ItemMeta meta = itemStack.getItemMeta();
            if (championPower.hasReachedMaxLevel()) {
                meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setAmount(championPower.getCurrentPowerLevel());
            }
            else {
                itemStack.setAmount(championPower.getCurrentPowerLevel() + 1);
            }
            meta.setDisplayName(championPower.getPowerName());
            meta.setLore(championPower.getDescription());
            itemStack.setItemMeta(meta);

            controller.setRepresentation(itemStack);
            controller.setTryBuyAll((Player p) -> tryBuyPower(shopItem, p));
            controller.setTryBuyOne((Player p) -> tryBuyPower(shopItem, p));

            res.add(controller);
        }

        return res;
    }

    private boolean tryBuyPower(ShopItem shopItem, Player player) {
        ChampionPower championPower = shopItemToChampionPower.get(shopItem);

        if (championPower.hasReachedMaxLevel()) return false;
        if (!moneyManager.trySpendMoney(player, championPower.getPrice())) return false;

        championPower.levelUp();

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String teamName = data.getPlayerTeam(player);
        data.setChampionPowerTypeLevel(teamName, championPower.getChampionPowerType(), championPower.getCurrentPowerLevel());

        for(IShopChangesListener shopChangesListener : shopChangesListeners) {
            shopChangesListener.ItemUpdated(shopItem);
        }

        return true;
    }

    private ShopItem generateShopItem(ChampionPower championPower) {
        ShopItem shopItem = new ShopItem();
        shopItem.setItemName(championPower.getPowerName());
        return shopItem;
    }
}
