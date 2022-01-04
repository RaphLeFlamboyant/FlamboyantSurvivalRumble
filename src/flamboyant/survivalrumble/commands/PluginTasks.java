package flamboyant.survivalrumble.commands;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class PluginTasks {
    private static PluginTasks instance;
    private BukkitTask checkPlayersPositionToHQTask = null;
    private BukkitTask updateGameTimeTask = null;

    public static PluginTasks getSingleton() {
        if (instance == null)
            instance = new PluginTasks();

        return instance;
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void scheduleRandomMeetup(JavaPlugin plugin, Server server) {
        if (data().pvpIntensity == 0)
            return;

        int remaningTime = data().minutesBeforeEnd;
        int timePart = remaningTime / data().pvpIntensity;
        Random rng = new Random();

        for (int i = 0; i < data().pvpIntensity; i++) {
            int meetupTime = i * timePart + timePart * 1 / 20 + rng.nextInt(timePart * 9 / 10);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.broadcastMessage("§4Oh non ! Un Meetup sauvage est apparu ! ");
                World world = server.getWorld("world");
                Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
                OfflinePlayer[] players = server.getOfflinePlayers();

                for (OfflinePlayer offPlayer : players) {
                    Player player = offPlayer.getPlayer();
                    if (player == null)
                        continue;
                    player.teleport(zeroLocation);
                }
            }, meetupTime * 60 * 20L);

            data().meetupTimer.add(meetupTime);
        }

        data().saveData();
    }
}
