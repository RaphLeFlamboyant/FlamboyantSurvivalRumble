package me.flamboyant.survivalrumble.delegates;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface TryRunOnPlayerCallback {
    boolean tryRunOnPlayer(Player player);
}
