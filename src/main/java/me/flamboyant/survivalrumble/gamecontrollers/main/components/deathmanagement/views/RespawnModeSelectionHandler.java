package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.views.respawnmodeselection.RespawnModeSelectionView;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class RespawnModeSelectionHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private static final int timerSeconds = 10;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();
    private RespawnModeSelectionView respawnModeSelectionView;

    public RespawnModeSelectionHandler() {
        // Todo : dependency injection
        respawnModeSelectionView = new RespawnModeSelectionView(this::onClassicRespawnSelection, this::onSpecialRespawnSelection);
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != DeathWorkflowStepType.RESPAWN_MODE_SELECTION) return;

        if (playerToPendingDeathWorkflowData.size() == 0) {
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);
        }

        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
        playerToCountdown.put(deathWorkflowData.deadPlayer, timerSeconds);
        respawnModeSelectionView.open(deathWorkflowData.deadPlayer);

        deathWorkflowData.deadPlayer.sendTitle(String.valueOf(timerSeconds), "secondes pour choisir",0, 20, 20);
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {

    }

    private void tickSoundOnPendingPlayers() {
        for (Player player : playerToPendingDeathWorkflowData.keySet()) {
            int currentRemainingSeconds = playerToCountdown.get(player) - 1;

            if (currentRemainingSeconds == 0) {
                playerToCountdown.remove(player);
                onClassicRespawnSelection(player);
                respawnModeSelectionView.close(player);

                if (playerToCountdown.isEmpty()) {
                    Bukkit.getScheduler().cancelTask(tickSoundTask.getTaskId());
                }
                return;
            }

            playerToCountdown.put(player, currentRemainingSeconds);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, currentRemainingSeconds > 3 ? 0.5f : 1f, 1);
        }
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
