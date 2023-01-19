package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class PyromaniacClass extends APlayerClass {
    private static final int scoringCoef = 20;
    private static final int malusCoef = -16;

    private HashSet<Material> validBurnableBlocks = new HashSet<>(Arrays.asList(
            Material.OAK_LOG,
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_MANGROVE_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG,
            Material.ACACIA_WOOD,
            Material.BIRCH_WOOD,
            Material.DARK_OAK_WOOD,
            Material.JUNGLE_WOOD,
            Material.MANGROVE_WOOD,
            Material.OAK_WOOD,
            Material.SPRUCE_WOOD,
            Material.STRIPPED_ACACIA_WOOD,
            Material.STRIPPED_BIRCH_WOOD,
            Material.STRIPPED_DARK_OAK_WOOD,
            Material.STRIPPED_JUNGLE_WOOD,
            Material.STRIPPED_MANGROVE_WOOD,
            Material.STRIPPED_OAK_WOOD,
            Material.STRIPPED_SPRUCE_WOOD,
            Material.COAL_BLOCK,
            Material.OAK_PLANKS,
            Material.ACACIA_PLANKS,
            Material.BIRCH_PLANKS,
            Material.DARK_OAK_PLANKS,
            Material.JUNGLE_PLANKS,
            Material.MANGROVE_PLANKS,
            Material.SPRUCE_PLANKS,
            Material.OAK_SLAB,
            Material.ACACIA_SLAB,
            Material.BIRCH_SLAB,
            Material.DARK_OAK_SLAB,
            Material.JUNGLE_SLAB,
            Material.MANGROVE_SLAB,
            Material.SPRUCE_SLAB,
            Material.OAK_FENCE,
            Material.ACACIA_FENCE,
            Material.BIRCH_FENCE,
            Material.DARK_OAK_FENCE,
            Material.JUNGLE_FENCE,
            Material.MANGROVE_FENCE,
            Material.SPRUCE_FENCE,
            Material.OAK_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.MANGROVE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.OAK_STAIRS,
            Material.ACACIA_STAIRS,
            Material.BIRCH_STAIRS,
            Material.DARK_OAK_STAIRS,
            Material.JUNGLE_STAIRS,
            Material.MANGROVE_STAIRS,
            Material.SPRUCE_STAIRS,
            Material.OAK_LEAVES,
            Material.ACACIA_LEAVES,
            Material.BIRCH_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES,
            Material.AZALEA_LEAVES,
            Material.COMPOSTER,
            Material.BEEHIVE,
            Material.TARGET,
            Material.BOOKSHELF,
            Material.LECTERN,
            Material.BEE_NEST,
            Material.BLACK_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
            Material.GREEN_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.LIME_WOOL,
            Material.MAGENTA_WOOL,
            Material.ORANGE_WOOL,
            Material.PINK_WOOL,
            Material.PURPLE_WOOL,
            Material.RED_WOOL,
            Material.WHITE_WOOL,
            Material.YELLOW_WOOL,
            Material.BLACK_CARPET,
            Material.BLUE_CARPET,
            Material.BROWN_CARPET,
            Material.CYAN_CARPET,
            Material.GRAY_CARPET,
            Material.GREEN_CARPET,
            Material.LIGHT_BLUE_CARPET,
            Material.LIGHT_GRAY_CARPET,
            Material.LIME_CARPET,
            Material.MAGENTA_CARPET,
            Material.ORANGE_CARPET,
            Material.PINK_CARPET,
            Material.PURPLE_CARPET,
            Material.RED_CARPET,
            Material.WHITE_CARPET,
            Material.YELLOW_CARPET,
            Material.DRIED_KELP_BLOCK,
            Material.HAY_BLOCK,
            Material.SCAFFOLDING
    ));

    private HashSet<EntityDamageEvent.DamageCause> validDeathCauses = new HashSet<>(Arrays.asList(EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.FIRE));

    public PyromaniacClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);

        scoringDescription = "Bréler des blocs dans la base adverse";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PYROMANIAC;
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        if (!validBurnableBlocks.contains(block.getType())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (int) (scoringCoef * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        if (!validBurnableBlocks.contains(block.getType())) return;
        if (!data().playersTeam.get(playerWhoBreaks.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, (int) (malusCoef * ScoringHelper.scoreAltitudeCoefficient(block.getLocation().getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        EntityDamageEvent.DamageCause deathCause = killed.getLastDamageCause().getCause();
        // Mob & co is not a valid death, we don't want trapper to earn free points
        if (!validDeathCauses.contains(deathCause)) return;
        // the trapper is too far away
        if (owner.getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 100) return;
        // the dead is in the trapper team
        if (data().playersTeam.get(killed.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), 50, ScoreType.FLAT);
    }
}
