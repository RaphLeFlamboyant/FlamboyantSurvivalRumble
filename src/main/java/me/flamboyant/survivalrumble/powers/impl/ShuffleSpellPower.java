package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ShuffleSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.PLAYER_HEAD,
                1,
                "Shuffle",
                Arrays.asList("Intervertit la position", "des assaillants"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        var players = SurvivalRumbleData.getSingleton().getAttackingPlayers(powerOwner);
        var previousPlayerLocation = powerOwner.getLocation();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            var location = player.getLocation();

            if (i > 0) {
                player.teleport(previousPlayerLocation);
            }

            previousPlayerLocation = location;
        }

        players.get(0).teleport(previousPlayerLocation);

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 10;
    }
}
