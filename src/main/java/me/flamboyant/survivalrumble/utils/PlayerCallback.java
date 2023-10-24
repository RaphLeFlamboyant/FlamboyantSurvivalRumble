package me.flamboyant.survivalrumble.utils;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerCallback {
    void runOnPlayer(Player player);
}
