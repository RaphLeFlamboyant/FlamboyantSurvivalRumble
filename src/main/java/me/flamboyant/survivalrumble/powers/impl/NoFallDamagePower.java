package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamagePower implements IChampionPower, Listener {
    private Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void deactivate() {
        EntityDamageEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() != powerOwner) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        event.setCancelled(true);
    }
}
