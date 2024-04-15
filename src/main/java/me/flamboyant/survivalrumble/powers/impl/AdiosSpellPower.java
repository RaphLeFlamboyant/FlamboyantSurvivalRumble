package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class AdiosSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.DIAMOND_PICKAXE,
                1,
                "Adios!",
                Arrays.asList("Fait disparaitre tous les", "blocs sous les pieds du", "capitaine jusqu'à la couche 0"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        var xLoc = powerOwner.getLocation().getBlockX();
        var zLoc = powerOwner.getLocation().getBlockZ();
        var world = powerOwner.getWorld();

        for (int i = 0; i <= powerOwner.getLocation().getBlockY(); i++) {
            world.getBlockAt(xLoc, i, zLoc).setType(Material.AIR);
        }

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 10;
    }
}
