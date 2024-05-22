package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BoorishClass extends APlayerClass implements Listener {
    private static final int validDistance = 100;
    private static final int hitMoneyReward = 10;
    private static final int killMoneyReward = 50;

    public BoorishClass(Player owner) {
        super(owner);

        scoringDescription = "Frapper et tuer des ennemis proches de ta base ";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BOORISH;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() != owner) return;
        onOwnerAttackPlayer(event.getEntity(), hitMoneyReward);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != owner) return;
        onOwnerAttackPlayer(event.getEntity(), killMoneyReward);
    }

    private void onOwnerAttackPlayer(Entity hitPlayer, int moneyReward) {
        if (hitPlayer.getType() != EntityType.PLAYER) return;
        var ownerTeamName = data().getPlayerTeam(owner);
        if (data().getPlayerTeam((Player) hitPlayer) == ownerTeamName) return;

        var teamLocation = data().getHeadquarterLocation(ownerTeamName);
        var foeLocation = hitPlayer.getLocation();

        var isFoeInHQ = TeamHelper.isLocationInHeadQuarter(hitPlayer.getLocation(), ownerTeamName);
        var isFoeCloseToHQ = foeLocation.distance(teamLocation) <= validDistance;
        if (!isFoeCloseToHQ && !isFoeInHQ) return;

        GameManager.getInstance().addAddMoney(ownerTeamName, moneyReward);
    }
}
