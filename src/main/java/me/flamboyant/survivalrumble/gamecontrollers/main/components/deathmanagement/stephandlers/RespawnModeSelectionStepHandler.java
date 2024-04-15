package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class RespawnModeSelectionStepHandler implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private static final int timerSeconds = 10;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();
    private RespawnModeSelectionView respawnModeSelectionView;

    public RespawnModeSelectionStepHandler() {
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
                respawnModeSelectionView.close(player);
                onClassicRespawnSelection(player);

                return;
            }

            playerToCountdown.put(player, currentRemainingSeconds);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, currentRemainingSeconds > 3 ? 0.5f : 1f, 1);

            if (currentRemainingSeconds <= 5) {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1);
            }
            else {
                player.playSound(player, Sound.BLOCK_WOOD_PLACE, SoundCategory.MASTER, 0.5f, 0.7f);
            }
        }
    }

    private void onClassicRespawnSelection(Player player) {
        triggerWorkflowEvent(player, DeathWorkflowEventType.SELECT_NORMAL_RESPAWN);
    }

    private void onSpecialRespawnSelection(Player player) {
        player.setBedSpawnLocation(null);
        triggerWorkflowEvent(player, DeathWorkflowEventType.SELECT_SPECIAL_RESPAWN);
    }

    private void triggerWorkflowEvent(Player player, DeathWorkflowEventType eventType) {
        playerToCountdown.remove(player);

        if (playerToCountdown.isEmpty()) {
            Bukkit.getScheduler().cancelTask(tickSoundTask.getTaskId());
        }

        DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(player);
        playerToPendingDeathWorkflowData.remove(player);

        DeathWorkflowOrchestrator.getInstance().onEventTriggered(eventType, deathWorkflowData);
    }
}
