package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class PyromaniacClass extends AAttackClass implements Listener {
    private static final float blocAmountReward = 8f;
    private static final float foesAwayMalusRatio = 0.25f;
    private static final double validFoesDistance = 50;

    private HashSet<Block> blocksPlacedByOwnerTeam = new HashSet<>();

    private HashSet<Material> invalidBurnableBlocks = new HashSet<>(Arrays.asList(
            Material.SHORT_GRASS,
            Material.TALL_GRASS,
            Material.VINE,
            Material.CAVE_VINES,
            Material.TWISTING_VINES,
            Material.WEEPING_VINES,
            Material.CAVE_VINES_PLANT,
            Material.TWISTING_VINES_PLANT,
            Material.WEEPING_VINES_PLANT,
            Material.GLOW_LICHEN,
            Material.AZALEA_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES,
            Material.ACACIA_LEAVES,
            Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.CHERRY_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.OAK_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.HANGING_ROOTS,
            Material.FERN,
            Material.LARGE_FERN,
            Material.DEAD_BUSH,
            Material.BIG_DRIPLEAF,
            Material.BIG_DRIPLEAF_STEM,
            Material.SMALL_DRIPLEAF,
            Material.SPORE_BLOSSOM,
            Material.BAMBOO,
            Material.BAMBOO_SAPLING,
            Material.SWEET_BERRY_BUSH,
            Material.PITCHER_PLANT,
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.ORANGE_TULIP,
            Material.PINK_TULIP,
            Material.RED_TULIP,
            Material.WHITE_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.TORCHFLOWER
    ));

    private HashSet<EntityDamageEvent.DamageCause> validDeathCauses = new HashSet<>(Arrays.asList(EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.FIRE));

    public PyromaniacClass(Player owner) {
        super(owner);

        scoringDescription = "Brûler des blocs dans la base adverse et tuer les ennemis par le feu";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PYROMANIAC;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockBurnEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (blocksPlacedByOwnerTeam.contains(event.getBlock())) {
            blocksPlacedByOwnerTeam.remove(event.getBlock());
            return;
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        var block = event.getBlock();

        if (blocksPlacedByOwnerTeam.contains(block)) {
            blocksPlacedByOwnerTeam.remove(block);
            return;
        }

        if (invalidBurnableBlocks.contains(block.getType())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().getPlayerTeam(owner);
        if (concernedTeamName == null || ownerTeamName.equals(concernedTeamName)) return;

        applyAmount(blocAmountReward);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (blocksPlacedByOwnerTeam.contains(event.getBlock())) {
            blocksPlacedByOwnerTeam.remove(event.getBlock());
            return;
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (blocksPlacedByOwnerTeam.contains(block)) {
                blocksPlacedByOwnerTeam.remove(block);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        var playerWhoPlaces = event.getPlayer();

        if (invalidBurnableBlocks.contains(block.getType())) return;
        var ownerTeam = data().getPlayerTeam(owner);
        if (!data().getPlayerTeam(playerWhoPlaces).equals(ownerTeam)) return;
        var location = block.getLocation();
        var concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || ownerTeam.equals(concernedTeamName)) return;

        blocksPlacedByOwnerTeam.add(block);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var killed = event.getEntity();

        EntityDamageEvent.DamageCause deathCause = killed.getLastDamageCause().getCause();
        if (!validDeathCauses.contains(deathCause)) return;
        if (owner.getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 100) return;
        if (data().getPlayerTeam(killed).equals(data().getPlayerTeam(owner))) return;

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), 100);
    }

    @Override
    protected float getMalusRatio() {
        return foesAwayMalusRatio;
    }

    @Override
    protected double getValidationDistance() {
        return validFoesDistance;
    }
}
