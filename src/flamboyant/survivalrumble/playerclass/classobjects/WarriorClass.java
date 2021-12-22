package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WarriorClass extends APlayerClass
{
    public WarriorClass(Player owner)
    {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);
    }

    @Override
    public PlayerClassType getClassType() { return PlayerClassType.WARRIOR; }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer)
    {
        if (killer == owner)
        {
            String teamName = data().playersTeam.get(owner.getUniqueId());
            if (!data().playersTeam.get(killed.getUniqueId()).equals(teamName))
            {
                changeScore(teamName, 300);
            }
        }
    }
}
