package me.flamboyant.survivalrumble.commands;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ScoreType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class DebugCommands implements CommandExecutor {
    private Server server;

    public DebugCommands(JavaPlugin plugin, Server server) {
        this.server = server;
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("f_add_score")) {
            addScore(args[0], Integer.parseInt(args[1]));
        }
        if (cmd.getName().equalsIgnoreCase("f_snapshot")) {
            List<String> arguments = Arrays.asList(args);
            List<OfflinePlayer> offlinePlayers = Arrays.asList(server.getOfflinePlayers());
            List<String> offlinePlayerNames = offlinePlayers.stream().filter(p -> p.getPlayer() != null).map(p -> p.getName()).collect(Collectors.toList());

            Bukkit.broadcastMessage("é4[DEBUG - SERVER SNAPSHOT]");
            // Players snapshot
            Bukkit.broadcastMessage("é3## Offline Players ##");

            for (OfflinePlayer player : offlinePlayers) {
                Bukkit.broadcastMessage("é6  - " + player.getName() + " [" + player.getUniqueId() + "] - IsOnlinePlayer :" + (player.getPlayer() != null));
            }
            Bukkit.broadcastMessage(" ");

            // Scoreboard snapshot
            Bukkit.broadcastMessage("é3## Scoreboard ##");
            ScoreboardManager scoreBoardManager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = scoreBoardManager.getMainScoreboard();
            Set<Team> teams = scoreboard.getTeams();
            List<String> teamNames = teams.stream().map(t -> t.getName()).collect(Collectors.toList());
            Bukkit.broadcastMessage("é6  - " + teams.size() + " teams :");
            for (Team team : teams) {
                Bukkit.broadcastMessage("    > " + team.getName() + " with " + team.getSize() + " players");
                for (String player : team.getEntries()) {
                    Bukkit.broadcastMessage("      > " + player);
                }
            }
            Bukkit.broadcastMessage(" ");

            Set<String> entries = scoreboard.getEntries();
            Bukkit.broadcastMessage("é6  - " + entries.size() + " entries :");
            for (String entry : entries) {
                Bukkit.broadcastMessage("    > " + entry);
            }
            Bukkit.broadcastMessage(" ");

            Set<Objective> objectives = scoreboard.getObjectives();
            Bukkit.broadcastMessage("é6  - " + objectives.size() + " objectives :");
            for (Objective objective : objectives) {
                Bukkit.broadcastMessage("    > " + objective.getName());
            }
            Bukkit.broadcastMessage(" ");

            Bukkit.broadcastMessage("é6  - Scores :");
            for (String entry : entries) {
                Set<Score> scores = scoreboard.getScores(entry);
                Bukkit.broadcastMessage("    > For entry " + entry + " with " + scores.size() + " scores");
                for (Score score : scores) {
                    Bukkit.broadcastMessage("      > " + score.getObjective().getName() + " : " + score.getScore());
                }
            }
            Bukkit.broadcastMessage(" ");

            // Plugin data
            if (arguments.contains("data")) {
                Bukkit.broadcastMessage("é3## Plugin Data ##");
                Boolean consistency = true;
                List<String> consistencyFailedReasons = new ArrayList<String>();
                Bukkit.broadcastMessage("é6  - isTeamSetupMode : ");// + data().isTeamSetupMode);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("é6  - teamHeadquarterLocation : " + data().teamHeadquarterLocation.size());
                for (String key : data().teamHeadquarterLocation.keySet()) {
                    Bukkit.broadcastMessage("    > " + key + " - " + data().teamHeadquarterLocation.get(key));
                    consistency &= MsgOnError(teamNames.contains(key), "teamNames doesn't contains " + key + " that is in teamHeadquarterLocation", consistencyFailedReasons);
                }
                consistency &= MsgOnError(teamNames.size() == data().teamHeadquarterLocation.size(), "teamNames and teamHeadquarterLocation not the same size", consistencyFailedReasons);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("é6  - playersClass : " + data().playersClass.size());
                for (UUID key : data().playersClass.keySet()) {
                    Bukkit.broadcastMessage("    > " + key + " - " + data().playersClass.get(key));
                    consistency &= MsgOnError(offlinePlayers.stream().anyMatch(p -> p.getUniqueId() == key),
                            "offlinePlayers doesn't contains " + key + " that is in playersClass", consistencyFailedReasons);
                }
                consistency &= MsgOnError(offlinePlayerNames.size() == data().playersClass.size(), "offlinePlayerNames and playersClass not the same size", consistencyFailedReasons);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("é6  - playersTeam : " + data().playersTeam.size());
                for (UUID key : data().playersTeam.keySet()) {
                    Bukkit.broadcastMessage("    > " + key + " - " + data().playersTeam.get(key));
                    consistency &= MsgOnError(offlinePlayers.stream().anyMatch(p -> p.getUniqueId() == key),
                            "offlinePlayers doesn't contains " + key + " that is in playersTeam", consistencyFailedReasons);
                }
                consistency &= MsgOnError(offlinePlayerNames.size() == data().playersTeam.size(), "offlinePlayerNames and playersTeam not the same size", consistencyFailedReasons);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("é6  - teamScores : " + data().teamPerfectScores.size());
                for (String key : data().teamPerfectScores.keySet()) {
                    Bukkit.broadcastMessage("    > " + key + " - " + data().getTotalScore(key));
                    consistency &= MsgOnError(teamNames.contains(key), "teamNames doesn't contains " + key + " that is in teamScores", consistencyFailedReasons);
                }
                consistency &= MsgOnError(teamNames.size() == data().teamPerfectScores.size(), "teamNames and teamScores not the same size", consistencyFailedReasons);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("é6  - minutesBeforeEnd : " + data().minutesBeforeEnd);
                Bukkit.broadcastMessage("é6  - meetupNumber : " + data().pvpIntensity);

                List<Integer> meetupTimer = new ArrayList<Integer>();
                Bukkit.broadcastMessage("é6  - meetupTimer : " + data().meetupTimer.size());
                for (Integer timer : data().meetupTimer) {
                    Bukkit.broadcastMessage("    > " + (timer / 60) + "h" + (timer % 60) + "m");
                }

                Bukkit.broadcastMessage(" ");
                if (consistency)
                    Bukkit.broadcastMessage("é7  Consistency OK");
                else {
                    Bukkit.broadcastMessage("é7  Consistency NOK");
                    for (String reason : consistencyFailedReasons) {
                        Bukkit.broadcastMessage("é7    > " + reason);
                    }
                }
            }

            Bukkit.broadcastMessage("é4[END OF DEBUG - SERVER SNAPSHOT]");

            return true;
        }

        return false;
    }

    private void addScore(String teamName, int scoreToAdd) {
        GameManager.getInstance().addScore(teamName, scoreToAdd, ScoreType.FLAT);
    }

    private Boolean MsgOnError(Boolean consistencyOk, String errorMsg, List<String> consistencyFailedReasons) {
        if (!consistencyOk) {
            consistencyFailedReasons.add(errorMsg);
        }

        return consistencyOk;
    }
}
