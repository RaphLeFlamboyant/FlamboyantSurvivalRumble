package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WitchClass extends APlayerClass implements Listener {
    private List<PotionEffectType> positiveEffects = Arrays.asList(PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.HEAL, PotionEffectType.HEALTH_BOOST, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.INVISIBILITY, PotionEffectType.JUMP, PotionEffectType.NIGHT_VISION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.REGENERATION, PotionEffectType.SLOW_FALLING, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING);
    private List<PotionEffectType> negativeEffects = Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.HARM, PotionEffectType.SLOW);

    public WitchClass(Player owner) {
        super(owner);

        scoringDescription = "Splash une potion bénéfique sur des alliés rapporte un peu de points. Splash une potion maléfique sur des ennemis rapporte beaucoup de points";
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Victime des superstitions", null, 20, 0, Arrays.asList(
                    new TargetPlayerDeathByAlliesQuestComponent(owner, owner, 1))));
            questList.add(new Quest(this, owner, "S'isoler des siens", null, 100, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 100, 5 * 60, true))));
        }
        questList.add(new Quest(this, owner, "Un savoir insoupéonné", null, 40, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.BOOK, 1))));
        questList.add(new Quest(this, owner, "Partir en quéte d'ingrédients", null, 750, 0, Arrays.asList(
                new BiomeVentureQuestComponent(owner, Arrays.asList(Biome.NETHER_WASTES, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.BASALT_DELTAS, Biome.SOUL_SAND_VALLEY), 5 * 60),
                new GetItemQuestComponent(owner, Material.GHAST_TEAR, 1),
                new GetItemQuestComponent(owner, Material.BLAZE_ROD, 10),
                new GetItemQuestComponent(owner, Material.GUNPOWDER, 4),
                new GetItemQuestComponent(owner, Material.BROWN_MUSHROOM, 4),
                new GetItemQuestComponent(owner, Material.SPIDER_EYE, 2),
                new GetItemQuestComponent(owner, Material.NETHER_WART, 16))));
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Se réconcillier avec les siens", null, 50, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 20, 5 * 60, false))));
        }
        questList.add(new Quest(this, owner, "Construire sa hutte", null, 100, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.COBBLESTONE, 45, true),
                new PlaceBlocQuestComponent(owner, Material.OAK_LOG, 12, true),
                new PlaceBlocQuestComponent(owner, Material.OAK_STAIRS, 20, true),
                new PlaceBlocQuestComponent(owner, Material.GLASS_PANE, 2, true),
                new PlaceBlocQuestComponent(owner, Material.BREWING_STAND, 1, true))));
        List<Player> ennemies = new ArrayList<>();
        if (data().teams.size() > 1) {
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Faire fondre les ennemis", null, 500, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner,
                            Arrays.asList(EntityDamageEvent.DamageCause.MAGIC, EntityDamageEvent.DamageCause.POISON),
                            20, 1, ennemies))));
        }
        questList.add(new Quest(this, owner, "Détruire la magie ennemie", null, 150, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.BREWING_STAND, 1))));
        questList.add(new Quest(this, owner, "Une concurrence génante", null, 200, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.WITCH, 2))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Faire un carnage", null, 1500, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner,
                            Arrays.asList(EntityDamageEvent.DamageCause.MAGIC, EntityDamageEvent.DamageCause.POISON),
                            20, 4, ennemies))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WITCH;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if (event.getAffectedEntities().isEmpty()) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        int positivity = potionPositivity(event.getPotion());
        if (positivity == 0) return;


        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        int scoreDelta = 0;
        for (LivingEntity ety : event.getAffectedEntities()) {
            if (!data().playersTeam.containsKey(ety.getUniqueId())) continue;

            boolean isSameTeam = data().playersTeam.get(ety.getUniqueId()).equals(ownerTeamName);
            if (positivity > 0 && isSameTeam)
                scoreDelta += 10;
            else if (positivity < 0 && !isSameTeam)
                scoreDelta += 100;
        }

        GameManager.getInstance().addScore(ownerTeamName, scoreDelta, ScoreType.FLAT);
    }

    private int potionPositivity(ThrownPotion potion) {
        int positivity = 0;

        for (PotionEffect effect : potion.getEffects()) {
            if (positiveEffects.contains(effect.getType())) positivity++;
            else if (negativeEffects.contains(effect.getType())) positivity--;
        }

        return positivity;
    }
}
