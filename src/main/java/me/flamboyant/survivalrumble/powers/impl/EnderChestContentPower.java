package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnderChestContentPower implements IChampionPower {
    @Override
    public void activate(Player powerOwner, int powerLevel) {
        var enderChestInventory = powerOwner.getEnderChest();
        var toto = Arrays.stream(enderChestInventory.getContents()).filter((e) -> e != null).collect(Collectors.toList()).toArray(new ItemStack[0]);
        powerOwner.getInventory().addItem(toto);
        enderChestInventory.clear();
    }

    @Override
    public void deactivate() {

    }
}
