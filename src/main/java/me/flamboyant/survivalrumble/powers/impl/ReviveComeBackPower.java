package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ReviveComeBackPower extends AComeBackPower {
    @Override
    protected void onPowerTriggered() {
        Bukkit.getLogger().info("[ReviveComeBackPower.onPowerTriggered]");

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> powerOwner.setHealth(powerOwner.getHealth() + 6), 1);
        powerOwner.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 10, 2));

        var players = SurvivalRumbleData.getSingleton().getAttackingPlayers(powerOwner);
        var rng = Common.rng.nextInt(players.size());
        players.get(rng).damage(0);
        players.get(rng).getInventory().clear();
    }

    @Override
    protected int getHealthTrigger() {
        return 0;
    }
}
