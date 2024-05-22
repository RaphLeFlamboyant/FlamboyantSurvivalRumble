package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashSet;

public class PeasantClass extends APlayerClass implements Listener {
    private static final float plantingAmountReward = 1;
    private static final float fertilizeAmountReward = 1;
    private static final float harvestAmountReward = 5;
    private static final float makingFarmlandAmountReward = 0.2f;
    private static final float breedingAmountReward = 20;
    private static final float cattleKillAmountReward = 2;
    private final String ownerTeam;

    private float leftovers;

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
    private HashSet<EntityType> validEntityType = new HashSet<>(Arrays.asList(
            EntityType.PIG,
            EntityType.COW,
            EntityType.CHICKEN,
            EntityType.SHEEP,
            EntityType.RABBIT
    ));

    public PeasantClass(Player owner) {
        super(owner);
        ownerTeam = data().getPlayerTeam(owner);

        scoringDescription = "Travailler comme un paysan";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PEASANT;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        BlockFertilizeEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        EntityBreedEvent.getHandlerList().unregister(this);
        EntityDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() != owner) return;
        var fertilizedBlocks = event.getBlocks();

        for (var blockState : fertilizedBlocks) {
            if (validFarmingMaterial.contains(blockState.getType()))
                earnMoney(fertilizeAmountReward);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() != owner) return;
        var brokenBlock = event.getBlock();

        if (validFarmingMaterial.contains(brokenBlock.getType()))
            earnMoney(harvestAmountReward);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != owner) return;
        if (!event.hasItem()) return;

        if (event.getItem().getType().toString().contains("HOE")) {
            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() != Material.GRASS_BLOCK && event.getClickedBlock().getType() != Material.DIRT) return;

            earnMoney(makingFarmlandAmountReward);
        }
        else if (validPlantationMaterial.contains(event.getItem().getType())) {
            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() != Material.FARMLAND) return;

            earnMoney(plantingAmountReward);
        }
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if (event.getBreeder() != owner) return;
        if (!validEntityType.contains(event.getEntityType())) return;

        earnMoney(breedingAmountReward);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != owner) return;
        if ( !validEntityType.contains(event.getEntity().getType())) return;

        earnMoney(cattleKillAmountReward);
    }

    private void earnMoney(float amount) {
        amount += leftovers;
        leftovers = amount % 1f;

        GameManager.getInstance().addAddMoney(ownerTeam, (int) amount);
    }
}
