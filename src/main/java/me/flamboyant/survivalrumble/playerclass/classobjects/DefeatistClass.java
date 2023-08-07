package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;

public class DefeatistClass extends APlayerClass {
    public DefeatistClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Mourir de la main d'un ennemi";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.DEFEATIST;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killed == owner && killer != null) {
            String teamName = data().getPlayerTeam(owner);
            if (!data().getPlayerTeam(killer).equals(teamName)) {
                GameManager.getInstance().addAddMoney(teamName, 50);
            }
        }
    }
}
