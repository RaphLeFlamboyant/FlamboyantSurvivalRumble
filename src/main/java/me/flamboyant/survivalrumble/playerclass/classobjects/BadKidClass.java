package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BadKidClass extends APlayerClass implements Listener {
    public BadKidClass(Player owner) {
        super(owner);

        scoringDescription = "Tirer des oeufs ou des boules de neige sur les adversaires";
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        questList.add(new Quest(this, owner, "Personne ne veut jouer avec moi", null, 23, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.GRASS_BLOCK, 100))));
        questList.add(new Quest(this, owner, "Préparer une blague", null, 18, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.OAK_TRAPDOOR, 4))));
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Une farce dangereuse", null, 97, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, Arrays.asList(EntityDamageEvent.DamageCause.FALL), 40, 1, allies))));
            questList.add(new Quest(this, owner, "Chemin vers l'exil", null, 211, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 200, 10 * 60, true))));
        }
        questList.add(new Quest(this, owner, "Se faire passer pour le maéon", null, 153, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.BRICKS, 30, true))));
        questList.add(new Quest(this, owner, "Je suis comme vous", null, 51, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_SWORD, 1),
                new CraftQuestComponent(owner, Material.IRON_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.IRON_BOOTS, 1),
                new CraftQuestComponent(owner, Material.IRON_HELMET, 1),
                new CraftQuestComponent(owner, Material.IRON_LEGGINGS, 1))));

        if (allies.size() > 0) {
            List<AQuestComponent> alliesKilled = new ArrayList<>();
            for (Player ally : allies) {
                alliesKilled.add(new TargetPlayerDeathByFoesQuestComponent(owner, ally, 1));
            }
            questList.add(new Quest(this, owner, "Assaut désastreux", null, 752, 0, alliesKilled));
            questList.add(new Quest(this, owner, "Rejeté é jamais", null, 133, 0, Arrays.asList(
                    new TargetPlayerDeathByAlliesQuestComponent(owner, owner, 10))));
        }
        questList.add(new Quest(this, owner, "Des gadgets é gogo", null, 549, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DISPENSER, 8),
                new CraftQuestComponent(owner, Material.OBSERVER, 8),
                new CraftQuestComponent(owner, Material.REPEATER, 16),
                new CraftQuestComponent(owner, Material.COMPARATOR, 8),
                new CraftQuestComponent(owner, Material.REDSTONE_TORCH, 128),
                new CraftQuestComponent(owner, Material.TNT, 8))));
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Se venger des siens", null, 512, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION), 40, allies.size(), allies))));
        }
        ArrayList<String> foesTeamList = new ArrayList<>(data().teams);
        foesTeamList.remove(ownerTeam);
        if (foesTeamList.size() > 0) {
            questList.add(new Quest(this, owner, "Incarner la folie", null, 1864, 0, Arrays.asList(
                    new HQExplosionQuestComponent(owner, 100, foesTeamList))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BADKID;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL && event.getEntity().getType() != EntityType.EGG) return;
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
        GameManager.getInstance().addScore(ownerTeamName, 30, ScoreType.FLAT);
    }
}
