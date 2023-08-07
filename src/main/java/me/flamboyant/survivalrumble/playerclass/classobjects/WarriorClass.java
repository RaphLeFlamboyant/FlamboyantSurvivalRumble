package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;

public class WarriorClass extends APlayerClass {
    public WarriorClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Tue des adversaires";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WARRIOR;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killer == owner) {
            String teamName = data().getPlayerTeam(owner);
            if (!data().getPlayerTeam(killed).equals(teamName)) {
                GameManager.getInstance().addAddMoney(teamName, 250);
            }
        }
    }
}
