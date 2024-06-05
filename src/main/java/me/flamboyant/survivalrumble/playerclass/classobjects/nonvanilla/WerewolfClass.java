package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WerewolfClass extends ANonVanillaClass implements Listener {
    private BukkitTask currentTask;
    private List<EntityPotionEffectEvent.Cause> effectCausesToCancel = Arrays.asList(EntityPotionEffectEvent.Cause.ARROW, EntityPotionEffectEvent.Cause.POTION_DRINK, EntityPotionEffectEvent.Cause.POTION_SPLASH);
    private HashMap<PotionEffectType, Integer> werewolfPotionEffects = new HashMap<PotionEffectType, Integer>() {{
        put(PotionEffectType.INCREASE_DAMAGE, 1);
        put(PotionEffectType.NIGHT_VISION, 1);
        put(PotionEffectType.SPEED, 1);
    }};

    public WerewolfClass(Player owner) {
        super(owner);
    }

    @Override
    protected String getAbilityDescription() {
        return "Tu obtiens les effets FORCE 1, SPEED 1 et NIGHT VISION la nuit. Ces effets ne disparaissent pas en "
                + "consommant un seau de lait. Les potions affectants ces effets ne fonctionneront pas sur toi "
                + "(que ça soit une potion FORCE 2 ou une potion WEAKNESS).";
    }

    @Override
    protected String getScoringDescription() {
        return "Vous gagnez 250 points part kill de nuit.";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WEREWOLF;
    }

    @Override
    public int getScoreMalus() {
        return -2500;
    }

    @Override
    public void enableClass() {
        super.enableClass();

        long time = owner.getWorld().getTime();

        if (time > 12000) {
            time -= 12000;
            enableEffects((int) (20 * 60 * 10 * (12000 - time) / 12000));
        }

        currentTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> switchState(), 20L * 60 * 10 * (12000 - time) / 12000);
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        super.disableClass();
        disableEffects();

        Bukkit.getScheduler().cancelTask(currentTask.getTaskId());

        TimeSkipEvent.getHandlerList().unregister(this);
        PlayerItemConsumeEvent.getHandlerList().unregister(this);
        EntityPotionEffectEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    private void enableEffects(int durationTicks) {
        for (PotionEffectType effectType : werewolfPotionEffects.keySet())
            owner.addPotionEffect(new PotionEffect(effectType, durationTicks, werewolfPotionEffects.get(effectType), false, false, true));
    }

    private void disableEffects() {
        for (PotionEffectType effectType : werewolfPotionEffects.keySet())
            owner.removePotionEffect(effectType);
    }

    private void switchState() {
        long worldTime = owner.getWorld().getTime();
        if (worldTime < 12000) {
            disableEffects();
        }
        else {
            worldTime -= 12000;
            enableEffects((int) (20 * 60 * 10 * (12000 - worldTime) / 12000));
        }

        currentTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> switchState(), 20L * 60 * 10);
    }

    @EventHandler
    private void onTimeSkip(TimeSkipEvent event) {
        long newTime = event.getSkipAmount() + owner.getWorld().getTime();
        Bukkit.getScheduler().cancelTask(currentTask.getTaskId());
        if (newTime < 12000) {
            disableEffects();
        }
        else {
            newTime -= 12000;
            enableEffects((int) (20 * 60 * 10 * (12000 - newTime) / 12000));
        }

        currentTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> switchState(), 20L * 60 * 10 * (12000 - newTime) / 12000);
    }

    @EventHandler
    private void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getItem().getType() != Material.MILK_BUCKET) return;

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            long worldTime = owner.getWorld().getTime();
            if (worldTime >= 12000) {
                worldTime -= 12000;
                enableEffects((int) (20 * 60 * 10 * (12000 - worldTime) / 12000));
            }
        }, 1L);
    }

    @EventHandler
    private void onPotionEffect(EntityPotionEffectEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!effectCausesToCancel.contains(event.getCause())) return;
        if (!werewolfPotionEffects.containsKey(event.getOldEffect().getType())) return;

        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer() != owner) return;
        long worldTime = owner.getWorld().getTime();
        if (worldTime >= 12000) {
            worldTime -= 12000;
            enableEffects((int) (20 * 60 * 10 * (12000 - worldTime) / 12000));
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        if (killed.getKiller() != owner) return;
        if (owner.getWorld().getTime() < 12000) return;
        String ownerTeam = data().getPlayerTeam(owner);
        if (data().getPlayerTeam(killed).equals(ownerTeam)) return;

        GameManager.getInstance().addAddMoney(ownerTeam, 250);
    }
}
