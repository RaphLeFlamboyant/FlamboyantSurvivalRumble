package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BowPower implements IChampionPower {
    @Override
    public void activate(Player powerOwner, int powerLevel) {
        var bow = new ItemStack(Material.BOW);
        var arrowStackNumber = 2;

        switch (powerLevel) {
            case 5:
                arrowStackNumber = 1;
                break;
            case 4:
                arrowStackNumber++;
            case 3:
                arrowStackNumber++;
                break;
        }


        bow.addEnchantment(Enchantment.ARROW_DAMAGE, powerLevel < 3 ? powerLevel - 1 : powerLevel);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, Math.max(0, powerLevel - 4));
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, Math.max(0, powerLevel - 4) * 2);

        powerOwner.getInventory().addItem(bow, new ItemStack(Material.ARROW, 64 * arrowStackNumber));
    }

    @Override
    public void deactivate() {

    }
}
