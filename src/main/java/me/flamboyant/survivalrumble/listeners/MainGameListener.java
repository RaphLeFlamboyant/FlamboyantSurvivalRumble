package me.flamboyant.survivalrumble.listeners;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.playerclass.managers.GameTimeManager;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.*;

public class MainGameListener implements Listener {
    public MainGameListener() {
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void initListener() {
        List<ItemStack> stuff = baseStuff(); // TODO : donner le stuff correspondant aux paramétres

        for (UUID playerId : data().players.values()) {
            Player player = Common.server.getPlayer(playerId);

            Location teamSpawnLocation = data().teamHeadquarterLocation.get(data().playersTeam.get(playerId));
            player.teleport(teamSpawnLocation);
            resetPlayerState(player);
            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            for (ItemStack item : stuff)
                inventory.addItem(item);
            player.updateInventory();
        }

        GameTimeManager gameTimeManager = new GameTimeManager();
        gameTimeManager.launchGameTimeManagement(Common.plugin);
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    private void resetPlayerState(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(3);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            Location loc = data().teamHeadquarterLocation.get(data().playersTeam.get(event.getPlayer().getUniqueId()));
            event.setRespawnLocation(loc);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.DEATH)) {
                System.out.println("TOTO TEST");
                playerClass.onPlayerDeathTrigger(event.getEntity(), event.getEntity().getKiller());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BREAK)) {
            playerClass.onBlockBreakTrigger(event.getPlayer(), event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        BlockData blockData = event.getBlock().getBlockData();
        handleBlockDestruction(blockData, blockLocation);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BURNED)) {
            playerClass.onBlockBurnedTrigger(event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        BlockData blockData = event.getBlock().getBlockData();
        handleBlockDestruction(blockData, blockLocation);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_EXPLOSION)) {
            playerClass.onExplosionTrigger(event.getBlock());
        }

        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            BlockData blockData = block.getBlockData();
            handleBlockDestruction(blockData, blockLocation);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            BlockData blockData = block.getBlockData();
            handleBlockDestruction(blockData, blockLocation);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_PLACE)) {
            playerClass.onBlockPlaceTrigger(event.getPlayer(), event.getBlock());
        }

        Location location = event.getBlock().getLocation();
        String concernedTeam = TeamHelper.getTeamHeadquarterName(location);

        if (concernedTeam == null)
            return;

        BlockData blockData = event.getBlock().getBlockData();
        if (MaterialHelper.scoringMaterial.containsKey(blockData.getMaterial())) {
            int scoreChange = MaterialHelper.scoringMaterial.get(blockData.getMaterial());

            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_MODIFIER)) {
                scoreChange = playerClass.onBlockModifierTrigger(scoreChange, blockData, location, concernedTeam);
            }

            int changes = (int) (scoreChange * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()));
            GameManager.getInstance().addScore(concernedTeam, changes, ScoreType.REVERSIBLE);
        }
    }

    private void handleBlockDestruction(BlockData blockData, Location blockLocation) {
        String concernedTeam = TeamHelper.getTeamHeadquarterName(blockLocation);

        if (concernedTeam == null)
            return;

        if (MaterialHelper.scoringMaterial.containsKey(blockData.getMaterial())) {
            int scoreChange = MaterialHelper.scoringMaterial.get(blockData.getMaterial());

            if (blockData instanceof Slab) {
                Slab slab = (Slab) blockData;
                if (slab.getType() == Slab.Type.DOUBLE)
                    scoreChange *= 2;
            }

            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_MODIFIER)) {
                scoreChange = playerClass.onBlockModifierTrigger(scoreChange, blockData, blockLocation, concernedTeam);
            }

            int changes = -(int) (scoreChange * ScoringHelper.scoreAltitudeCoefficient(blockLocation.getBlockY()));
            GameManager.getInstance().addScore(concernedTeam, changes, ScoreType.REVERSIBLE);
        }
    }

    private List<ItemStack> baseStuff() {
        if (data().selectedStuff == Material.SWEET_BERRIES)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.SWEET_BERRIES, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false)
            );

        if (data().selectedStuff == Material.COBBLESTONE)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.IRON_INGOT, 5, "Fer du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.REDSTONE, 64, "Poudre du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.OAK_LOG, 64, "Bois du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COAL, 32, "Charbon du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COBBLESTONE, 240, "Blocs du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COOKED_BEEF, 32, "Bouffe de pilote", Arrays.asList(), false, null, false, false)
            );

        if (data().selectedStuff == Material.IRON_PICKAXE)
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

        if (data().selectedStuff == Material.BAKED_POTATO)
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
