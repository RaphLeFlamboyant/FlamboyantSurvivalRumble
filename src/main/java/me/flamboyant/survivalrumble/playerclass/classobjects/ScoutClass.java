package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ScoutClass extends AAttackClass {
    private static final float scoreRatioInMalus = 0.1f;
    private static final int minDistDefault = 101;
    private int checkInterval = 1;
    private HashMap<Integer, Float> scoreBySeconds = new HashMap<>() {{
        put(4, 20f);
        put(10, 5f);
        put(20, 2.5f);
        put(30, 1.7f);
        put(50, 1f);
        put(60, 0.6f);
        put(80, 0.4f);
        put(100, 0.2f);
    }};
    private List<Integer> orderedDistance;

    public ScoutClass(Player owner) {
        super(owner);

        scoringDescription = "Approche toi autant que possible du centre de la base adverse";
        orderedDistance = scoreBySeconds.keySet().stream().sorted().collect(Collectors.toList());
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

    @Override
    protected float getMalusRatio() {
        return scoreRatioInMalus;
    }

    @Override
    protected double getValidationDistance() {
        return minDistDefault;
    }

    private void updateScoring() {
        if (!owner.getWorld().getName().equals("world")) return;
        String closestTeamHQ = getClosestValidHeadQuarter();
        if (closestTeamHQ == null) return;

        var amount = getFlatAmount((int) owner.getLocation().distance(data().getHeadquarterLocation(closestTeamHQ)));
        applyAmount(amount);
    }

    private float getFlatAmount(int distToHqCenter) {
        float score = 0f;
        for (Integer dist : orderedDistance) {
            if (distToHqCenter <= dist) {
                score = scoreBySeconds.get(dist);
                break;
            }
        }

        return score;
    }
}
