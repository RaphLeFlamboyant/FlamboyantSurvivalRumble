package flamboyant.survivalrumble.playerclass.managers;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class GameTimeManager {
    private BukkitTask updateGameTimeTask = null;
    private JavaPlugin plugin;

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void launchGameTimeManagement(JavaPlugin plugin) {
        this.plugin = plugin;
        scheduleStopTask();
        scheduleUpdateGameTimeTask();
    }

    private void scheduleStopTask() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stopGame();
        }, data().minutesBeforeEnd * 60 * 20L);
    }

    private void scheduleUpdateGameTimeTask() {
        updateGameTimeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            updateGameTime();
        }, 60 * 20L, 60 * 20L);
    }

    private void updateGameTime() {
        data().minutesBeforeEnd--;

        for (int i = 0; i < data().meetupTimer.size(); i++) {
            int timer = data().meetupTimer.get(i) - 1;

            if (timer > 0)
                data().meetupTimer.set(i, timer);
            else
                data().meetupTimer.remove(i--);
        }

        data().saveData();
    }

    private void stopGame() {
        int highiestScore = -1;
        String winnerTeam = null;

        int iColor = 0;
        int iWinner = 0;
        for (String teamName : data().teams) {
            if (winnerTeam == null || data().getTotalScore(teamName) > highiestScore) {
                winnerTeam = teamName;
                highiestScore = data().getTotalScore(teamName);
                iWinner = iColor;
            }
            iColor++;
        }

        Bukkit.broadcastMessage("L'�quipe " + winnerTeam + " a gagn� !!");
        Bukkit.getScheduler().cancelTask(updateGameTimeTask.getTaskId());
    }
}
