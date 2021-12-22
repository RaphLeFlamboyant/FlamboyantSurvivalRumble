package flamboyant.survivalrumble.utils;

public class ScoringHelper
{
    public static double scoreAltitudeCoefficient(int blockLocationY)
    {
        if (blockLocationY >= 58 && blockLocationY < 80)
            return 1;
        if (blockLocationY < 20 || blockLocationY > 200)
            return 0;

        double baseY;
        double relative;

        if (blockLocationY > 64) {
            baseY = 136;
            relative = blockLocationY - 64;
        }
        else
        {
            baseY = 44;
            relative = 64 - blockLocationY;
        }

        return (5.0 - (relative / baseY * 4.0 + 1.0)) / 5.0;
    }
}
