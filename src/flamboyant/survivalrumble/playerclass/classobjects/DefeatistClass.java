package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DefeatistClass extends APlayerClass {
    public DefeatistClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.DEFEATIST;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killed == owner) {
            String teamName = data().playersTeam.get(owner.getUniqueId());
            if (!data().playersTeam.get(killer.getUniqueId()).equals(teamName)) {
                ScoringHelper.addScore(teamName, 50, ScoreType.FLAT);
            }
        }
    }
}
