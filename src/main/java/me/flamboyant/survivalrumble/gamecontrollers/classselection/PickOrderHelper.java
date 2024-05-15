package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PickOrderHelper {
    public static List<Player> getPlayerPickOrder() {
        List<List<Player>> teamPlayerList = new ArrayList<>();
        List<Player> res = new ArrayList<>();

        int maxPlayerCountInTeam = 0;
        for (String teamName : SurvivalRumbleData.getSingleton().getTeams()) {
            var players = SurvivalRumbleData.getSingleton().getPlayers(teamName);
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

        int teamIndex = 0;
        int listIndex = 0;
        int delta = 1;
        do {
            if (teamPlayerList.get(teamIndex).size() > listIndex) {
                res.add(teamPlayerList.get(teamIndex).get(listIndex));
            }

            if (teamIndex + delta == teamPlayerList.size()
                    || teamIndex + delta == -1) {
                listIndex++;
                delta *= -1;
            }
            else
                teamIndex += delta;
        } while (teamPlayerList.get(teamPlayerList.size() - 1).size() > listIndex);

        return res;
    }
}
