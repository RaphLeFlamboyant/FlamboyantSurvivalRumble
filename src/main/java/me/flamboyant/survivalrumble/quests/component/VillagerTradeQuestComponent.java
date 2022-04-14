package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerTradeQuestComponent extends AQuestComponent implements Listener {
    private Material material;
    private int quantity;
    private boolean give;

    public VillagerTradeQuestComponent(Player player, Material material, int quantity, boolean give) {
        super(player);
        this.material = material;
        this.quantity = quantity;
        this.give = give;

        String prefix = give ? "échange " : "Obtiens ";
        subQuestMessage = prefix + quantity + " x " + material.toString() + " avec un villageois";
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        InventoryClickEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getUniqueId() != player.getUniqueId()) return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.MERCHANT)
            return;
        if (event.getSlotType() != InventoryType.SlotType.RESULT) return;

        MerchantInventory merchantInventory = (MerchantInventory) event.getClickedInventory();
        MerchantRecipe recipe = merchantInventory.getSelectedRecipe();

        int inventorySlot = 0;
        int numberOfPossibleTrades = 9999;
        boolean isMaterialInTrade = false;
        int baseQty = 0;
        for (ItemStack recipeItemStack : recipe.getIngredients()) {
            int recipeAmount = recipeItemStack.getAmount();
            ItemStack itemStack = merchantInventory.getItem(inventorySlot);
            inventorySlot++;

            if (itemStack == null || recipeItemStack.getType() == Material.AIR) continue;

            int inventoryAmount = itemStack.getAmount();
            numberOfPossibleTrades = Integer.min(numberOfPossibleTrades, inventoryAmount / recipeAmount);

            if (give && recipeItemStack.getType() == material) {
                isMaterialInTrade = true;
                baseQty += recipeAmount;
            }
            inventorySlot++;
        }

        if (!give && recipe.getResult().getType() == material) {
            isMaterialInTrade = true;
            baseQty += recipe.getResult().getAmount();
        }

        if (!isMaterialInTrade) return;

        quantity -= event.isShiftClick() ? baseQty * numberOfPossibleTrades : baseQty;
        if (quantity <= 0) {
            stopQuest();
        }
    }
}
