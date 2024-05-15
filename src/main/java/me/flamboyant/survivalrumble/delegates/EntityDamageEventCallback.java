package me.flamboyant.survivalrumble.delegates;

import org.bukkit.event.entity.EntityDamageEvent;

@FunctionalInterface
public interface EntityDamageEventCallback {
    void onEntityDamageEvent(EntityDamageEvent event);
}
