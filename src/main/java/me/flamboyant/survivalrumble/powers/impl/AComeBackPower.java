package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.delegates.EntityDamageEventCallback;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class AComeBackPower implements IChampionPower, EntityDamageEventCallback {
    protected boolean hasAlreadyBeenUsed;
    protected Player powerOwner;
    protected int powerLevel;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;
        this.powerLevel = powerLevel;

        hasAlreadyBeenUsed = false;

        AssaultManager.getInstance().addListener(this);

        onActivate();
    }

    @Override
    public void deactivate() {
        AssaultManager.getInstance().removeListener(this);
        onDeactivate();
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (hasAlreadyBeenUsed) return;
        if (event.getEntity() != powerOwner) return;
        if (powerOwner.getHealth() - event.getFinalDamage() > getHealthTrigger()) return;

        while (event.getFinalDamage() >= powerOwner.getHealth()) {
            event.setDamage(event.getDamage() - 1);
            if (event.getDamage() < 1) {
                Bukkit.getLogger().warning("DAMAGE REDUCTION NOT WORKING");
                break;
            }
        }
        onPowerTriggered();
        hasAlreadyBeenUsed = true;
    }

    protected void onActivate() {}
    protected void onDeactivate() {}

    protected abstract void onPowerTriggered();
    protected abstract int getHealthTrigger();
}
