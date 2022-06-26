package me.flamboyant.survivalrumble.commands;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.listeners.MainGameListener;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.playerclass.factory.PlayerClassFactory;
import me.flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import me.flamboyant.survivalrumble.utils.TeamHelper;
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
            case "c_sr_load_data":
                return loadData(senderPlayer);
            case "c_sr_reset_server":
                return resetServerConfig(senderPlayer);
            case "c_sr_build_game_from_data":
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
            int teamScore = data().getTotalScore(teamName);
            scoreObj.getScore(teamName).setScore(teamScore);
        }

        OfflinePlayer[] players = server.getOfflinePlayers();
        PlayerClassFactory factory = new PlayerClassFactory();
        for (OfflinePlayer offlinePlayer : players) {
            Player player = offlinePlayer.getPlayer();
            senderPlayer.sendMessage("Prise en charge du joueur " + offlinePlayer.getName());
            if (data().playersTeam.containsKey(offlinePlayer.getUniqueId())) {
                String teamName = data().playersTeam.get(offlinePlayer.getUniqueId());
                senderPlayer.sendMessage("Ajout du joueur " + offlinePlayer.getName() + " dans la team " + teamName);
                Team team = scoreboardBricklayer.getTeam(teamName);
                team.addPlayer(offlinePlayer);
                APlayerClass playerClass = factory.generatePlayerClass(data().playersClass.get(offlinePlayer.getUniqueId()), player);
                PlayerClassMechanicsHelper.getSingleton().declarePlayerClass(player, playerClass);
                playerClass.enableClass();
            }
        }

        scheduleStopTask(plugin);
        scheduleUpdateGameTimeTask(plugin);

        launchGameListeners();

        return true;
    }

    private void launchGameListeners() {
        GameManager.getInstance().setMorningTimeAtGameLaunch();
        MainGameListener listener = new MainGameListener();
        server.getPluginManager().registerEvents(listener, plugin);
        listener.initListener();
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
            if (winnerTeam == null || data().getTotalScore(teamName) > highiestScore) {
                winnerTeam = teamName;
                highiestScore = data().getTotalScore(teamName);
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
    }
}
