package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class SummoningSpellPower extends ASpellPower {
    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.ZOMBIE_HEAD,
                1,
                "Invocation",
                Arrays.asList("Invoque des mobs", "près des assaillants"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    private HashMap<Integer, EntityType> probabilityToMobType = new HashMap<>() {{
       put(50, EntityType.ZOMBIE);
       put(70, EntityType.SKELETON);
       put(75, EntityType.SKELETON_HORSE);
       put(85, EntityType.SPIDER);
       put(86, EntityType.VEX);
       put(90, EntityType.WITCH);
       put(93, EntityType.BLAZE);
       put(96, EntityType.WITHER_SKELETON);
       put(98, EntityType.STRAY);
       put(100, EntityType.VINDICATOR);
    }};

    @Override
    protected boolean applySpellEffect() {
        var data = SurvivalRumbleData.getSingleton();
        var ownerTeamName = data.getPlayerTeam(powerOwner);
        var assaultPlayers = data.getAttackingPlayers(powerOwner);

        for (Player player : assaultPlayers) {
            if (!TeamHelper.isLocationInHeadQuarter(player.getLocation(), ownerTeamName))
                continue;

            int rndInt = Common.rng.nextInt(100);
            EntityType mobToSpawn = EntityType.ZOMBIE;

            for (Integer probability : probabilityToMobType.keySet()) {
                if (probability < rndInt)
                    continue;

                mobToSpawn = probabilityToMobType.get(probability);

                break;
            }

            for (int i = 0; i < 6; i++) {
                player.getWorld().spawnEntity(player.getLocation(), mobToSpawn);
            }
        }

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 5;
    }
}
