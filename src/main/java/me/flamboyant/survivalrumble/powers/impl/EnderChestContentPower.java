package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.entity.Player;

public class EnderChestContentPower implements IChampionPower {
    @Override
    public void activate(Player powerOwner, int powerLevel) {
        var enderChestInventory = powerOwner.getEnderChest();
        powerOwner.getInventory().addItem(enderChestInventory.getContents());
        enderChestInventory.clear();
    }

    @Override
    public void deactivate() {

    }
}
