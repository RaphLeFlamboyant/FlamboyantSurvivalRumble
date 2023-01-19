package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class ProximityToPlayersQuestTask extends AQuestTask {
    private List<Player> playerToCheckLocations;
    private int distance;
    private int durationInSeconds;
    private boolean revert;
    private BukkitTask checkProximityTask = null;

    public ProximityToPlayersQuestTask(Quest ownerQuest, List<Player> playerToCheckLocations, int distance, int durationInSeconds, boolean revert) {
        super(ownerQuest);

        this.playerToCheckLocations = playerToCheckLocations;
        this.distance = distance;
        this.durationInSeconds = durationInSeconds;
        this.revert = revert;

        subQuestMessage = "Tu dois te tenir é " + (revert ? "plus" : "moins") + " de " + distance + " blocs de distance des joueurs suivants :";
        for (Player target : playerToCheckLocations) {
            subQuestMessage += "\n    -> " + target.getDisplayName();
        }
    }


    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        checkProximityTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> checkPlayersProximity(), 20L, 20L);
    }

    @Override
    protected void stopQuest(boolean success) {
        super.stopQuest(success);
        Bukkit.getScheduler().cancelTask(checkProximityTask.getTaskId());
    }

    private void checkPlayersProximity() {
        for (Player target : playerToCheckLocations) {
            if (target.getWorld() != player.getWorld()
                    || !checkDistance(target.getLocation().distance(player.getLocation()))) return;
        }

        if (--durationInSeconds <= 0) stopQuest(true);
    }

    private boolean checkDistance(double currentDistance) {
        return (revert && currentDistance > distance) || (!revert && currentDistance < distance);
    }
}
