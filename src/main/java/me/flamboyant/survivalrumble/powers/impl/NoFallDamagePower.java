package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.delegates.EntityDamageEventCallback;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.survivalrumble.utils.Priority;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamagePower implements IChampionPower, EntityDamageEventCallback {
    private Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        AssaultManager.getInstance().addEntityDamageListener(this, Priority.HIGH);
    }

    @Override
    public void deactivate() {
        AssaultManager.getInstance().removeEntityDamageListener(this);
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() != powerOwner) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        event.setCancelled(true);
    }
}
