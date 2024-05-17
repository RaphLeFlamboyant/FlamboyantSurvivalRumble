package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PickOrderHelper {
    public static List<Player> getPlayerPickOrder() {
        List<List<Player>> teamPlayerList = new ArrayList<>();
        List<Player> res = new ArrayList<>();

        Bukkit.getLogger().info("PickupOrder");
        int maxPlayerCountInTeam = 0;
        for (String teamName : SurvivalRumbleData.getSingleton().getTeams()) {
            var players = SurvivalRumbleData.getSingleton().getPlayers(teamName);
            Collections.shuffle(players);
            Bukkit.getLogger().info("Team " + teamName + " size = " + players.size());
            teamPlayerList.add(players);

            if (maxPlayerCountInTeam < players.size())
                maxPlayerCountInTeam = players.size();
        }

        List<List<Player>> halfRandomizedTeamPlayerList = new ArrayList<>();
        Collections.shuffle(teamPlayerList);
        for (var i = maxPlayerCountInTeam; i > 0; i--) {
            for (var teamList : teamPlayerList) {
                if (teamList.size() == i) {
                    halfRandomizedTeamPlayerList.add(teamList);
                }
            }
        }
        teamPlayerList = halfRandomizedTeamPlayerList;
        Bukkit.getLogger().info("Randomized list of list size : " + teamPlayerList.size());

        for (int i = 0; i < teamPlayerList.get(0).size(); i++) {
            for (int j = 0; j < teamPlayerList.size(); j++) {
                if (teamPlayerList.get(j).size() > i) {
                    res.add(teamPlayerList.get(j).get(i));
                }
            }
        }

        Bukkit.getLogger().info("res size : " + res.size());
        return res;
    }
}
