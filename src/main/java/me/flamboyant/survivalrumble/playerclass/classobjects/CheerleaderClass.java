package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;

public class CheerleaderClass extends APlayerClass {
    public CheerleaderClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "étre é moins de 50 blocs d'un allié qui fait un kill";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CHEERLEADER;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (!data().playersTeam.get(killer.getUniqueId()).equals(ownerTeamName)) return;
        if (data().playersTeam.get(killed.getUniqueId()).equals(ownerTeamName)) return;
        if (owner.getLocation().getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 50) return;

        GameManager.getInstance().addScore(ownerTeamName, 125, ScoreType.FLAT);
    }
}
