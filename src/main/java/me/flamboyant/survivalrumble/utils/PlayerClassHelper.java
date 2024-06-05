package me.flamboyant.survivalrumble.utils;

import me.flamboyant.survivalrumble.data.PlayerClassCategory;
import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class PlayerClassHelper {
    public static Map<PlayerClassType, PlayerClassMetadata> playerClassMetadata = new HashMap<>() {{
        put(PlayerClassType.WARRIOR, new PlayerClassMetadata(PlayerClassType.WARRIOR, PlayerClassCategory.PVP, Material.DIAMOND_CHESTPLATE, "GUERRIER", Sound.ENTITY_PLAYER_ATTACK_STRONG, "Guerrier ", "Prêt à vous écraser"));
        put(PlayerClassType.SCOUT, new PlayerClassMetadata(PlayerClassType.SCOUT, PlayerClassCategory.ATTACK, Material.TORCH, "ÉCLAIREUR", Sound.BLOCK_GRASS_STEP, "Éclaireur", "connait votre base par coeur"));
        //put(PlayerClassType.BANKER, new PlayerClassMetadata(PlayerClassType.BANKER, Material.GOLD_INGOT, "BANQUIER", Sound.BLOCK_CHEST_LOCKED, "Banquier", "Sa richesse est votre perte"));
        put(PlayerClassType.MASON, new PlayerClassMetadata(PlayerClassType.MASON, PlayerClassCategory.SURVIVAL, Material.BRICK, "MAçON", Sound.BLOCK_NETHER_BRICKS_BREAK, "Maçon", "Une défaite pavée de briques"));
        put(PlayerClassType.BUNGLER, new PlayerClassMetadata(PlayerClassType.BUNGLER, PlayerClassCategory.ATTACK, Material.STONE_PICKAXE, "SABOTEUR", Sound.BLOCK_GLASS_BREAK, "Saboteur", "Votre base sera un gruyère"));
        put(PlayerClassType.PYROMANIAC, new PlayerClassMetadata(PlayerClassType.PYROMANIAC, PlayerClassCategory.ATTACK, Material.BLAZE_POWDER, "PYROMANE", Sound.BLOCK_PISTON_EXTEND, "Pyromane", "va vous réduire en cendres"));
        put(PlayerClassType.TRAPPER, new PlayerClassMetadata(PlayerClassType.TRAPPER, PlayerClassCategory.REDSTONE, Material.SPRUCE_TRAPDOOR, "TRAPPEUR", Sound.BLOCK_IRON_TRAPDOOR_OPEN, "Trappeur", "Bouger c'est mourir"));
        put(PlayerClassType.ELECTRICIAN, new PlayerClassMetadata(PlayerClassType.ELECTRICIAN, PlayerClassCategory.SURVIVAL, Material.REDSTONE_LAMP, "ÉLECTRICIEN", Sound.ENTITY_LIGHTNING_BOLT_THUNDER, "Électricien", "et son ambiance survoltée"));
        put(PlayerClassType.ELBMURLAVIVRUS, new PlayerClassMetadata(PlayerClassType.ELBMURLAVIVRUS, PlayerClassCategory.DEFENSE, Material.BAKED_POTATO, "ELBMUR LAVIVRUS", Sound.ENTITY_VILLAGER_CELEBRATE, "Elbmur Lavivrus", "A déjà vaincu le jeu"));
        put(PlayerClassType.PEASANT, new PlayerClassMetadata(PlayerClassType.PEASANT, PlayerClassCategory.SURVIVAL, Material.HAY_BLOCK, "PAYSAN", Sound.BLOCK_GRASS_BREAK, "Paysan", "Il cultive sa victoire"));
        put(PlayerClassType.ARCHER, new PlayerClassMetadata(PlayerClassType.ARCHER, PlayerClassCategory.PVP, Material.BOW, "ARCHER", Sound.ITEM_CROSSBOW_HIT, "Archer", "Ses flèches vous harcélent"));
        put(PlayerClassType.BLACKSMITH, new PlayerClassMetadata(PlayerClassType.BLACKSMITH, PlayerClassCategory.SURVIVAL, Material.ANVIL, "FORGERON", Sound.BLOCK_ANVIL_USE, "Forgeron", "Forge son avenir"));
        put(PlayerClassType.BADKID, new PlayerClassMetadata(PlayerClassType.BADKID, PlayerClassCategory.PVP, Material.ARMOR_STAND, "SALEGAMIN", Sound.ENTITY_PARROT_IMITATE_WITCH, "Sale Gamin", "Le roi du prank"));
        put(PlayerClassType.CRAFTER, new PlayerClassMetadata(PlayerClassType.CRAFTER, PlayerClassCategory.SURVIVAL, Material.CRAFTING_TABLE, "FABRIQUANT", Sound.BLOCK_SMITHING_TABLE_USE, "Fabriquant", "Jouer lui suffira à gagner"));
        put(PlayerClassType.ANTIQUARIAN, new PlayerClassMetadata(PlayerClassType.ANTIQUARIAN, PlayerClassCategory.SURVIVAL, Material.ANCIENT_DEBRIS, "ANTIQUAIRE", Sound.BLOCK_CHEST_OPEN, "Antiquaire", "Étend ses collections"));
        put(PlayerClassType.WITCH, new PlayerClassMetadata(PlayerClassType.WITCH, PlayerClassCategory.PVP, Material.SPLASH_POTION, "SORCIÈRE", Sound.ENTITY_WITCH_AMBIENT, "Sorcière", "Concocte votre défaite"));
        put(PlayerClassType.DEFEATIST, new PlayerClassMetadata(PlayerClassType.DEFEATIST, PlayerClassCategory.PVP, Material.POISONOUS_POTATO, "DÉFAITISTE", Sound.ENTITY_VILLAGER_AMBIENT, "Défaitiste", "Sa défaite est la vôtre"));
        put(PlayerClassType.CHEERLEADER, new PlayerClassMetadata(PlayerClassType.CHEERLEADER, PlayerClassCategory.PVP, Material.FIREWORK_STAR, "POM-POM GIRL", Sound.ENTITY_VILLAGER_CELEBRATE, "Pom-pom Girl", "Ses alliés sont sa force"));
        put(PlayerClassType.FISHER, new PlayerClassMetadata(PlayerClassType.FISHER, PlayerClassCategory.SURVIVAL, Material.FISHING_ROD, "PÊCHEUR", Sound.ENTITY_FISHING_BOBBER_THROW, "Pêcheur", "Une force tranquille"));
        put(PlayerClassType.BOORISH, new PlayerClassMetadata(PlayerClassType.BOORISH, PlayerClassCategory.DEFENSE, Material.COAL_ORE, "RUSTRE", Sound.ENTITY_PILLAGER_AMBIENT, "Rustre", "Il vit dans une grotte"));
        put(PlayerClassType.HUNTER, new PlayerClassMetadata(PlayerClassType.HUNTER, PlayerClassCategory.SURVIVAL, Material.BEEF, "CHASSEUR", Sound.ENTITY_COW_DEATH, "Chasseur", "Un flair inégalé"));
        put(PlayerClassType.STANDARDBEARER, new PlayerClassMetadata(PlayerClassType.STANDARDBEARER, PlayerClassCategory.DEFENSE, Material.LIGHT_BLUE_BANNER, "PORTE ETENDARD", Sound.ITEM_GOAT_HORN_SOUND_0, "Porte-Étendard", "Il apprécie votre visite"));
        put(PlayerClassType.ENGINEER, new PlayerClassMetadata(PlayerClassType.ENGINEER, PlayerClassCategory.REDSTONE, Material.REDSTONE, "INGÉNIEUR", Sound.ENTITY_VILLAGER_WORK_TOOLSMITH, "Ingénieur", "Il construit des machines"));
        put(PlayerClassType.SHAMELESS, new PlayerClassMetadata(PlayerClassType.SHAMELESS, PlayerClassCategory.ATTACK, Material.DIRT, "SANS GÊNE", Sound.BLOCK_COMPOSTER_FILL, "Sans Gêne", "Il se croit chez lui"));
        put(PlayerClassType.NUDIST, new PlayerClassMetadata(PlayerClassType.NUDIST, PlayerClassCategory.ATTACK, Material.LEATHER_LEGGINGS, "NUDISTE", Sound.ITEM_ARMOR_EQUIP_LEATHER, "Nudiste", "Il aime voyager léger"));
        put(PlayerClassType.NARCOLEPTIC, new PlayerClassMetadata(PlayerClassType.NARCOLEPTIC, PlayerClassCategory.DEFENSE, Material.MAGENTA_BED, "NARCOLEPTIQUE", Sound.ENTITY_FOX_SLEEP, "Narcoleptique", "Dort aux pires moments"));

        put(PlayerClassType.BOMBER, new PlayerClassMetadata(PlayerClassType.BOMBER, PlayerClassCategory.NON_VANILLA, Material.TNT_MINECART, "BOMBARDIER FOU", Sound.ENTITY_GENERIC_EXPLODE, "Bombadier Fou", "Pire que 100 creepers"));
        put(PlayerClassType.WEREWOLF, new PlayerClassMetadata(PlayerClassType.WEREWOLF, PlayerClassCategory.NON_VANILLA, Material.BONE, "LOUP GAROU", Sound.ENTITY_WOLF_GROWL, "Loup Garou", "Il salive d'impatience"));
        put(PlayerClassType.ASSASSIN, new PlayerClassMetadata(PlayerClassType.ASSASSIN, PlayerClassCategory.NON_VANILLA, Material.STONE_SWORD, "ASSASSIN", Sound.BLOCK_CHAIN_PLACE, "Assassin", "Vous serez persécutés"));
        put(PlayerClassType.THIEF, new PlayerClassMetadata(PlayerClassType.THIEF, PlayerClassCategory.NON_VANILLA, Material.CHEST, "VOLEUR", Sound.BLOCK_CHEST_OPEN, "Voleur", "Votre stuff est le sien"));
        put(PlayerClassType.NINJA, new PlayerClassMetadata(PlayerClassType.NINJA, PlayerClassCategory.NON_VANILLA, Material.BLACK_STAINED_GLASS_PANE, "NINJA", Sound.ENTITY_PUFFER_FISH_BLOW_OUT, "Ninja", "Toujours proche de vous"));

        put(PlayerClassType.FAKE_CLASS, new PlayerClassMetadata(PlayerClassType.FAKE_CLASS, PlayerClassCategory.PVP, Material.COMMAND_BLOCK, "FAKE_CLASS", Sound.BLOCK_STONE_BUTTON_CLICK_ON, "Fake class", "Si tu vois ça c'est un bug"));
    }};
}
