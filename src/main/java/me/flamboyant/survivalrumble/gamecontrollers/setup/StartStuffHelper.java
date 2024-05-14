package me.flamboyant.survivalrumble.gamecontrollers.setup;

import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class StartStuffHelper {
    public static void givePlayersStartStuff(StartStuffKind stuffKind) {
        List<ItemStack> stuff = baseStuff(stuffKind);
        for (Player player : Common.server.getOnlinePlayers()) {
            player.getInventory().clear();
            for (ItemStack item : stuff) {
                player.getInventory().addItem(item.clone());
            }
        }
    }
    
    private static List<ItemStack> baseStuff(StartStuffKind stuffKind) {
        if (stuffKind == StartStuffKind.BASIC)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.SWEET_BERRIES, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false)
            );

        if (stuffKind == StartStuffKind.MEDIUM)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.IRON_INGOT, 5, "Fer du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.REDSTONE, 64, "Poudre du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.OAK_LOG, 64, "Bois du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COAL, 32, "Charbon du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COBBLESTONE, 240, "Blocs du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COOKED_BEEF, 32, "Bouffe de pilote", Arrays.asList(), false, null, false, false)
            );

        if (stuffKind == StartStuffKind.FAST_GAME)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.OBSIDIAN, 5, "Obsidienne du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.IRON_INGOT, 64, "Fer du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.REDSTONE_BLOCK, 64, "Poudre du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.QUARTZ, 64, "Composant du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.SLIME_BALL, 64, "Gelée du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.OAK_LOG, 64, "Bois du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COAL_BLOCK, 32, "Charbon du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COBBLESTONE, 480, "Blocs du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.GOLDEN_APPLE, 2, "Pommes du pro", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COOKED_BEEF, 64, "Bouffe de pro", Arrays.asList(), false, null, false, false)
            );

        if (stuffKind == StartStuffKind.WTF)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.BAKED_POTATO, 16, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.ELYTRA, 1, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COBWEB, 32, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.FISHING_ROD, 32, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.BLAZE_ROD, 32, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.MANGROVE_LOG, 32, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.LIGHTNING_ROD, 32, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.OCHRE_FROGLIGHT, 64, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.GOLD_BLOCK, 64, "wtf", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.CREEPER_SPAWN_EGG, 32, "wtf", Arrays.asList(), false, null, false, false)
            );

        return Arrays.asList(
                ItemHelper.generateItem(Material.SWEET_BERRIES, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false)
        );
    }
}
