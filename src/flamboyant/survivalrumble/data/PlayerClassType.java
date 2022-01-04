package flamboyant.survivalrumble.data;

public enum PlayerClassType {
    // Vanilla roles
    WARRIOR, // Score by killing
    HUNTER, // Score by hitting foes with arrows
    WITCH, // Score by throwing potions on foes

    SCOUT, // Score by being close to foes base

    MASON, // Clay based Bricks give twice the amount of points, warned when blocs are destroys in the base
    PEASANT, // Hay bale is a 10 points based bloc instead 0
    BUNGLER, // Destroy the foes blocs to get points
    ELBMURLAVIVRUS, // revert bloc score in his hq and destroy natural bloc earn points

    ELECTRICIAN, // Iron, Gold, Diamond and Netherite blocs in base generate points every minutes
    PYROMANIAC, // Score by burning blocs in foes HQ
    TRAPPER, // Score when foes < 100 blocks away die from PvE
    BLACKSMITH, // Score when crafting armors in HQ location
    BADKID, // Score by hitting foes with eggs or snowball
    CRAFTER, // Score by crafting new items
    ANTIQUARIAN, // Score by getting rare items
    DEFEATIST, // Score by dying
    CHEERLEADER, // Score when foes are killed by an ally < 50 blocs away
    FISHER, // Score when fishing in its HQ

    // Non Vanilla roles
    BOMBER, // Score by exploding TNT in foes HQ

    // Non codés
    // Non Vanilla
    ASSASSIN, // Score by killing, can detect foes location
    WEREWOLF, // Score by killing foes by night, has Night Vision, Strengh I and Speed I by night. -1000 pts
    NINJA, // Score by being close to foes, location undetectable, can be invisible

    // Vanilla
    THIEF, // Score by stealing things in foes chests
    BANKER, // Each gold block in HQ chest generatese points (like electrician block for example)
}