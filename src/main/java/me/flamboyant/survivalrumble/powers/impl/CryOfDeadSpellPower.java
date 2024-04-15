package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class CryOfDeadSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.GHAST_TEAR,
                1,
                "Cri d'Effroi",
                Arrays.asList("Envoi les adversaires", "dans les airs à mesure", "qu'ils sont proches"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        var data = SurvivalRumbleData.getSingleton();
        var assaultPlayers = data.getAttackingPlayers(powerOwner);

        for (Player player : assaultPlayers) {
            if (player.getWorld() != powerOwner.getWorld())
                continue;

            double ratio = Math.max((100.0 - player.getLocation().distance(powerOwner.getLocation())) / 100.0, 0);
            player.damage(1, powerOwner);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                Vector velocity = player.getVelocity();
                player.setVelocity(new Vector(0, 0.3 * ratio, 0));
            }, 1);
        }

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 2;
    }
}
