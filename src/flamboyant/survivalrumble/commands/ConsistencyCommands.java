package flamboyant.survivalrumble.commands;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ConsistencyCommands implements CommandExecutor {
    private JavaPlugin plugin;
    private Server server;
    private ScoreboardBricklayer scoreboardBricklayer;
    private ChatColor[] teamColors = {ChatColor.BLUE, ChatColor.RED, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.BLACK, ChatColor.LIGHT_PURPLE};
    private BukkitTask checkPlayersPositionToHQTask = null;
    private BukkitTask updateGameTimeTask = null;

    public ConsistencyCommands(JavaPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        this.scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        Player senderPlayer = (Player) sender;

        switch (cmd.getName()) {
            case "f_sr_maintenance_load_data":
                return loadData(senderPlayer);
            case "f_sr_maintenance_reset_server_config":
                return resetServerConfig(senderPlayer);
            case "f_sr_maintenance_make_server_config":
                return makeServerConfig(senderPlayer);
            default:
                break;
        }

        return false;
    }

    private Boolean loadData(Player senderPlayer) {
        senderPlayer.sendMessage("Sauvegarde chargée");
        SurvivalRumbleData.loadData();
        return true;
    }

    private Boolean resetServerConfig(Player senderPlayer) {
        scoreboardBricklayer.deleteAllObjectives();
        senderPlayer.sendMessage("Objectifs supprimés");
        System.out.println("Objectifs supprimés");
        scoreboardBricklayer.deleteAllTeams();
        senderPlayer.sendMessage("Equipes supprimées");
        System.out.println("Equipes supprimées");

        return true;
    }

    private Boolean makeServerConfig(Player senderPlayer) {
        ArrayList<String> teams = data().teams;
        Objective scoreObj = scoreboardBricklayer.createObjective("Score", "Score", DisplaySlot.SIDEBAR);
        senderPlayer.sendMessage("Création des scores");

        int iColor = 0;
        for (String teamName : teams) {
            senderPlayer.sendMessage("Ajout de la team " + teamName);
            Team team = scoreboardBricklayer.addNewTeam(teamName);
            team.setColor(teamColors[iColor++]);
            int teamScore = data().teamScores.get(teamName);
            scoreObj.getScore(teamName).setScore(teamScore);
        }

        OfflinePlayer[] players = server.getOfflinePlayers();

        for (OfflinePlayer player : players) {
            senderPlayer.sendMessage("Prise en charge du joueur " + player.getName());
            if (data().playersTeam.containsKey(player.getUniqueId())) {
                String teamName = data().playersTeam.get(player.getUniqueId());
                senderPlayer.sendMessage("Ajout du joueur " + player.getName() + " dans la team " + teamName);
                Team team = scoreboardBricklayer.getTeam(teamName);
                team.addPlayer(player);
            }
        }

        World world = senderPlayer.getWorld();
        Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
        scheduleStopTask(plugin);
        scheduleUpdateGameTimeTask(plugin);
		/*if (data().isPlayerInHQ.values().contains(false))
			scheduleCheckPlayersPositionToHQTask(zeroLocation, plugin, server);
		else
			scheduleRandomMeetup(plugin, server);*/

        return true;
    }

    public void scheduleCheckPlayersPositionToHQTask(Location zeroLocation, JavaPlugin plugin, Server server) {
        checkPlayersPositionToHQTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkPlayersPositionToHQ(zeroLocation, plugin, server);
        }, 0, 5L);
    }

    public void scheduleStopTask(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stopGame();
        }, data().minutesBeforeEnd * 60 * 20L);
    }

    public void scheduleUpdateGameTimeTask(JavaPlugin plugin) {
        updateGameTimeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            updateGameTime();
        }, 60 * 20L, 60 * 20L);
    }

    private void stopGame() {
        int highiestScore = -1;
        String winnerTeam = null;

        for (String teamName : data().teams) {
            if (winnerTeam == null || data().teamScores.get(teamName) > highiestScore) {
                winnerTeam = teamName;
                highiestScore = data().teamScores.get(teamName);
            }
        }

        Bukkit.broadcastMessage(TeamHelper.getTeamColor(winnerTeam) + "L'équipe " + winnerTeam + " a gagné !!");
        Bukkit.getScheduler().cancelTask(updateGameTimeTask.getTaskId());
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

    private void checkPlayersPositionToHQ(Location zeroLocation, JavaPlugin plugin, Server server) {
        ArrayList<String> teamNames = data().teams;

        Boolean allTeamReached = true;

        for (String teamName : teamNames) {
            Team team = ScoreboardBricklayer.getSingleton().getTeam(teamName);
			/*if (!data().isRunToHeadquarterPhase.get(teamName))
				continue;
			*/
            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            double totalDistance = hqLocation.distance(zeroLocation);
            double percentage = 0;
            Set<OfflinePlayer> players = team.getPlayers();
            int playersInBase = 0;
			/*
			for(OfflinePlayer offlinePlayer : players)
			{
				Player player = offlinePlayer.getPlayer();
				if (player == null)
					continue;
				
				double distance = hqLocation.distance(player.getLocation());

				if (distance < 50 && !data().isPlayerInHQ.get(player.getUniqueId()))
				{
					data().isPlayerInHQ.put(player.getUniqueId(), true);
					player.sendMessage("§5Tu es arrivé aux coordonnées de la base de ton équipe !");	
					percentage += 0;
				}
				else if (distance >= 50)
				{
					if (!data().isPlayerInHQ.containsKey(player.getUniqueId()))
					{
						data().isPlayerInHQ.put(player.getUniqueId(), false);
					}
					else if (data().isPlayerInHQ.get(player.getUniqueId()))
					{
						player.sendMessage("§4Attention, tu t'éloignes des coordonnées de la base de ton équipe !");
						data().isPlayerInHQ.put(player.getUniqueId(), false);
					}
					
					percentage += distance / totalDistance;
				}
				
				if (data().isPlayerInHQ.get(player.getUniqueId()))
					playersInBase++;
			}

			if (playersInBase == players.size())
			{
				data().isRunToHeadquarterPhase.put(team.getName(), false);
				Bukkit.broadcastMessage("§3" + team.getName() + " team reached the headquarter !");
			}
			else
			{
				allTeamReached = false;
			}
			*/

            data().saveData();
        }

        if (allTeamReached) {
            Bukkit.getScheduler().cancelTask(checkPlayersPositionToHQTask.getTaskId());
            scheduleRandomMeetup(plugin, server);
        }
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
