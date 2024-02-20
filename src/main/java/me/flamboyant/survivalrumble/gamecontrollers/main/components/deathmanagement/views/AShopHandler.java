package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.shop.IShopChangesListener;
import me.flamboyant.survivalrumble.shop.ItemStackShop;
import me.flamboyant.survivalrumble.shop.ShopItem;
import me.flamboyant.survivalrumble.shop.TeamMoneyManager;
import me.flamboyant.survivalrumble.views.shop.ShopView;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

public abstract class AShopHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData>, IShopChangesListener {
    private static final int timerSeconds = 60;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();
    private ItemStackShop itemShop;
    private ShopView shopView;

    protected abstract DeathWorkflowStepType GetStepType();
    protected abstract List<ItemStack> FilterKeptItem(List<ItemStack> keptItems);
    protected abstract int getUnitaryPrice(ItemStack item);

    public AShopHandler()
    {
        this.itemShop = new ItemStackShop(TeamMoneyManager.getInstance());
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != GetStepType()) return;

        for (ItemStack keptItem : FilterKeptItem(deathWorkflowData.keptItems)) {
            itemShop.AddItemStackToShop(keptItem, getUnitaryPrice(keptItem), keptItem.getAmount());
        }

        if (playerToPendingDeathWorkflowData.isEmpty()) {
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);

            shopView = new ShopView();
        }

        shopView.setItemControllerList(itemShop.getAllShopItemControllers());

        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
        playerToCountdown.put(deathWorkflowData.deadPlayer, timerSeconds);
        shopView.open(deathWorkflowData.deadPlayer);

        deathWorkflowData.deadPlayer.sendTitle(String.valueOf(timerSeconds), "secondes pour choisir",0, 20, 20);
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {

    }

    @Override
    public void ItemAdded(ShopItem shopItem) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsAdded(List<ShopItem> shopItems) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemRemoved(ShopItem shopItem) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsRemoved(List<ShopItem> shopItems) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemUpdated(ShopItem shopItem) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsUpdated(List<ShopItem> shopItems) {
        shopView.setItemControllerList(itemShop.getAllShopItemControllers());
    }

    private void tickSoundOnPendingPlayers() {
        for (Player player : playerToPendingDeathWorkflowData.keySet()) {
            int currentRemainingSeconds = playerToCountdown.get(player) - 1;

            if (currentRemainingSeconds == 0) {
                playerToCountdown.remove(player);
                shopView.close(player);

                if (playerToCountdown.isEmpty()) {
                    Bukkit.getScheduler().cancelTask(tickSoundTask.getTaskId());
                }
                return;
            }

            playerToCountdown.put(player, currentRemainingSeconds);

            if (currentRemainingSeconds <= 5) {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1);
            }
        }
    }
}
