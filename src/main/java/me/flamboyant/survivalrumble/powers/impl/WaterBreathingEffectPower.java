package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.potion.PotionEffectType;

public class WaterBreathingEffectPower extends AEffectPower {
    @Override
    protected PotionEffectType getPotionEffectType() {
        return PotionEffectType.WATER_BREATHING;
    }
}
