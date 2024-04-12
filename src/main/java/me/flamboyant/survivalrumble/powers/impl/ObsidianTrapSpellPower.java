package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ObsidianTrapSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.IRON_BARS,
                1,
                "Prison",
                Arrays.asList("Enferme tous les  assaillants", "dans une prison d'obsidienne"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        var data = SurvivalRumbleData.getSingleton();
        var ownerTeamName = data.getPlayerTeam(powerOwner);
        for (Player player : data.getAttackingPlayers(powerOwner)) {
            if (!TeamHelper.isLocationInHeadQuarter(player.getLocation(), ownerTeamName))
                continue;

            var world = player.getWorld();
            var xLoc = player.getLocation().getBlockX();
            var yLoc = player.getLocation().getBlockY();
            var zLoc = player.getLocation().getBlockZ();

            world.getBlockAt(xLoc, yLoc - 1, zLoc).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc, yLoc + 2, zLoc).setType(Material.OBSIDIAN);

            world.getBlockAt(xLoc - 1, yLoc, zLoc - 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc + 1, yLoc, zLoc - 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc - 1, yLoc, zLoc + 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc + 1, yLoc, zLoc + 1).setType(Material.OBSIDIAN);

            world.getBlockAt(xLoc - 1, yLoc + 1, zLoc - 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc + 1, yLoc + 1, zLoc - 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc - 1, yLoc + 1, zLoc + 1).setType(Material.OBSIDIAN);
            world.getBlockAt(xLoc + 1, yLoc + 1, zLoc + 1).setType(Material.OBSIDIAN);
        }

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 15;
    }
}
