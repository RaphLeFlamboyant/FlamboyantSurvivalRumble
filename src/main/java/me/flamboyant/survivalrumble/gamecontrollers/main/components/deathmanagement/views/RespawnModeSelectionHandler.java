package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.views.respawnmodeselection.RespawnModeSelectionView;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class RespawnModeSelectionHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private RespawnModeSelectionView respawnModeSelectionView;

    public RespawnModeSelectionHandler() {
        // Todo : dependency injection
        respawnModeSelectionView = new RespawnModeSelectionView((p) -> onClassicRespawnSelection(p), (p) -> onSpecialRespawnSelection(p));
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {

    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != DeathWorkflowStepType.RESPAWN_MODE_SELECTION) return;

        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
        respawnModeSelectionView.open(deathWorkflowData.deadPlayer);
        // TODO : le chrono
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {

    }

    private void onClassicRespawnSelection(Player player) {
        DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(player);
        playerToPendingDeathWorkflowData.remove(player);

        DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.SELECT_NORMAL_RESPAWN, deathWorkflowData);
    }

    private void onSpecialRespawnSelection(Player player) {
        DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(player);
        playerToPendingDeathWorkflowData.remove(player);

        DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.SELECT_SPECIAL_RESPAWN, deathWorkflowData);
    }
}
