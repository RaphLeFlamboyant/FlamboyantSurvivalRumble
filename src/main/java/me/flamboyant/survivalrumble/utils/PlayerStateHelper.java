package me.flamboyant.survivalrumble.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerStateHelper {
    public static void resetPlayerState(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(3);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
