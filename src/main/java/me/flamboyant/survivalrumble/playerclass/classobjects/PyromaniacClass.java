package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.stream.Collectors;

public class PyromaniacClass extends APlayerClass {
    public PyromaniacClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);

        scoringDescription = "Bréler des blocs dans la base adverse";
    }

    @Override
    protected void buildQuestList() {
        List<EntityDamageEvent.DamageCause> pyroCause = Arrays.asList(EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.LAVA);
        Random rng = new Random();
        questList.add(new Quest(this, owner, "Une substance dangereuse", null, 15, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, Arrays.asList(EntityDamageEvent.DamageCause.SUFFOCATION)))));
        questList.add(new Quest(this, owner, "Détruire l'élément toxique", null, 50, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.WATER_BUCKET, 1))));
        questList.add(new Quest(this, owner, "Les outils du salut", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.FLINT_AND_STEEL, 1),
                new CraftQuestComponent(owner, Material.FIRE_CHARGE, 3),
                new BucketFillQuestComponent(owner, Material.LAVA, 1))));
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        if (data().players.size() > 1) {
            List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
            if (allies.size() > 0) {
                questList.add(new Quest(this, owner, "Sauver les étres vivants", null, 150, 0, Arrays.asList(
                        new KillMobQuestComponent(owner, EntityType.ZOMBIE, 1),
                        new KillMobQuestComponent(owner, EntityType.CREEPER, 1),
                        new TargetPlayerSpecificDeathQuestComponent(owner,
                                allies.get(rng.nextInt(allies.size())),
                                1, pyroCause))));
            }
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            List<UUID> enemiesIDfromSpecificTeam = data().playersByTeam.get(otherTeams.get(rng.nextInt(otherTeams.size())));
            questList.add(new Quest(this, owner, "Transmettre la bonne parole", null, 500 * enemiesIDfromSpecificTeam.size(), 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner,
                            pyroCause,
                            16,
                            enemiesIDfromSpecificTeam.size(),
                            enemiesIDfromSpecificTeam.stream().map(pid -> owner.getServer().getPlayer(pid)).collect(Collectors.toList())))));
            questList.add(new Quest(this, owner, "Multiplier les options", null, 100, 0, Arrays.asList(
                    new CraftQuestComponent(owner, Material.CROSSBOW, 1),
                    new CraftQuestComponent(owner, Material.FIREWORK_ROCKET, 16))));
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Les protéger malgré eux", null, 500, 0, Arrays.asList(
                    new FireworkExplodesOnPlayerQuestComponent(owner, ennemies.get(rng.nextInt(ennemies.size())), 3))));
            questList.add(new Quest(this, owner, "Un fontaine ? Mais vous étes fous !!", null, 75, 0, Arrays.asList(
                    new KillMobQuestComponent(owner, EntityType.VILLAGER, 3))));
            questList.add(new Quest(this, owner, "La terre sainte", null, 50, 0, Arrays.asList(
                    new BiomeVentureQuestComponent(owner, Biome.NETHER_WASTES, 5 * 60))));
            questList.add(new Quest(this, owner, "Une légére obsession", null, 200, 0, Arrays.asList(
                    new BucketFillQuestComponent(owner, Material.LAVA, 10))));

            List<AQuestComponent> components = new ArrayList<>();
            for (Player ennemy : ennemies) {
                components.add(new TargetPlayerSpecificDeathQuestComponent(owner, ennemy, 1, pyroCause));
            }
            for (Player ally : allies) {
                components.add(new TargetPlayerSpecificDeathQuestComponent(owner, ally, 1, pyroCause));
            }
            questList.add(new Quest(this, owner, "Les sauver TOUS", null, 300 * ennemies.size() + 50 * allies.size(), 0, components));
            questList.add(new Quest(this, owner, "S'acharner sur les impies", null, 10, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, pyroCause, 16, 20, ennemies))));
            questList.add(new Quest(this, owner, "Mériter le salut", null, 3000, 0, Arrays.asList(
                    new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, pyroCause))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PYROMANIAC;
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (int) (20 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
    }
}
