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
        scheduleUpdateGameTimeTask();
    }

    private void scheduleUpdateGameTimeTask() {
        updateGameTimeTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> doOnScheduledTick(), 60 * 20L, 60 * 20L);
    }

    private void doOnScheduledTick() {
        data().minutesBeforeEnd--;

        if (data().minutesBeforeEnd == 0) {
            visitor.onAction();
            Bukkit.getScheduler().cancelTask(updateGameTimeTask.getTaskId());
        }
    }
}
