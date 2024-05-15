package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.delegates.EntityDamageEventCallback;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamagePower implements IChampionPower, EntityDamageEventCallback {
    private Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        AssaultManager.getInstance().addListener(this);
    }

    @Override
    public void deactivate() {
        AssaultManager.getInstance().removeListener(this);
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() != powerOwner) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        event.setCancelled(true);
    }
}
