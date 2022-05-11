package me.flamboyant.survivalrumble.comebackmechanics;

import me.flamboyant.survivalrumble.comebackmechanics.impl.CaptureTheFlagMechanics;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ComebackMechanicsManager implements IComebackMechanicsOwner {
    private int threshold = 20000;
    private List<IComebackMechanics> mechanicsList = new ArrayList<>();
    private HashMap<String, Boolean> activatedByTeam = new HashMap<>();

    private static ComebackMechanicsManager instance;
    public static ComebackMechanicsManager getInstance() {
        if (instance == null) {
            instance = new ComebackMechanicsManager();
        }

        return instance;
    }

    private ComebackMechanicsManager() {
        mechanicsList.add(new CaptureTheFlagMechanics(this));
        for (String teamName : SurvivalRumbleData.getSingleton().teams) {
            System.out.println("Storing team " + teamName + " for comeback mechanics");
            activatedByTeam.put(teamName, false);
        }
    }

    // TODO : en fait avec des triggers c'est tjr le plus petit qui va sortir
    public void onScoreGapCalculation(int scoreGap, String bestTeamName) {
        if (activatedByTeam.get(bestTeamName)) return;

        List<IComebackMechanics> possibleMechanics = new ArrayList<>();
        for (IComebackMechanics mechanics : mechanicsList) {
            if (scoreGap >= threshold) {
                possibleMechanics.add(mechanics);
            }
        }

        if (possibleMechanics.size() > 0) {
            System.out.println("Got " + possibleMechanics.size());
            Random rng = Common.rng;
            IComebackMechanics chosenMechanics = possibleMechanics.get(rng.nextInt(possibleMechanics.size()));
            chosenMechanics.activate(bestTeamName);
            activatedByTeam.put(bestTeamName, true);
        }
    }

    @Override
    public void onMechanicsEnd(String teamName) {
        System.out.println("Mechanic ends for team " + teamName);
        activatedByTeam.put(teamName, false);
    }
}
