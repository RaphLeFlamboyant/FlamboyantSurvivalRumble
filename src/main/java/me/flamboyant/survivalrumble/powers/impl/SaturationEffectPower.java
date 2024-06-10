package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.potion.PotionEffectType;

public class SaturationEffectPower extends AEffectPower {
    @Override
    protected PotionEffectType getPotionEffectType() {
        return PotionEffectType.SATURATION;
    }
}
