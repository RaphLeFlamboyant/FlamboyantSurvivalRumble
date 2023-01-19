package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoutClass extends APlayerClass {
    private static final float scoreRatioInMalus = 0.05f;
    private static final int minDistDefault = 101;
    private String ownerTeam;
    private int checkInterval = 1;
    private float leftovers = 0f;
    private HashMap<Integer, Float> scoreBySeconds = new HashMap<Integer, Float>() {{
        put(4, 20f);
        put(10, 5f);
        put(20, 2.5f);
        put(30, 1.7f);
        put(50, 1f);
        put(60, 0.6f);
        put(80, 0.4f);
        put(100, 0.2f);
    }};

    public ScoutClass(Player owner) {
        super(owner);

        ownerTeam = data().playersTeam.get(owner.getUniqueId());
        scoringDescription = "Approche toi autant que possible du centre de la base adverse";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.SCOUT;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    private void updateScoring() {
        if (!owner.getWorld().getName().equals("world")) return;
        String closestTeamHQ = getCloserValidHeadQuarter();
        if (closestTeamHQ == null) return;

        Boolean isAnyFoeClose = false;
        for (UUID playerId : data().playersByTeam.get(closestTeamHQ)) {
            Player player = Common.server.getPlayer(playerId);
            isAnyFoeClose |= ((player.getWorld() == owner.getWorld()) && player.getLocation().distance(owner.getLocation()) < minDistDefault);
        }

        GameManager.getInstance().addScore(ownerTeam, getScoring((int) owner.getLocation().distance(data().teamHeadquarterLocation.get(closestTeamHQ)), !isAnyFoeClose), ScoreType.FLAT);
    }

    private int getScoring(int distToHqCenter, boolean isMalusApplied) {
        float score = 0f;
        for (Integer dist : scoreBySeconds.keySet()) {
            if (distToHqCenter <= dist) {
                score = scoreBySeconds.get(dist);
                break;
            }
        }

        if (isMalusApplied) score *= scoreRatioInMalus;
        score += leftovers;
        leftovers = score % 1f;

        return (int) score;
    }

    private String getCloserValidHeadQuarter() {
        double minDist = minDistDefault;
        String res = null;
        for (String teamName : data().teamHeadquarterLocation.keySet()) {
            if (ownerTeam.equals(teamName)) continue;

            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            double dist = hqLocation.distance(owner.getLocation());
            if (dist < minDist) {
                minDist = dist;
                res = teamName;
            }
        }

        return res;
    }
}
