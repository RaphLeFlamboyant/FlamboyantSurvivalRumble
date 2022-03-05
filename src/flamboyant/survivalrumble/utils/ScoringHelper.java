package flamboyant.survivalrumble.utils;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.scoreboard.Objective;

public class ScoringHelper {
    public static int fullScoreMinY = 58;
    public static int fullScoreMaxY = 80;
    public static int zeroScoreYUnderground = 20;
    public static int zeroScoreYInSky = 200;

    public static double scoreAltitudeCoefficient(int blockLocationY) {
        if (blockLocationY >= fullScoreMinY && blockLocationY < fullScoreMaxY)
            return 1;
        if (blockLocationY < zeroScoreYUnderground || blockLocationY > zeroScoreYInSky)
            return 0;

        double baseY;
        double relative;

        if (blockLocationY > 64) {
            baseY = 136;
            relative = blockLocationY - 64;
        } else {
            baseY = 44;
            relative = 64 - blockLocationY;
        }

        return (5.0 - (relative / baseY * 4.0 + 1.0)) / 5.0;
    }

    // Returns new score
    public static int addScore(String teamName, int score, ScoreType scoreType) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        int result = -1;

        // TODO : gérer le case score < 0 (?)
        switch (scoreType)
        {
            case REVERSIBLE:
                data.teamReversibleScores.put(teamName, Math.max(0, score + data.teamReversibleScores.get(teamName)));
                result = data.teamReversibleScores.get(teamName);
                break;
            case FLAT:
                data.teamFlatScores.put(teamName, Math.max(0, score + data.teamFlatScores.get(teamName)));
                result = data.teamFlatScores.get(teamName);
                break;
            case PERFECT:
                data.teamPerfectScores.put(teamName, score + data.teamPerfectScores.get(teamName));
                result = data.teamPerfectScores.get(teamName);
                break;
        }

        data.saveData();
        ScoreboardBricklayer.getSingleton().setTeamScore("Score", teamName, data.getTotalScore(teamName));

        return result;
    }
}
