package me.flamboyant.survivalrumble.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MaterialHelper {
    public static ArrayList<Material> banners = new ArrayList<Material>(Arrays.asList(Material.BLUE_BANNER, Material.RED_BANNER, Material.GREEN_BANNER, Material.YELLOW_BANNER, Material.BLACK_BANNER, Material.PURPLE_BANNER));

    public static Map<Material, Integer> constructionMaterial = new HashMap<Material, Integer>() {{
        put(Material.BRICKS, 6);
        put(Material.BRICK_SLAB, 3);
        put(Material.BRICK_STAIRS, 6);
        put(Material.BRICK_WALL, 6);
        put(Material.MUD_BRICKS, 2);
        put(Material.MUD_BRICK_SLAB, 1);
        put(Material.MUD_BRICK_STAIRS, 2);
        put(Material.MUD_BRICK_WALL, 2);
        put(Material.CHISELED_NETHER_BRICKS, 4);
        put(Material.NETHER_BRICKS, 4);
        put(Material.NETHER_BRICK_FENCE, 4);
        put(Material.NETHER_BRICK_SLAB, 2);
        put(Material.NETHER_BRICK_STAIRS, 4);
        put(Material.NETHER_BRICK_WALL, 4);
        put(Material.CRACKED_NETHER_BRICKS, 5);
        put(Material.RED_NETHER_BRICKS, 5);
        put(Material.RED_NETHER_BRICK_SLAB, 2);
        put(Material.RED_NETHER_BRICK_WALL, 5);
        put(Material.RED_NETHER_BRICK_STAIRS, 5);
        put(Material.END_STONE_BRICKS, 3);
        put(Material.END_STONE_BRICK_STAIRS, 3);
        put(Material.END_STONE_BRICK_WALL, 3);
        put(Material.END_STONE_BRICK_SLAB, 1);
        put(Material.MOSSY_STONE_BRICKS, 4);
        put(Material.MOSSY_STONE_BRICK_STAIRS, 4);
        put(Material.MOSSY_STONE_BRICK_WALL, 4);
        put(Material.MOSSY_STONE_BRICK_SLAB, 2);
        put(Material.BLACKSTONE, 2);
        put(Material.BLACKSTONE_SLAB, 1);
        put(Material.BLACKSTONE_STAIRS, 2);
        put(Material.BLACKSTONE_WALL, 2);
        put(Material.CHISELED_POLISHED_BLACKSTONE, 2);
        put(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, 3);
        put(Material.GILDED_BLACKSTONE, 10);
        put(Material.POLISHED_BLACKSTONE, 2);
        put(Material.POLISHED_BLACKSTONE_BRICK_SLAB, 1);
        put(Material.POLISHED_BLACKSTONE_BRICK_WALL, 2);
        put(Material.POLISHED_BLACKSTONE_BRICK_STAIRS, 2);
        put(Material.POLISHED_BLACKSTONE_BRICKS, 2);
        put(Material.POLISHED_BLACKSTONE_SLAB, 1);
        put(Material.POLISHED_BLACKSTONE_STAIRS, 2);
        put(Material.POLISHED_BLACKSTONE_WALL, 2);
        put(Material.DEEPSLATE_BRICKS, 2);
        put(Material.DEEPSLATE_BRICK_SLAB, 1);
        put(Material.DEEPSLATE_BRICK_STAIRS, 2);
        put(Material.DEEPSLATE_BRICK_WALL, 2);
        put(Material.DEEPSLATE_TILES, 2);
        put(Material.DEEPSLATE_TILE_SLAB, 1);
        put(Material.DEEPSLATE_TILE_STAIRS, 2);
        put(Material.DEEPSLATE_TILE_WALL, 2);
        put(Material.DEEPSLATE, 2);
        put(Material.CHISELED_DEEPSLATE, 2);
        put(Material.COBBLED_DEEPSLATE, 2);
        put(Material.COBBLED_DEEPSLATE_SLAB, 1);
        put(Material.COBBLED_DEEPSLATE_STAIRS, 2);
        put(Material.COBBLED_DEEPSLATE_WALL, 2);
        put(Material.POLISHED_DEEPSLATE, 2);
        put(Material.POLISHED_DEEPSLATE_SLAB, 1);
        put(Material.POLISHED_DEEPSLATE_STAIRS, 2);
        put(Material.POLISHED_DEEPSLATE_WALL, 2);
        put(Material.CRACKED_DEEPSLATE_BRICKS, 2);
        put(Material.CRACKED_DEEPSLATE_TILES, 2);
        put(Material.PRISMARINE, 3);
        put(Material.PRISMARINE_BRICK_SLAB, 2);
        put(Material.PRISMARINE_BRICK_STAIRS, 4);
        put(Material.PRISMARINE_BRICKS, 4);
        put(Material.PRISMARINE_SLAB, 1);
        put(Material.PRISMARINE_STAIRS, 3);
        put(Material.PRISMARINE_WALL, 3);
        put(Material.DARK_PRISMARINE, 5);
        put(Material.DARK_PRISMARINE_SLAB, 2);
        put(Material.DARK_PRISMARINE_STAIRS, 5);
        put(Material.QUARTZ_BLOCK, 4);
        put(Material.QUARTZ_BRICKS, 4);
        put(Material.QUARTZ_PILLAR, 4);
        put(Material.QUARTZ_SLAB, 2);
        put(Material.QUARTZ_STAIRS, 4);
        put(Material.CHISELED_QUARTZ_BLOCK, 4);
        put(Material.SMOOTH_QUARTZ, 4);
        put(Material.SMOOTH_QUARTZ_SLAB, 2);
        put(Material.SMOOTH_QUARTZ_STAIRS, 4);
        put(Material.STONE_BRICKS, 3);
        put(Material.STONE_BRICK_SLAB, 1);
        put(Material.STONE_BRICK_STAIRS, 3);
        put(Material.STONE_BRICK_WALL, 3);
        put(Material.POLISHED_ANDESITE, 2);
        put(Material.POLISHED_ANDESITE_SLAB, 1);
        put(Material.POLISHED_ANDESITE_STAIRS, 2);
        put(Material.POLISHED_BASALT, 3);
        put(Material.POLISHED_DIORITE, 2);
        put(Material.POLISHED_DIORITE_SLAB, 1);
        put(Material.POLISHED_DIORITE_STAIRS, 2);
        put(Material.POLISHED_GRANITE, 2);
        put(Material.POLISHED_GRANITE_SLAB, 1);
        put(Material.POLISHED_GRANITE_STAIRS, 2);
        put(Material.COPPER_BLOCK, 6);
        put(Material.RAW_COPPER_BLOCK, 6);
        put(Material.WAXED_COPPER_BLOCK, 6);
        put(Material.CUT_COPPER_SLAB, 3);
        put(Material.EXPOSED_CUT_COPPER_SLAB, 3);
        put(Material.OXIDIZED_CUT_COPPER_SLAB, 3);
        put(Material.WAXED_CUT_COPPER_SLAB, 3);
        put(Material.WAXED_EXPOSED_CUT_COPPER_SLAB, 3);
        put(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB, 3);
        put(Material.WAXED_WEATHERED_CUT_COPPER_SLAB, 3);
        put(Material.WEATHERED_CUT_COPPER_SLAB, 3);
        put(Material.CUT_COPPER_STAIRS, 6);
        put(Material.EXPOSED_CUT_COPPER_STAIRS, 6);
        put(Material.OXIDIZED_CUT_COPPER_STAIRS, 6);
        put(Material.WAXED_CUT_COPPER_STAIRS, 6);
        put(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, 6);
        put(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS, 6);
        put(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, 6);
        put(Material.WEATHERED_CUT_COPPER_STAIRS, 6);
        put(Material.AMETHYST_BLOCK, 4);
        put(Material.ACACIA_PLANKS, 2);
        put(Material.ACACIA_DOOR, 4);
        put(Material.ACACIA_FENCE, 2);
        put(Material.ACACIA_FENCE_GATE, 2);
        put(Material.ACACIA_SLAB, 1);
        put(Material.ACACIA_STAIRS, 2);
        put(Material.ACACIA_TRAPDOOR, 2);
        put(Material.BIRCH_PLANKS, 2);
        put(Material.BIRCH_DOOR, 4);
        put(Material.BIRCH_FENCE, 2);
        put(Material.BIRCH_FENCE_GATE, 2);
        put(Material.BIRCH_SLAB, 1);
        put(Material.BIRCH_STAIRS, 2);
        put(Material.BIRCH_TRAPDOOR, 2);
        put(Material.CRIMSON_PLANKS, 2);
        put(Material.CRIMSON_DOOR, 4);
        put(Material.CRIMSON_FENCE, 2);
        put(Material.CRIMSON_FENCE_GATE, 2);
        put(Material.CRIMSON_SLAB, 1);
        put(Material.CRIMSON_STAIRS, 2);
        put(Material.CRIMSON_TRAPDOOR, 2);
        put(Material.DARK_OAK_PLANKS, 2);
        put(Material.DARK_OAK_DOOR, 4);
        put(Material.DARK_OAK_FENCE, 2);
        put(Material.DARK_OAK_FENCE_GATE, 2);
        put(Material.DARK_OAK_SLAB, 1);
        put(Material.DARK_OAK_STAIRS, 2);
        put(Material.DARK_OAK_TRAPDOOR, 2);
        put(Material.JUNGLE_PLANKS, 2);
        put(Material.JUNGLE_DOOR, 4);
        put(Material.JUNGLE_FENCE, 2);
        put(Material.JUNGLE_FENCE_GATE, 2);
        put(Material.JUNGLE_SLAB, 1);
        put(Material.JUNGLE_STAIRS, 2);
        put(Material.JUNGLE_TRAPDOOR, 2);
        put(Material.OAK_PLANKS, 2);
        put(Material.OAK_DOOR, 4);
        put(Material.OAK_FENCE, 2);
        put(Material.OAK_FENCE_GATE, 2);
        put(Material.OAK_SLAB, 1);
        put(Material.OAK_STAIRS, 2);
        put(Material.OAK_TRAPDOOR, 2);
        put(Material.MANGROVE_PLANKS, 2);
        put(Material.MANGROVE_DOOR, 4);
        put(Material.MANGROVE_FENCE, 2);
        put(Material.MANGROVE_FENCE_GATE, 2);
        put(Material.MANGROVE_SLAB, 1);
        put(Material.MANGROVE_STAIRS, 2);
        put(Material.MANGROVE_TRAPDOOR, 2);
        put(Material.SPRUCE_PLANKS, 2);
        put(Material.SPRUCE_DOOR, 4);
        put(Material.SPRUCE_FENCE, 2);
        put(Material.SPRUCE_FENCE_GATE, 2);
        put(Material.SPRUCE_SLAB, 1);
        put(Material.SPRUCE_STAIRS, 2);
        put(Material.SPRUCE_TRAPDOOR, 2);
        put(Material.WARPED_PLANKS, 2);
        put(Material.WARPED_DOOR, 4);
        put(Material.WARPED_FENCE, 2);
        put(Material.WARPED_FENCE_GATE, 2);
        put(Material.WARPED_SLAB, 1);
        put(Material.WARPED_STAIRS, 2);
        put(Material.WARPED_TRAPDOOR, 2);
        put(Material.EMERALD_BLOCK, 10);
        put(Material.LAPIS_BLOCK, 5);
        put(Material.LANTERN, 4);
        put(Material.SOUL_LANTERN, 5);
        put(Material.SEA_LANTERN, 4);
        put(Material.SHROOMLIGHT, 3);
        put(Material.OCHRE_FROGLIGHT, 8);
        put(Material.PEARLESCENT_FROGLIGHT, 8);
        put(Material.VERDANT_FROGLIGHT, 8);
        put(Material.REDSTONE_LAMP, 4);
        put(Material.GLOWSTONE, 3);
    }};
}
