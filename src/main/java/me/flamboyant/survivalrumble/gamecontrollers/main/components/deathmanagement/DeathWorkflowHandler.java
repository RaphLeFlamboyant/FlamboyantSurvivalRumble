package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DeathWorkflowHandler {
    private HashMap<Player, DeathWorkflowData> playerToWorkflowData = new HashMap<>();
    private IDeathWorkflowStep startStep;
    private EndWorkflowCallback endWorkflowCallback;

    public DeathWorkflowHandler(IDeathWorkflowStep workflowStartStep, EndWorkflowCallback endWorkflowCallback) {
        this.endWorkflowCallback = endWorkflowCallback;
        this.startStep = workflowStartStep;
    }

    public void startWorkflow(DeathWorkflowData workflowData) {
        if (playerToWorkflowData.containsKey(workflowData.deadPlayer)) {
            Bukkit.getLogger().warning("A death workflow is already running for player " + workflowData.deadPlayer.getDisplayName());
            return;
        }

        playerToWorkflowData.put(workflowData.deadPlayer, workflowData);

        startStep.runStep(workflowData, this::goToNextStep);
    }

    private void goToNextStep(IDeathWorkflowStep nextStep, Player player) {
        if (nextStep == null) {
            DeathWorkflowData data = playerToWorkflowData.get(player);
            playerToWorkflowData.remove(player);
            endWorkflowCallback.onWorkflowEnds(data);
            return;
        }

        nextStep.runStep(playerToWorkflowData.get(player), this::goToNextStep);
    }
}
