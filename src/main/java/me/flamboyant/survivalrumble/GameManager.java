package me.flamboyant.survivalrumble;

import me.flamboyant.survivalrumble.comebackmechanics.ComebackMechanicsManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;

public class GameManager {
    private static GameManager instance;
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    private SurvivalRumbleData data;

    private GameManager() {
        data = SurvivalRumbleData.getSingleton();
    }

    public int addScore(String teamName, int score, ScoreType scoreType) {
        int result = -1;

        // TODO : gérer le case score < 0 (?)
        switch (scoreType) {
            case REVERSIBLE:
                data.teamReversibleScores.put(teamName, score + data.teamReversibleScores.get(teamName));
                result = data.teamReversibleScores.get(teamName);
                break;
            case FLAT:
                data.teamFlatScores.put(teamName, score + data.teamFlatScores.get(teamName));
                result = data.teamFlatScores.get(teamName);
                break;
            case PERFECT:
                data.teamPerfectScores.put(teamName, score + data.teamPerfectScores.get(teamName));
                result = data.teamPerfectScores.get(teamName);
                break;
        }

        ScoreboardBricklayer.getSingleton().setTeamScore("Score", teamName, data.getTotalScore(teamName));
        checkScoresForRemontadaMechanics();

        return result;
    }

    public void setMorningTimeAtGameLaunch() {
        Common.server.getWorld("world").setTime(0);
    }

    private void checkScoresForRemontadaMechanics() {
        int lowerScore = 9999999;
        int higherScore = 0;
        String teamHigher = "";

        for (String teamName : data.teams) {
            int malus = data.teamMalus.get(teamName);
            int score = data.getTotalScore(teamName) - malus;
            if (score < lowerScore) {
                lowerScore = score;
            }
            if (score > higherScore) {
                higherScore = score;
                teamHigher = teamName;
            }
        }

        if (!teamHigher.equals(""))
            ComebackMechanicsManager.getInstance().onScoreGapCalculation(higherScore - lowerScore, teamHigher);
    }
}
