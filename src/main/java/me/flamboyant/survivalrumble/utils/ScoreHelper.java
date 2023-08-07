package me.flamboyant.survivalrumble.utils;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;

// TODO : virer et remplacer par une dist du centre de la base
public class ScoreHelper {
    public static int fullScoreMinY = 58;
    public static int fullScoreMaxY = 80;
    public static int zeroScoreYUnderground = 20;
    public static int zeroScoreYInSky = 200;

    public static void addScore(String team, int delta) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        data.addMoney(team, delta);
        ScoreboardBricklayer.getSingleton().setTeamScore("Score", team, data.getMoney(team));
    }

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
}
