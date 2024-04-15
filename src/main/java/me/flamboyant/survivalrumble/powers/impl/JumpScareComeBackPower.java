package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.Location;

public class JumpScareComeBackPower extends AComeBackPower {
    @Override
    protected void onPowerTriggered() {
        var location = powerOwner.getLocation();
        powerOwner.teleport(new Location(location.getWorld(), location.getX(), location.getWorld().getHighestBlockYAt(location) + 2, location.getZ()));
    }

    @Override
    protected int getHealthTrigger() {
        return 4;
    }
}
