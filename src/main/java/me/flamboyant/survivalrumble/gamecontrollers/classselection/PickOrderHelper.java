package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PickOrderHelper {
    public static List<Player> getPlayerPickOrder() {
        List<List<Player>> teamPlayerList = new ArrayList<>();
        List<Player> res = new ArrayList<>();

        for (String teamName : SurvivalRumbleData.getSingleton().getTeams()) {
            teamPlayerList.add(SurvivalRumbleData.getSingleton().getPlayers(teamName));
        }

        teamPlayerList.sort(Comparator.comparingInt(List::size));
        Collections.reverse(teamPlayerList);

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
