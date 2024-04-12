package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.potion.PotionEffectType;

public class ResistanceEffectPower extends AEffectPower {
    @Override
    protected PotionEffectType getPotionEffectType() {
        return PotionEffectType.DAMAGE_RESISTANCE;
    }
}
