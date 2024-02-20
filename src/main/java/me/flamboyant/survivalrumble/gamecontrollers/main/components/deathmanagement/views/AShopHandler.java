package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.stores.MinecraftItemShop;
import me.flamboyant.survivalrumble.shop.IShopChangesListener;
import me.flamboyant.survivalrumble.shop.ItemStackShop;
import me.flamboyant.survivalrumble.shop.ShopItem;
import me.flamboyant.survivalrumble.views.shop.ShopView;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.Bukkit;
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

    vdsvdsvds LIRE LES NOTES SUR TELEPHONE;

    protected abstract DeathWorkflowStepType GetStepType();

    public AShopHandler(ItemStackShop itemShop)
    {
        this.itemShop = itemShop;
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != GetStepType()) return;

        if (playerToPendingDeathWorkflowData.size() == 0) {
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);
        }

        for (ItemStack keptItem : deathWorkflowData.keptItems) {
            itemShop.AddItemStackToShop(keptItem, getUnitaryPrice(keptItem), keptItem.getAmount());
        }

        if (playerToPendingDeathWorkflowData.isEmpty()) {
            // TODO create view
        }
        else {
            shopView.
            // TODO : add items to view
        }

        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
        playerToCountdown.put(deathWorkflowData.deadPlayer, timerSeconds);
        // TODO open view respawnModeSelectionView.open(deathWorkflowData.deadPlayer);

        deathWorkflowData.deadPlayer.sendTitle(String.valueOf(timerSeconds), "secondes pour choisir",0, 20, 20);
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {

    }

    private int getUnitaryPrice(ItemStack item) {

    }

    @Override
    public void ItemAdded(ShopItem shopItem) {
        
    }

    @Override
    public void ItemsAdded(List<ShopItem> shopItems) {

    }

    @Override
    public void ItemRemoved(ShopItem shopItem) {

    }

    @Override
    public void ItemsRemoved(List<ShopItem> shopItems) {

    }

    @Override
    public void ItemUpdated(ShopItem shopItem) {

    }

    @Override
    public void ItemsUpdated(List<ShopItem> shopItems) {

    }
}
