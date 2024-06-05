package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.MaterialHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ShamelessClass extends AAttackClass implements Listener {
    private static final float plantingAmountReward = 10;
    private static final float fertilizeAmountReward = 3;
    private static final float harvestAmountReward = 20;
    private static final float makingFarmlandAmountReward = 1f;
    private static final float breedingAmountReward = 200;
    private static final float cattleKillAmountReward = 10;
    private static final float mobKillAmountReward = 5;

    private List<EntityType> badEntityTypes = Arrays.asList(EntityType.WITCH, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);
    private HashSet<Material> validFarmingMaterial = new HashSet<>(Arrays.asList(
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.WHEAT
    ));
    private HashSet<Material> validPlantationMaterial = new HashSet<>(Arrays.asList(
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOT_SEEDS,
            Material.WHEAT_SEEDS
    ));
    private HashSet<EntityType> cattleEntityType = new HashSet<>(Arrays.asList(
            EntityType.PIG,
            EntityType.COW,
            EntityType.CHICKEN,
            EntityType.SHEEP,
            EntityType.RABBIT
    ));

    public ShamelessClass(Player owner) {
        super(owner);

        scoringDescription = "Faire des actions de survie dans la base adverse";
    }

    @Override
    protected float getMalusRatio() {
        return 0.025f;
    }

    @Override
    protected double getValidationDistance() {
        return 32;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.SHAMELESS;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);

    }

    @Override
    public void disableClass() {
        BlockPlaceEvent.getHandlerList().unregister(this);
        CraftItemEvent.getHandlerList().unregister(this);
        BlockFertilizeEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        EntityBreedEvent.getHandlerList().unregister(this);
        EntityDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getPlayer() != owner) return;
        applyAmount(5f);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer() != owner) return;

        if (MaterialHelper.constructionMaterial.containsKey(event.getBlock().getType()))
            applyAmount(MaterialHelper.constructionMaterial.get(event.getBlock().getType()));
        else
            applyAmount(1f);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() != owner) return;
        var brokenBlock = event.getBlock();

        if (validFarmingMaterial.contains(brokenBlock.getType()))
            applyAmount(harvestAmountReward);
        else
            applyAmount(1f);
    }
    
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getWhoClicked() != owner) return;
        applyAmount(2f);
    }
    
    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() != owner) return;
        var fertilizedBlocks = event.getBlocks();

        for (var blockState : fertilizedBlocks) {
            if (validFarmingMaterial.contains(blockState.getType()))
                applyAmount(fertilizeAmountReward);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != owner) return;
        if (!event.hasItem()) return;

        if (event.getItem().getType().toString().contains("HOE")) {
            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() != Material.GRASS_BLOCK && event.getClickedBlock().getType() != Material.DIRT) return;

            applyAmount(makingFarmlandAmountReward);
        }
        else if (validPlantationMaterial.contains(event.getItem().getType())) {
            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() != Material.FARMLAND) return;

            applyAmount(plantingAmountReward);
        }
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if (event.getBreeder() != owner) return;
        if (!cattleEntityType.contains(event.getEntityType())) return;

        applyAmount(breedingAmountReward);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != owner) return;

        if (cattleEntityType.contains(event.getEntity().getType()))
            applyAmount(cattleKillAmountReward);
        if (badEntityTypes.contains(event.getEntity().getType()))
            applyAmount(mobKillAmountReward);
    }
}
