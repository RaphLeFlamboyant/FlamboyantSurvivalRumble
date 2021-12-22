package flamboyant.survivalrumble.utils;

import flamboyant.survivalrumble.data.PlayerClassMetadata;
import flamboyant.survivalrumble.data.PlayerClassType;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerClassHelper
{
    public static Map<PlayerClassType, PlayerClassMetadata> playerClassMetadata = new HashMap<PlayerClassType, PlayerClassMetadata>()
    {{
        //put(PlayerClassType.ASSASSIN, new PlayerClassMetadata(PlayerClassType.ASSASSIN, Material.STONE_SWORD, "ASSASSIN", Sound.BLOCK_CHAIN_PLACE, "Assassin", "Vous serez pers�cut�s"));
        put(PlayerClassType.WARRIOR, new PlayerClassMetadata(PlayerClassType.WARRIOR, Material.DIAMOND_CHESTPLATE, "GUERRIER", Sound.ENTITY_PLAYER_ATTACK_STRONG, "Guerrier ", "Pr�t � vous �craser"));
        put(PlayerClassType.SCOUT, new PlayerClassMetadata(PlayerClassType.SCOUT, Material.TORCH, "�CLAIREUR", Sound.BLOCK_GRASS_STEP, "�claireur", "connait votre base par coeur"));
        //put(PlayerClassType.NINJA, new PlayerClassMetadata(PlayerClassType.NINJA, Material.BLACK_STAINED_GLASS_PANE, "NINJA", Sound.ENTITY_PUFFER_FISH_BLOW_OUT, "Ninja", "Toujours proche de vous"));
        //put(PlayerClassType.THIEF, new PlayerClassMetadata(PlayerClassType.THIEF, Material.CHEST, "VOLEUR", Sound.BLOCK_CHEST_OPEN, "Voleur", "Vos coffres seront ses coffres"));
        //put(PlayerClassType.BANKER, new PlayerClassMetadata(PlayerClassType.BANKER, Material.GOLD_INGOT, "BANQUIER", Sound.BLOCK_CHEST_LOCKED, "Banquier", "Sa richesse est votre perte"));
        put(PlayerClassType.MASON, new PlayerClassMetadata(PlayerClassType.MASON, Material.BRICK, "MA�ON", Sound.BLOCK_NETHER_BRICKS_BREAK, "Ma�on", "Une d�faite pav�e de briques"));
        put(PlayerClassType.BUNGLER, new PlayerClassMetadata(PlayerClassType.BUNGLER, Material.STONE_PICKAXE, "SABOTEUR", Sound.BLOCK_GLASS_BREAK, "Saboteur", "Votre base sera un gruy�re"));
        put(PlayerClassType.PYROMANIAC, new PlayerClassMetadata(PlayerClassType.PYROMANIAC, Material.BLAZE_POWDER, "PYROMANE", Sound.BLOCK_PISTON_EXTEND, "Pyromane", "va vous r�duire en cendres"));
        put(PlayerClassType.TRAPPER, new PlayerClassMetadata(PlayerClassType.TRAPPER, Material.SPRUCE_TRAPDOOR, "TRAPPEUR", Sound.BLOCK_IRON_TRAPDOOR_OPEN, "Trappeur", "Bouger c'est mourir"));
        put(PlayerClassType.ELECTRICIAN, new PlayerClassMetadata(PlayerClassType.ELECTRICIAN, Material.REDSTONE_LAMP, "�LECTRICIEN", Sound.ENTITY_LIGHTNING_BOLT_THUNDER, "�lectricien", "et son ambiance survolt�e"));
        put(PlayerClassType.BOMBER, new PlayerClassMetadata(PlayerClassType.BOMBER, Material.TNT_MINECART, "BOMBARDIER FOU", Sound.ENTITY_GENERIC_EXPLODE, "Bombadier Fou", "Pire que 100 creepers"));
        //put(PlayerClassType.WEREWOLF, new PlayerClassMetadata(PlayerClassType.WEREWOLF, Material.BONE, "LOUP GAROU", Sound.ENTITY_WOLF_GROWL, "Loup Garou", "Il salive d'impatience"));
        put(PlayerClassType.ELBMURLAVIVRUS, new PlayerClassMetadata(PlayerClassType.ELBMURLAVIVRUS, Material.BAKED_POTATO, "SURVIVANT DU ELBMUR LAVIVRUS", Sound.ENTITY_VILLAGER_CELEBRATE, "Le Survivant", "du Elbmur Lavivrus"));
        put(PlayerClassType.PEASANT, new PlayerClassMetadata(PlayerClassType.PEASANT, Material.HAY_BLOCK, "PAYSAN", Sound.BLOCK_GRASS_BREAK, "Paysan", "Il cultive sa victoire"));
        put(PlayerClassType.HUNTER, new PlayerClassMetadata(PlayerClassType.HUNTER, Material.BOW, "CHASSEUR", Sound.ITEM_CROSSBOW_HIT, "Chasseur", "Ses fl�ches vous harc�lent"));
        put(PlayerClassType.BLACKSMITH, new PlayerClassMetadata(PlayerClassType.BLACKSMITH, Material.ANVIL, "FORGERON", Sound.BLOCK_ANVIL_USE, "Forgeron", "Forge son avenir"));
        put(PlayerClassType.BADKID, new PlayerClassMetadata(PlayerClassType.BADKID, Material.ARMOR_STAND, "SALEGAMIN", Sound.ENTITY_PARROT_IMITATE_WITCH, "Sale Gamin", "Le roi du prank"));
        put(PlayerClassType.CRAFTER, new PlayerClassMetadata(PlayerClassType.CRAFTER, Material.CRAFTING_TABLE, "FABRIQUANT", Sound.BLOCK_SMITHING_TABLE_USE, "Fabriquant", "Jouer lui suffira � gagner"));

    }};
}
