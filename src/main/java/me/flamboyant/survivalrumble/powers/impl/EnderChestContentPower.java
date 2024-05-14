package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.entity.Player;

public class EnderChestContentPower implements IChampionPower {
    @Override
    public void activate(Player powerOwner, int powerLevel) {
        var enderChestInventory = powerOwner.getEnderChest();
        var content = enderChestInventory.getContents();
        if (content.length > 0)
            powerOwner.getInventory().addItem(enderChestInventory.getContents());
        enderChestInventory.clear();
    }

    @Override
    public void deactivate() {

    }
}
