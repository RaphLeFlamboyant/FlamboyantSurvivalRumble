package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class WarriorClass extends APlayerClass implements Listener {
    public WarriorClass(Player owner) {
        super(owner);

        scoringDescription = "Tue des adversaires";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WARRIOR;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var killed = event.getEntity();
        var killer = event.getEntity().getKiller();

        if (killer == owner) {
            String teamName = data().getPlayerTeam(owner);
            if (!data().getPlayerTeam(killed).equals(teamName)) {
                GameManager.getInstance().addAddMoney(teamName, 250);
            }
        }
    }
}
