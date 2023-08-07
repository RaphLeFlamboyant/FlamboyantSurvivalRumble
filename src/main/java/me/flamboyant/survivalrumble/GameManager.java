package me.flamboyant.survivalrumble;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import me.flamboyant.utils.Common;

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

    public int addAddMoney(String teamName, int deltaMoney) {
        data.addMoney(teamName, deltaMoney);
        int totalMoney = data.getMoney(teamName);
        ScoreboardBricklayer.getSingleton().setTeamScore("Score", teamName, totalMoney);
        return totalMoney;
    }

    public void setMorningTimeAtGameLaunch() {
        Common.server.getWorld("world").setTime(0);
    }
}
