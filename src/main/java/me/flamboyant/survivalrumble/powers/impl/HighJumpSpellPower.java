package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class HighJumpSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.LEATHER_BOOTS,
                1,
                "Saut",
                Arrays.asList("Fait un grand saut"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        Vector velocity = powerOwner.getVelocity();
        powerOwner.setVelocity(new Vector(velocity.getX(), 50, velocity.getZ()));
        powerOwner.getWorld().playSound(powerOwner, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1.2f);

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 2;
    }
}
