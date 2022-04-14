package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArcherClass extends APlayerClass implements Listener {
    public ArcherClass(Player owner) {
        super(owner);

        scoringDescription = "Tirer des fléches sur les adversaires";
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        questList.add(new Quest(this, owner, "Survivre", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.STONE_AXE, 1),
                new KillMobQuestComponent(owner, EntityType.CHICKEN, 8))));
        questList.add(new Quest(this, owner, "Révélation", null, 100, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.SKELETON, 10),
                new GetItemQuestComponent(owner, Material.ARROW, 5),
                new GetItemQuestComponent(owner, Material.BOW, 1))));
        questList.add(new Quest(this, owner, "Défendre de loin", null, 50, 0, Arrays.asList(
                new AimOnEntityQuestComponent(owner, EntityType.ARROW, 2, EntityType.ZOMBIE, false),
                new AimOnEntityQuestComponent(owner, EntityType.ARROW, 2, EntityType.SKELETON, false),
                new AimOnEntityQuestComponent(owner, EntityType.ARROW, 2, EntityType.SPIDER, false),
                new AimOnEntityQuestComponent(owner, EntityType.ARROW, 2, EntityType.CREEPER, false))));
        questList.add(new Quest(this, owner, "Sa propre arme", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.BOW, 1))));
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "S'isoler pour apprendre'", null, 200, 0, Arrays.asList(
                    new BiomeVentureQuestComponent(owner,
                            Arrays.asList(Biome.FOREST,
                                    Biome.BIRCH_FOREST,
                                    Biome.OLD_GROWTH_BIRCH_FOREST,
                                    Biome.DARK_FOREST,
                                    Biome.WINDSWEPT_FOREST,
                                    Biome.JUNGLE,
                                    Biome.FLOWER_FOREST,
                                    Biome.SPARSE_JUNGLE,
                                    Biome.BAMBOO_JUNGLE,
                                    Biome.OLD_GROWTH_PINE_TAIGA,
                                    Biome.OLD_GROWTH_SPRUCE_TAIGA,
                                    Biome.TAIGA,
                                    Biome.SNOWY_TAIGA),
                            5 * 60),
                    new ProximityToPlayersQuestComponent(owner, allies, 200, 5 * 60, true))));
        }
        questList.add(new Quest(this, owner, "Préparer l'assaut", null, 150, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.CROSSBOW, 1),
                new VillagerTradeQuestComponent(owner, Material.ARROW, 64, false))));

        if (data().teams.size() > 0) {
            questList.add(new Quest(this, owner, "Observer l'environnement", null, 250, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 60, 70, true))));
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            Player target = null;
            double minDist = 9999999;
            for (Player ennemy : ennemies) {
                double dist = ennemy.getLocation().distance(owner.getLocation());
                if (dist < minDist) {
                    minDist = dist;
                    target = ennemy;
                }
            }
            questList.add(new Quest(this, owner, "Premier assassinat", null, 500, 0, Arrays.asList(
                    new KillPlayerQuestComponent(owner, target, 1))));
            questList.add(new Quest(this, owner, "S'attendre aux représailles", null, 150, 0, Arrays.asList(
                    new CraftQuestComponent(owner, Material.DISPENSER, 4),
                    new GetItemQuestComponent(owner, Material.LAVA_BUCKET, 4),
                    new GetItemQuestComponent(owner, Material.TARGET, 4))));
            questList.add(new Quest(this, owner, "Activer les piéges", null, 1000, 0, Arrays.asList(
                    new AimOnBlockQuestComponent(owner, EntityType.ARROW, 4, Material.TARGET),
                    new ProximityPlayerDeathQuestComponent(owner, Arrays.asList(EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK), 36, 2, ennemies))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ARCHER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.ARROW) return;
        if (event.getHitEntity() == null) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        Entity ety = event.getHitEntity();
        if (!(ety instanceof Player)) return;
        Player player = (Player) ety;
        if (data().playersTeam.get(owner.getUniqueId()).equals(data().playersTeam.get(player.getUniqueId()))) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, 40, ScoreType.FLAT);
    }
}
