package me.flamboyant.survivalrumble.shop;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.entity.Player;

public class TeamMoneyManager implements IMoneyManager {
    private TeamMoneyManager() {}

    private static TeamMoneyManager instance;
    public static TeamMoneyManager getInstance() {
        if (instance == null) {
            instance = new TeamMoneyManager();
        }

        return instance;
    }

    public boolean trySpendMoney(Player player, int amount) {
        SurvivalRumbleData data = data();
        String teamName = data.getPlayerTeam(player);
        if (data.getMoney(teamName) >= amount) {
            GameManager.getInstance().addAddMoney(teamName, -amount);
            return true;
        }

        return false;
    }

    public void earnMoney(Player player, int amount) {
        SurvivalRumbleData data = data();
        String teamName = data.getPlayerTeam(player);

        data.addMoney(teamName, amount);
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }
}
