package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.models.ShopItemData;
import me.flamboyant.survivalrumble.views.respawnmodeselection.RespawnModeSelectionView;
import me.flamboyant.survivalrumble.views.shop.ItemShopView;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class AShopHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private static final int timerSeconds = 60;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();
    private ItemShopView itemShopView;
    private List<ShopItemData> shopItems = new ArrayList<>();

    vdsvdsvds LIRE LES NOTES SUR TELEPHONE;

    protected abstract List<ShopItemData> CreateHandledItems(List<ItemStack> items);
    protected abstract DeathWorkflowStepType GetStepType();

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != GetStepType()) return;

        if (playerToPendingDeathWorkflowData.size() == 0) {
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);
        }

        List<ShopItemData> newShopItems = CreateHandledItems(deathWorkflowData.keptItems);
        shopItems.addAll(newShopItems);

        if (playerToPendingDeathWorkflowData.isEmpty()) {
            // TODO create view
        }
        else {
            // TODO : add items to view
        }

        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
        playerToCountdown.put(deathWorkflowData.deadPlayer, timerSeconds);
        respawnModeSelectionView.open(deathWorkflowData.deadPlayer);

        deathWorkflowData.deadPlayer.sendTitle(String.valueOf(timerSeconds), "secondes pour choisir",0, 20, 20);

    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {

    }
}
