package flamboyant.survivalrumble.commands;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;

public class PluginTasks {
	private BukkitTask checkPlayersPositionToHQTask = null;
	private BukkitTask updateGameTimeTask = null;
	
	private SurvivalRumbleData data()
	{
		return SurvivalRumbleData.getSingleton();
	}
	
	private static PluginTasks instance;
	public static PluginTasks getSingleton()
	{
		if (instance == null)
			instance = new PluginTasks();
		
		return instance;		
	}
	
	public void scheduleRandomMeetup(JavaPlugin plugin, Server server)
	{
		if (data().pvpIntensity == 0)
			return;
		
		int remaningTime = data().minutesBeforeEnd;
		int timePart = remaningTime / data().pvpIntensity;
		Random rng = new Random();
		
		for (int i = 0; i < data().pvpIntensity; i++)
		{
			int meetupTime = i * timePart + timePart * 1 / 20 + rng.nextInt(timePart * 9 / 10);

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				Bukkit.broadcastMessage("§4Oh non ! Un Meetup sauvage est apparu ! ");
				World world = server.getWorld("world");
				Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
				OfflinePlayer[] players = server.getOfflinePlayers();
				
				for (OfflinePlayer offPlayer : players)
				{
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
