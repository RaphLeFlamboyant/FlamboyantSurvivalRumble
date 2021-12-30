package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TrapperClass extends APlayerClass {
    public TrapperClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);
    }

    @Override
    public PlayerClassType getClassType() { return PlayerClassType.TRAPPER; }

    @Override
    public void gameStarted(Server server, Plugin plugin) { }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killer != null) return;
        if (data().playersTeam.get(killed.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;
        if (owner.getLocation().distance(killed.getLocation()) > 100) return;

        changeScore(data().playersTeam.get(owner.getUniqueId()), 500);
    }
}
