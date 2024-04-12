package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.potion.PotionEffectType;

public class StrengthEffectPower extends AEffectPower {
    @Override
    protected PotionEffectType getPotionEffectType() {
        return PotionEffectType.INCREASE_DAMAGE;
    }
}
