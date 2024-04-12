package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.potion.PotionEffectType;

public class FireResistanceEffectPower extends AEffectPower {
    @Override
    protected PotionEffectType getPotionEffectType() {
        return PotionEffectType.FIRE_RESISTANCE;
    }
}
