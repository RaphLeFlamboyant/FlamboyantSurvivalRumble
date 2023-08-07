package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class GameTimeManager {
    private BukkitTask updateGameTimeTask = null;
    private ITriggerVisitor visitor;

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void launchGameTimeManagement(ITriggerVisitor visitor) {
        this.visitor = visitor;
        scheduleStopTask();
        scheduleUpdateGameTimeTask();
    }

    private void scheduleStopTask() {
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            doOnEndOfTimer();
        }, data().minutesBeforeEnd * 60 * 20L);
    }

    private void scheduleUpdateGameTimeTask() {
        updateGameTimeTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> {
            updateGameTime();
        }, 60 * 20L, 60 * 20L);
    }

    private void updateGameTime() {
        data().minutesBeforeEnd--;
    }

    private void doOnEndOfTimer() {
        visitor.onAction();
        Bukkit.getScheduler().cancelTask(updateGameTimeTask.getTaskId());
    }
}
