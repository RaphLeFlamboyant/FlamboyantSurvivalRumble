package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public abstract class AEffectPower implements IChampionPower {
    private Player powerOwner;
    private BukkitTask potionEffectTask;
    private PotionEffect potionEffect;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        potionEffectTask = Bukkit.getScheduler().runTaskTimer(Common.plugin,() -> applyPotionEffect(), 0, 40);
        potionEffect = new PotionEffect(getPotionEffectType(), 100, powerLevel, false, false, true);
    }

    @Override
    public void deactivate() {
        Bukkit.getScheduler().cancelTask(potionEffectTask.getTaskId());
    }

    protected abstract PotionEffectType getPotionEffectType();

    private void applyPotionEffect() {
        powerOwner.addPotionEffect(potionEffect);
    }
}
