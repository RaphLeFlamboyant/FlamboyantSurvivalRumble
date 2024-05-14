package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class GhostModeStepHandler implements WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private static final int timerSeconds = 5;
    private BukkitTask tickSoundTask;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();
    private HashMap<Player, Integer> playerToCountdown = new HashMap<>();


    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType != DeathWorkflowStepType.GHOST_MODE) return;

        if (playerToPendingDeathWorkflowData.size() == 0) {
            tickSoundTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, this::tickSoundOnPendingPlayers, 20, 20);
        }

        Player player = deathWorkflowData.deadPlayer;

        playerToPendingDeathWorkflowData.put(player, deathWorkflowData);
        playerToCountdown.put(player, timerSeconds);

        player.sendTitle(String.valueOf(timerSeconds), "secondes avant spawn",0, 20, 20);
        player.setGameMode(GameMode.SPECTATOR);

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Location headquarterLocation = data.getHeadquarterLocation(data.getPlayerTeam(player));
        player.teleport(headquarterLocation);
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {
    }

    private void tickSoundOnPendingPlayers() {
        for (Player player : playerToPendingDeathWorkflowData.keySet()) {
            int currentRemainingSeconds = playerToCountdown.get(player) - 1;

            if (currentRemainingSeconds == 0) {
                playerToCountdown.remove(player);
                if (playerToCountdown.isEmpty()) {
                    Bukkit.getScheduler().cancelTask(tickSoundTask.getTaskId());
                }

                DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(player);
                playerToPendingDeathWorkflowData.remove(player);

                player.setGameMode(GameMode.SURVIVAL);
                PlayerClassMechanicsHelper.getSingleton().enablePlayerClass(player.getUniqueId());
                DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.END_OF_GHOST_MODE, deathWorkflowData);
                return;
            }

            playerToCountdown.put(player, currentRemainingSeconds);
            if (currentRemainingSeconds <= 5) {
                player.sendTitle(String.valueOf(currentRemainingSeconds), "",0, 20, 20);
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1);
            }
            else {
                player.playSound(player, Sound.BLOCK_WOOD_PLACE, SoundCategory.MASTER, 0.5f, 0.7f);
            }
        }
    }
}
