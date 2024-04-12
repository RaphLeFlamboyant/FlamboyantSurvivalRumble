package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class FriendlyMobsPower implements IChampionPower, Listener {
    private Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;
    }

    @Override
    public void deactivate() {

    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() != powerOwner) return;

        event.setCancelled(true);
    }
}
