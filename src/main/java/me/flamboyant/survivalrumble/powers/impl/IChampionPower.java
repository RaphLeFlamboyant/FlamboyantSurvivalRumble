package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.entity.Player;

public interface IChampionPower {
    void activate(Player powerOwner, int powerLevel);
    void deactivate();
}
