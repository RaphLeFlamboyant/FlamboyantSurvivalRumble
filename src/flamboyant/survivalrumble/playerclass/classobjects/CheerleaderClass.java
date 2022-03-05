package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CheerleaderClass extends APlayerClass {
    public CheerleaderClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CHEERLEADER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (!data().playersTeam.get(killer.getUniqueId()).equals(ownerTeamName)) return;
        if (data().playersTeam.get(killed.getUniqueId()).equals(ownerTeamName)) return;
        if (owner.getLocation().distance(killed.getLocation()) > 50) return;

        ScoringHelper.addScore(ownerTeamName, 125, ScoreType.FLAT);
    }
}
