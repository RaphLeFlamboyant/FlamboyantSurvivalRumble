package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.AssassinClassData;
import me.flamboyant.survivalrumble.data.classes.NinjaClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitTask;

import java.util.stream.Collectors;

public class NinjaClass extends ANonVanillaClass implements Listener {
    private static final int cooldownDuration = 15;
    private static final int softGainDistance = 32;
    private static final int hardGainDistance = 10;
    private static final float softGain = 0.2f;
    private static final float hardGain = 1f;
    private static final int checkInterval = 1;

    private Player targetedPlayer;
    private Location overworldLocationBeforePortal;
    private Location netherLocationBeforePortal;

    private NinjaClassData classData;
    private BukkitTask intervalTask;
    private int cooldown;
    private float leftovers = 0f;

    public NinjaClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.NINJA;
    }

    @Override
    public PlayerClassData buildClassData() { return new NinjaClassData(); }

    @Override
    protected String getAbilityDescription() {
        return "Utiliser une boussole te donne la position d'un joueur au hasard pendant 15 mn. "
                + "Réutiliser la boussole pendant ce délai rafraichit la position du joueur."
                + "Boire une fiole d'eau te rend invisible pendant une minute. "
                + "Durant cette invisibilité tu ne peux ni taper, ni poser de bloc et ni en casser. "
                + "Si tu prends un coup tu redeviens visible. Cooldown 15 minutes.";
    }

    @Override
    protected String getScoringDescription() {
        return "Être proche d'adversaires rapporte des points.";
    }

    @Override
    public int getScoreMalus() {
        return -1500;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (NinjaClassData) data().getPlayerClassData(owner);
        System.out.println("Ninja enabling with target id = " + classData.targetPlayerId);
        if (classData.targetPlayerId != null)
            targetedPlayer = Bukkit.getPlayer(classData.targetPlayerId);

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            targetedPlayer = null;
            classData.targetPlayerId = null;
        }, 15 * 60 * 20);

        Common.server.getPluginManager().registerEvents(this, Common.plugin);

        intervalTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    @Override
    public void disableClass() {
        super.disableClass();

        PlayerInteractEvent.getHandlerList().unregister(this);
        EntityPortalEnterEvent.getHandlerList().unregister(this);
        PlayerItemConsumeEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);

        Bukkit.getScheduler().cancelTask(intervalTask.getTaskId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (event.getPlayer() != owner) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem().getType() == Material.COMPASS) {
            handleCompass(event.getItem());
            var item = event.getItem();
            var meta = item.getItemMeta();
            meta.setDisplayName(targetedPlayer.getDisplayName());
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        if (event.getEntity() != targetedPlayer) return;
        if (event.getLocation().getWorld().getName().equals("world"))
            overworldLocationBeforePortal = event.getLocation();
        if (event.getLocation().getWorld().getName().equals("world_nether"))
            netherLocationBeforePortal = event.getLocation();
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getItem().getType() != Material.POTION) return;
        if (((PotionMeta)event.getItem().getItemMeta()).getBasePotionType() != PotionType.WATER) return;

        if (cooldown > 0) {
            owner.sendMessage(ChatColors.feedback("Cooldown : " + cooldown + "mn"));
            return;
        }

        cooldown = cooldownDuration;
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> onCooldownTick(), 60 * 20);

        var effect = new PotionEffect(PotionEffectType.INVISIBILITY, 60 * 20, 1, false, false, true);
        owner.addPotionEffect(effect);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() != owner) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) return;
        if (cooldown != cooldownDuration) return;
        if (!owner.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

        owner.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() != owner) return;
        if (cooldown != cooldownDuration) return;
        if (!owner.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEntity(BlockPlaceEvent event) {
        if (event.getPlayer() != owner) return;
        if (cooldown != cooldownDuration) return;
        if (!owner.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEntity(BlockBreakEvent event) {
        if (event.getPlayer() != owner) return;
        if (cooldown != cooldownDuration) return;
        if (!owner.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

        event.setCancelled(true);
    }

    private void onCooldownTick() {
        if (--cooldown > 0) {
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> onCooldownTick(), 60 * 20);
        }
    }

    private void handleCompass(ItemStack compass) {
        System.out.println("NinjaClass.handleCompass");
        if (targetedPlayer == null)
            selectTarget();

        var targetLocation = targetedPlayer.getLocation();
        var targetWorldName = targetLocation.getWorld().getName();
        var ownerWorld = owner.getWorld().getName();

        if (!ownerWorld.equals(targetWorldName)) {
            owner.sendMessage("La cible est dans la dimension " + targetWorldName);

            if (!targetWorldName.equals(UsefulConstants.endWorldName)) {
                if (ownerWorld.equals(UsefulConstants.overworldName))
                    targetLocation = overworldLocationBeforePortal;
                if (ownerWorld.equals(UsefulConstants.netherWorldName))
                    targetLocation = netherLocationBeforePortal;
            }
        }

        if (ownerWorld.equals(UsefulConstants.netherWorldName) && (targetWorldName.equals(UsefulConstants.netherWorldName) || netherLocationBeforePortal != null)){
            Location lodeStoneLocation = new Location(targetLocation.getWorld(), targetLocation.getBlockX(), 0, targetLocation.getBlockZ());
            lodeStoneLocation.getBlock().setType(Material.LODESTONE);

            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            compassMeta.setLodestone(lodeStoneLocation);
            compassMeta.setLodestoneTracked(true);
            compass.setItemMeta(compassMeta);
        }
        else {
            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            compassMeta.setLodestone(null);
            compassMeta.setLodestoneTracked(false);
            compass.setItemMeta(compassMeta);
            if (targetLocation != null) owner.setCompassTarget(targetLocation);
        }
    }

    private void selectTarget() {
        var ownerTeam = data().getPlayerTeam(owner);
        var allPlayers = Common.server.getOnlinePlayers()
                .stream()
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL && data().getPlayerTeam(p) != ownerTeam)
                .collect(Collectors.toList());

        targetedPlayer = allPlayers.get(Common.rng.nextInt(allPlayers.size()));
        classData.targetPlayerId = targetedPlayer.getUniqueId();

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            targetedPlayer = null;
            classData.targetPlayerId = null;
        }, 15 * 60 * 20);
    }

    private void updateScoring() {
        if (!owner.getWorld().getName().equals(targetedPlayer.getWorld().getName())) return;

        var ownerTeam = data().getPlayerTeam(owner);
        var ownerTeamHqLocation = data().getHeadquarterLocation(ownerTeam);
        var gain = 0f;
        for (Player player : Common.server.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) continue;
            var playerTeam = data().getPlayerTeam(player);
            if (playerTeam == ownerTeam || playerTeam == null) continue;

            var distance = player.getLocation().distance(ownerTeamHqLocation);
            if (distance > softGainDistance) continue;

            if (distance <= hardGainDistance)
                gain += hardGain;
            else if (gain == 0f)
                gain = softGain;
        }

        gain += leftovers;
        leftovers = gain % 1f;
        GameManager.getInstance().addAddMoney(ownerTeam, (int) gain);
    }
}
