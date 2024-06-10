package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

        switch (data().minutesBeforeEnd)
        {
            case 0:
                visitor.onAction();
                Bukkit.getScheduler().cancelTask(updateGameTimeTask.getTaskId());
                break;
            case 30, 10, 5:
                Bukkit.broadcastMessage(ChatHelper.importantMessage("Plus que " + data().minutesBeforeEnd + " minutes avant la fin !"));
                break;
            case 1:
                for (var player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle("1mn restante", "", 2, 56, 2);
                    Bukkit.broadcastMessage(ChatHelper.importantMessage("1mn restante"));
                }
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> doCountdown(59), 20);
                break;
        }
    }

    private void doCountdown(int seconds) {
        var nextCount = seconds - 1;
        if (nextCount == 0)
            return;

        for (var player : Bukkit.getOnlinePlayers()) {
            if (seconds > 10) {
                player.playSound(player, Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1f, 1);
            }
            else {
                player.sendTitle("" + seconds, "", 0, 19, 0);
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1);
            }
        }

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> doCountdown(nextCount), 20);
    }
}
