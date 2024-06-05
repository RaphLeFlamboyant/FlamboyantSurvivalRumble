package me.flamboyant.survivalrumble.data;

public enum PlayerClassType {
    // Vanilla roles
    WARRIOR, // Scores by killing
    ARCHER, // Scores by hitting foes with arrows
    WITCH, // Scores by throwing potions on foes

    SCOUT, // Scores by being close to foes base
    STANDARDBEARER, // Scores by having foes close to the base

    MASON, // Clay based Bricks give twice the amount of points, warned when blocs are destroys in the base
    PEASANT, // Hay bale is a 10 points based bloc instead 0
    BOORISH, // Ores give points instead 0
    BUNGLER, // Destroy the foes blocs to get points
    ELBMURLAVIVRUS, // revert bloc Scores in his hq and destroy natural bloc earn points

    ELECTRICIAN, // Iron, Gold, Diamond and Netherite blocs in base generate points every minutes
    PYROMANIAC, // Scores by burning blocs in foes HQ
    TRAPPER, // Scores when foes < 100 blocks away die from PvE
    BLACKSMITH, // Scores when crafting armors in HQ location
    BADKID, // Scores by hitting foes with eggs or snowball
    CRAFTER, // Scores by crafting new items
    ANTIQUARIAN, // Scores by getting rare items
    DEFEATIST, // Scores by dying
    CHEERLEADER, // Scores when foes are killed by an ally < 50 blocs away
    FISHER, // Scores when fishing in its HQ
    HUNTER, // Scores by killing mobs
    ENGINEER, // Scores by crafting Redstone items
    SHAMELESS, // Scores by making survivale stuff in ennemy team base
    NUDIST, // Scores by being close to foes with few stuff
    NARCOLEPTIC, // Scores by sleeping close to attacking foes

    // Non Vanilla roles
    BOMBER, // Scores by exploding TNT in foes HQ

    // Non codés
    // Non Vanilla
    ASSASSIN, // Scores by killing, can detect foes location
    WEREWOLF, // Scores by killing foes by night, has Night Vision, Strengh I and Speed I by night. -1000 pts
    THIEF, // Scores by putting foes blocs in the base, earn a copy of one ennemy player crafted items
    NINJA, // Scores by being close to foes, location undetectable, can be invisible

    // Vanilla
    BANKER, // Each gold block in HQ chest generatese points (like electrician block for example)
    FAKE_CLASS, // Scores by killing foes by night, has Night Vision, Strengh I and Speed I by night. -1000 pts
}