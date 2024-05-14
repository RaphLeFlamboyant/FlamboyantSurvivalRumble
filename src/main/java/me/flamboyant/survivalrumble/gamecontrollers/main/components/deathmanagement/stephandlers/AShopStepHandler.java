package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

import me.flamboyant.survivalrumble.delegates.RunOnPlayerCallback;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class AShopStepHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData>, IShopChangesListener {
    private static final int timerSeconds = 120;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();
    private ItemStackShop itemShop;
    private ShopView shopView;
    private RunOnPlayerCallback playerCloseShopCallback = (p) -> {
        Bukkit.getLogger().info("[AShopStepHandler] playerCloseShopCallback");
        onShopStepEndind(p);
    };

    protected abstract DeathWorkflowStepType GetStepType();
    protected abstract DeathWorkflowEventType GetEventType();
    protected abstract List<ItemStack> FilterKeptItem(List<ItemStack> keptItems);
    protected abstract int getUnitaryPrice(ItemStack item);

    public AShopStepHandler(String viewName)
    {
        Bukkit.getLogger().info("[AShopStepHandler] Creating view : " + viewName);
        shopView = new ShopView(viewName, Arrays.asList());
        itemShop = new ItemStackShop(TeamMoneyManager.getInstance());
        itemShop.addShopContentChangeListener(this);
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != GetStepType()) return;


        Bukkit.getLogger().info("[AShopStepHandler] Shop triggered on event " + deathWorkflowStepType.toString() + " for player " + deathWorkflowData.deadPlayer.getDisplayName());
        Bukkit.getLogger().info("[AShopStepHandler] ITEMS IN INVENTORY WHEN DEAD");
        for (var item : deathWorkflowData.keptItems) {
            Bukkit.getLogger().info("[AShopStepHandler]   - " + item.getType() + " x " + item.getAmount());
        }
        Bukkit.getLogger().info("[AShopStepHandler] ## END");

        Bukkit.getLogger().info("[AShopStepHandler] FILTERED ITEMS");
        for (ItemStack keptItem : FilterKeptItem(deathWorkflowData.keptItems)) {
            Bukkit.getLogger().info("[AShopStepHandler]   - " + keptItem.getType() + " x " + keptItem.getAmount());
            itemShop.addItemStackToShop(keptItem, getUnitaryPrice(keptItem), keptItem.getAmount());
        }
        Bukkit.getLogger().info("[AShopStepHandler] ## END");

        if (playerToPendingDeathWorkflowData.isEmpty()) {
            Bukkit.getLogger().info("[AShopStepHandler] FIRST PLAYER ENTERING THE SHOP");
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);

            shopView.addPlayerCloseShopCallback(playerCloseShopCallback);
        }
        else {
            Bukkit.getLogger().info("[AShopStepHandler] CONCURRENT PLAYER ENTERING THE SHOP");
            shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
        }

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
        // TODO c'est mal fait
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsAdded(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemRemoved(ShopItem shopItem) {
        Bukkit.getLogger().info("[AShopStepHandler.ItemRemoved]");
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsRemoved(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemUpdated(ShopItem shopItem) {
        Bukkit.getLogger().info("[AShopStepHandler.ItemUpdated]");
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsUpdated(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(itemShop.getAllShopItemControllers());
    }

    private void tickSoundOnPendingPlayers() {
        for (Player player : playerToPendingDeathWorkflowData.keySet()) {
            int currentRemainingSeconds = playerToCountdown.get(player) - 1;

            if (currentRemainingSeconds == 0) {
                shopView.close(player);
                //onShopStepEndind(player);

                return;
            }

            playerToCountdown.put(player, currentRemainingSeconds);

            if (currentRemainingSeconds <= 5) {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1);
            }
        }
    }

    private void onShopStepEndind(Player player) {
        triggerNextStep(player, GetEventType());
    }

    private void triggerNextStep(Player player, DeathWorkflowEventType eventType) {
        playerToCountdown.remove(player);

        if (playerToCountdown.isEmpty()) {
            Bukkit.getScheduler().cancelTask(tickSoundTask.getTaskId());
        }

        DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(player);
        playerToPendingDeathWorkflowData.remove(player);

        if (playerToPendingDeathWorkflowData.isEmpty()) {
            shopView.removePlayerCloseShopCallback(playerCloseShopCallback);
        }

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> DeathWorkflowOrchestrator.getInstance().onEventTriggered(eventType, deathWorkflowData), 1);
    }
}
