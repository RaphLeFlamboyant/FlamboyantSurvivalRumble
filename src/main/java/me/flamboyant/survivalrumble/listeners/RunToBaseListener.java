package me.flamboyant.survivalrumble.listeners;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.managers.GameTimeManager;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RunToBaseListener implements Listener {
    private JavaPlugin plugin;
    private Server server;
    private World world;

    private List<UUID> playerReachedHQ = new ArrayList<>();
    private int pointsTick = 0;
    private BukkitTask checkPlayersPositionToHQTask = null;

    public RunToBaseListener(JavaPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        world = server.getWorld("world");
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    // Scheduling

    public void initListener() {
        Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 1, 0);

        List<ItemStack> stuff = baseStuff(); // TODO : donner le stuff correspondant aux paramétres

        for (UUID playerId : data().players.values()) {
            Player player = server.getPlayer(playerId);

            Location teamZeroSpawnLocation = data().teamSpawnLocation.get(data().playersTeam.get(playerId));
            player.teleport(teamZeroSpawnLocation == null ? zeroLocation : teamZeroSpawnLocation);
            resetPlayerState(player);
            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            for (ItemStack item : stuff)
                inventory.addItem(item);
            player.updateInventory();
        }

        world.setSpawnLocation(zeroLocation);
        world.setGameRule(GameRule.UNIVERSAL_ANGER, true);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setPVP(false);
        world.setDifficulty(Difficulty.EASY);

        scheduleCheckPlayersPositionToHQTask();
        GameTimeManager gameTimeManager = new GameTimeManager();
        gameTimeManager.launchGameTimeManagement(plugin);
    }

    public void scheduleCheckPlayersPositionToHQTask() {
        checkPlayersPositionToHQTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkPlayersPositionToHQ();
        }, 0, 20L);
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

    private void checkPlayersPositionToHQ() {
        Map<String, Integer> reachesByTeam = new HashMap<>();

        for (String teamName : data().teams)
            reachesByTeam.put(teamName, 0);

        for (UUID playerId : data().players.values()) {
            Player player = server.getPlayer(playerId);
            String teamName = data().playersTeam.get(playerId);
            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            double distance = hqLocation.distance(player.getLocation());

            if (distance < 50) {
                if (!playerReachedHQ.contains(playerId)) {
                    playerReachedHQ.add(playerId);
                    String message = ChatUtils.personalAnnouncement("TU ES ARRIVé", "Reste dans cette zone pour marquer quelques premiers points !");
                    player.sendMessage(message);
                }

                reachesByTeam.put(teamName, reachesByTeam.get(teamName) + 1);
            }
        }

        handleScore(reachesByTeam);
        setupNextStepIfAllTeamReached(reachesByTeam);
    }

    private void handleScore(Map<String, Integer> reachesByTeam) {
        if (pointsTick == 9) {
            pointsTick = 0;
            ScoreboardBricklayer sb = ScoreboardBricklayer.getSingleton();
            for (String teamName : data().teams) {
                GameManager.getInstance().addScore(teamName, reachesByTeam.get(teamName), ScoreType.FLAT);
            }

            data().saveData();
        } else
            pointsTick++;
    }

    private void setupNextStepIfAllTeamReached(Map<String, Integer> reachesByTeam) {
        for (String teamName : reachesByTeam.keySet()) {
            // System.out.println("Reaches for team " + teamName + " is " + reachesByTeam.get(teamName) + " and team size is " + data().playersByTeam.get(teamName).size());
            if (reachesByTeam.get(teamName) < data().playersByTeam.get(teamName).size())
                return;
        }

        String message = ChatUtils.generalAnnouncement("L'AVENTURE COMMENCE !",
                "Chaque joueur a atteint la base de son équipe ! Vous pouvez "
                        + "de nouveau poser et casser des blocs. Marquez des points en complétant votre quéte principale ou en posant des blocs de "
                        + "construction dans votre base (plus vous étes proches de la couche 64 plus éa marque de points). "
                        + "Pour détecter les bases adverses, utilisez une boussole !");
        for (UUID playerId : data().playersTeam.keySet()) {
            Player player = server.getPlayer(playerId);
            player.sendMessage(message);
            player.setGameMode(GameMode.SURVIVAL);
        }

        PlayerClassMechanicsHelper.getSingleton().enablePlayerClasses();

        Bukkit.getScheduler().cancelTask(checkPlayersPositionToHQTask.getTaskId());
        world.setGameRule(GameRule.KEEP_INVENTORY, false);
        world.setPVP(true);
        world.setDifficulty(Difficulty.HARD);

        unregisterEvents();

        MainGameListener listener = new MainGameListener(plugin, server);
        server.getPluginManager().registerEvents(listener, plugin);
        listener.initListener();
    }

    // Listening

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    public void unregisterEvents() {
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    // Utils

    private List<ItemStack> baseStuff() {
        if (data().selectedStuff == Material.BEEF)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.STONE_AXE, 1, "Hache du schlag", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.ACACIA_BOAT, 1, "Bateau du schlag", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.WATER_BUCKET, 1, "Flotte pour schlag", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.SWEET_BERRIES, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.TORCH, 16, "Lumiére du schlag", Arrays.asList(), false, null, false, false)
            );

        if (data().selectedStuff == Material.ENDER_PEARL)
            return Arrays.asList(
                    ItemHelper.generatePotion(PotionType.SPEED, false, false, true, "Pour courir plus vite ;)", Arrays.asList(), true),
                    ItemHelper.generatePotion(PotionType.SPEED, false, false, true, "Pour courir plus vite ;)", Arrays.asList(), true),
                    ItemHelper.generateItem(Material.ENDER_PEARL, 8, "Téléportation du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.ACACIA_BOAT, 1, "Bateau du pilote", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.GOLDEN_APPLE, 5, "Bouffe de pilote", Arrays.asList(), false, null, false, false)
            );

        if (data().selectedStuff == Material.IRON_PICKAXE)
            return Arrays.asList(
                    ItemHelper.generateItem(Material.IRON_PICKAXE, 1, "Pioche du nain", Arrays.asList(), true, Enchantment.DIG_SPEED, 5, false, false),
                    ItemHelper.generateItem(Material.IRON_SHOVEL, 1, "Pelle du nain", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.IRON_PICKAXE, 1, "L'autre pioche du nain", Arrays.asList(), true, Enchantment.DURABILITY, 5, false, false),
                    ItemHelper.generateItem(Material.ACACIA_BOAT, 1, "Bateau du nain", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.WATER_BUCKET, 1, "Flotte pour nain", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.COOKIE, 32, "Bouffe de nain", Arrays.asList(), false, null, false, false),
                    ItemHelper.generateItem(Material.TORCH, 64, "Lumiére du nain", Arrays.asList(), false, null, false, false)
            );

        return Arrays.asList(
                ItemHelper.generateItem(Material.ACACIA_BOAT, 1, "Bateau du schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.WATER_BUCKET, 1, "Flotte pour schlag", Arrays.asList(), false, null, false, false),
                ItemHelper.generateItem(Material.SWEET_BERRIES, 64, "Bouffe de schlag", Arrays.asList(), false, null, false, false)
        );
    }
}