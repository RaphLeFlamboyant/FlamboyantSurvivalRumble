package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CheerleaderClass extends APlayerClass implements Listener {
    public CheerleaderClass(Player owner) {
        super(owner);

        scoringDescription = "étre à moins de 50 blocs d'un allié qui fait un kill";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CHEERLEADER;
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

        String ownerTeam = data().getPlayerTeam(owner);
        if (!data().getPlayerTeam(killer).equals(ownerTeam)) return;
        if (data().getPlayerTeam(killed).equals(ownerTeam)) return;
        if (owner.getLocation().getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 50) return;

        GameManager.getInstance().addAddMoney(ownerTeam, 125);
    }
}
