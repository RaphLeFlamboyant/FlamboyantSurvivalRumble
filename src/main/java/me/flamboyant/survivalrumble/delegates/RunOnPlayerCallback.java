package me.flamboyant.survivalrumble.delegates;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface RunOnPlayerCallback {
    void runOnPlayer(Player player);
}
