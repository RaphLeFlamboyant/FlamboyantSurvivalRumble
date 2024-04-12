package me.flamboyant.survivalrumble.gamecontrollers.assault;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AssaultManager implements Listener {
    private HashSet<Material> storingMaterial = Stream.of(Material.CHEST,
                    Material.BARREL, Material.TRAPPED_CHEST, Material.DISPENSER,
                    Material.DROPPER, Material.HOPPER, Material.ENDER_CHEST)
            .collect(Collectors.toCollection(HashSet::new));
    private HashSet<Material> armorEquipmentMaterials = Stream.of(Material.LEATHER_CHESTPLATE, Material.LEATHER_BOOTS,
                    Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_BOOTS,
                    Material.GOLDEN_HELMET, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_BOOTS,
                    Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_BOOTS,
                    Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_BOOTS,
                    Material.NETHERITE_HELMET, Material.NETHERITE_LEGGINGS, Material.TURTLE_HELMET, Material.ELYTRA)
            .collect(Collectors.toCollection(HashSet::new));
    private HashSet<EntityType> storingEntity = Stream.of(EntityType.CHEST_BOAT,
                    EntityType.MINECART_CHEST, EntityType.MINECART_HOPPER, EntityType.ITEM_FRAME,
                    EntityType.GLOW_ITEM_FRAME)
            .collect(Collectors.toCollection(HashSet::new));

    private HashMap<Player, Location> playerLastLocation = new HashMap<>();
    private HashMap<String, Location> assaultSpawnByTeamTarget = new HashMap<>();
    private List<IAssaultStepListener> assaultStepListeners = new ArrayList<>();
    private BukkitTask locationDefenderTask;

    private static AssaultManager instance;
    protected AssaultManager() {

    }

    public static AssaultManager getInstance() {
        if (instance == null) {
            instance = new AssaultManager();
        }

        return instance;
    }

    public void start() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.NATURAL_REGENERATION, false);
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        for (String teamName : data.getTeams()) {
            Player champion = data.getTeamChampion(teamName);
            Location teamHq = data.getHeadquarterLocation(teamName);

            champion.closeInventory();
            champion.setGameMode(GameMode.SPECTATOR);
            champion.teleport(teamHq);
            playerLastLocation.put(champion, teamHq);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, champion), 0);

            String targetTeamName = data.getTeamTargetTeam(teamName);
            Location targetTeamHq = data.getHeadquarterLocation(targetTeamName);
            Location spawnPoint = targetTeamHq.add(55, 0, 0);
            spawnPoint = new Location(spawnPoint.getWorld(), spawnPoint.getX(), spawnPoint.getWorld().getHighestBlockYAt(spawnPoint), spawnPoint.getZ());
            assaultSpawnByTeamTarget.put(targetTeamName, spawnPoint);
            for (Player player : data.getPlayers(teamName)) {
                if (player == champion) continue;

                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(spawnPoint);
                playerLastLocation.put(player, spawnPoint);
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, player), 0);
            }
        }

        locationDefenderTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> locationBasedActions(), 0, 20);

        ChampionPowerManager.getInstance().activateChampionsPowers();

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(locationDefenderTask.getTaskId());
        EntityDamageEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerInteractEntityEvent.getHandlerList().unregister(this);
        BlockDropItemEvent.getHandlerList().unregister(this);
        BlockDispenseArmorEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        PlayerItemDamageEvent.getHandlerList().unregister(this);
        PlayerPortalEvent.getHandlerList().unregister(this);
    }

    public void addListener(IAssaultStepListener assaultStepListener) {
        assaultStepListeners.add(assaultStepListener);
    }

    public void removeListener(IAssaultStepListener assaultStepListener) {
        assaultStepListeners.remove(assaultStepListener);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player deadPlayer = (Player) event.getEntity();
        if (event.getFinalDamage() < deadPlayer.getHealth()) return;

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String teamName = data.getPlayerTeam(deadPlayer);

        if (deadPlayer != data.getTeamChampion(teamName)) {
            String targetTeamName = data.getTeamTargetTeam(teamName);
            Location spawnPoint = assaultSpawnByTeamTarget.get(targetTeamName);

            resetPlayerState(deadPlayer);
            playerLastLocation.put(deadPlayer, spawnPoint);
            deadPlayer.setGameMode(GameMode.SPECTATOR);
            deadPlayer.teleport(spawnPoint);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, deadPlayer), 0);
        }
        else {
            if (data.getTeams().size() == 2) {
                ChampionPowerManager.getInstance().deactivateAllAndReset();
                data.removeTeam(teamName);
                Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + teamName + " est éliminée !"));
                String winTeamName = data.getTeams().get(0);
                Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + winTeamName + " a gagné !"));

                Location spawnPoint = data.getHeadquarterLocation(winTeamName);
                for (Player player : Common.server.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(spawnPoint);
                }

                stop();
                return;
            }

            ChampionPowerManager.getInstance().deactivateChampionPowers(data.getTeamChampion(teamName));
            for (Player player : data.getPlayers(teamName)) {
                player.setGameMode(GameMode.SPECTATOR);
            }

            Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + teamName + " est éliminée !"));

            String targetTeam = data.getTeamTargetTeam(teamName);
            Location spawnPoint = assaultSpawnByTeamTarget.get(targetTeam);

            String assaultTeam = data.getTeamAssaultTeam(teamName);
            data.removeTeam(teamName);

            for (Player player : data.getPlayers(assaultTeam)) {
                if (player == data.getTeamChampion(teamName))
                    continue;

                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(spawnPoint);
                playerLastLocation.put(player, spawnPoint);
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, player), 0);
            }

            for (var assaultListener : assaultStepListeners) {
                assaultListener.onTeamEliminated();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        onPlayerInteractTriggeredOnForbiddenBloc(event);
        onPlayerInteractWithForbiddenItem(event);
    }

    private void onPlayerInteractTriggeredOnForbiddenBloc(PlayerInteractEvent event) {
        if (TeamHelper.getTeamHeadquarterName(event.getClickedBlock().getLocation()) == null)
            return;
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        if ((!storingMaterial.contains(event.getMaterial())
                && !event.getMaterial().toString().contains("SHULKER_BOX"))
                || ((event.getMaterial() == Material.ENCHANTING_TABLE || event.getMaterial().toString().contains("ANVIL"))
                && data.getTeamChampion(data.getPlayerTeam(event.getPlayer())) != event.getPlayer()))
            return;

        event.setCancelled(true);
    }

    private void onPlayerInteractWithForbiddenItem(PlayerInteractEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        if (!armorEquipmentMaterials.contains(event.getMaterial())) return;
        if (data.getTeamChampion(data.getPlayerTeam(event.getPlayer())) != event.getPlayer()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!storingEntity.contains(event.getRightClicked().getType())) return;
        if (TeamHelper.getTeamHeadquarterName(event.getRightClicked().getLocation()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockDropItemEvent event) {
        if (!storingMaterial.contains(event.getBlockState().getBlockData().getMaterial())
                && !event.getBlockState().getBlockData().getMaterial().toString().contains("SHULKER_BOX"))
            return;
        if (TeamHelper.getTeamHeadquarterName(event.getBlockState().getLocation()) == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
        if (TeamHelper.getTeamHeadquarterName(event.getTargetEntity().getLocation()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getWhoClicked();
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        if (data.getTeamChampion(data.getPlayerTeam(player)) != player) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        event.setDamage(0);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        if (data.getTeamChampion(data.getPlayerTeam(event.getPlayer())) != event.getPlayer()) return;

        event.setCancelled(true);
    }

    private void locationBasedActions() {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        for (String teamName : data.getTeams()) {
            Player champion = data.getTeamChampion(teamName);
            if (TeamHelper.isLocationInHeadQuarter(champion.getLocation(), teamName)) {
                playerLastLocation.put(champion, champion.getLocation());
            } else {
                champion.teleport(playerLastLocation.get(champion));
            }

            if (champion.getLocation().getBlockY() < 64) {
                champion.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
            }

            for (Player player : data.getPlayers(teamName)) {
                if (player == champion) continue;

                if (player.getGameMode() == GameMode.SPECTATOR && TeamHelper.isLocationInHeadQuarter(player.getLocation(), teamName)) {
                    player.teleport(playerLastLocation.get(player));
                }
                else {
                    playerLastLocation.put(player, player.getLocation());
                }
            }
        }
    }

    private void countdownPlayer(Integer count, Player player) {
        if (count-- > 0) {
            int finalCount = count;
            player.sendTitle(count.toString(), "", 0, 19, 0);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(finalCount, player), 20);
            return;
        }

        Location location = player.getLocation();
        location = new Location(location.getWorld(), location.getX(), location.getWorld().getHighestBlockYAt(location), location.getZ());
        player.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
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
}
