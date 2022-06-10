package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CheerleaderClass extends APlayerClass {
    public CheerleaderClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "étre é moins de 50 blocs d'un allié qui fait un kill";
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = SurvivalRumbleData.getSingleton().playersTeam.get(owner.getUniqueId());
        questList.add(new Quest(this, owner, "Soutenir ses alliés", null, 25, 0, Arrays.asList(
                new HQProximityQuestComponent(owner, 5 * 60, 25, true, ownerTeam))));
        questList.add(new Quest(this, owner, "Pour une tenue chaude", null, 75, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.WHITE_WOOL, 16))));
        questList.add(new Quest(this, owner, "Ah mince j'sais pas faire de pull", null, 25, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.WHITE_WOOL, 16))));
        questList.add(new Quest(this, owner, "Pour mes alliés", null, 150, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_BOOTS, 3),
                new CraftQuestComponent(owner, Material.IRON_CHESTPLATE, 3),
                new CraftQuestComponent(owner, Material.IRON_LEGGINGS, 3),
                new CraftQuestComponent(owner, Material.IRON_HELMET, 3))));
        questList.add(new Quest(this, owner, "Préparer l'assaut", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.GOLDEN_APPLE, 6))));
        questList.add(new Quest(this, owner, "Trouver un prétexte pour rester loin", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.BOW, 1))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Tenir le siége", null, 250, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 20 * 60, 50, true),
                    new AimOnEntityQuestComponent(owner, EntityType.ARROW, 10, EntityType.PLAYER, false))));
        }

        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        if (allies.size() > 0) {
            List<AQuestComponent> alliesKilled = new ArrayList<>();
            for (Player ally : allies) {
                alliesKilled.add(new TargetPlayerDeathByFoesQuestComponent(owner, ally, 1));
            }
            questList.add(new Quest(this, owner, "Voir ses fréres mourir", null, 750, 0, alliesKilled));
        }
        questList.add(new Quest(this, owner, "Protéger leur lieu de convalescence", null, 150, 0, Arrays.asList(
                new HQProximityQuestComponent(owner, 15 * 60, 40, true, ownerTeam),
                new KillMobQuestComponent(owner, EntityType.ZOMBIE, 25),
                new KillMobQuestComponent(owner, EntityType.SKELETON, 25))));
        questList.add(new Quest(this, owner, "La rage de vaincre", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_SWORD, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_BOOTS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_HELMET, 1))));


        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        if (otherTeams.size() > 0) {
            String selectedTeam = otherTeams.get((new Random()).nextInt(otherTeams.size()));
            List<AQuestComponent> ennemiesKilled = new ArrayList<>();
            ennemiesKilled.add(new ProximityToPlayersQuestComponent(owner, allies, 1000, 15 * 60, true));
            for (UUID target : data().playersByTeam.get(selectedTeam)) {
                ennemiesKilled.add(new KillPlayerQuestComponent(owner, owner.getServer().getPlayer(target), 1));
            }

            questList.add(new Quest(this, owner, "VENGEANCE !!", null, 3000, 0, ennemiesKilled));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CHEERLEADER;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (!data().playersTeam.get(killer.getUniqueId()).equals(ownerTeamName)) return;
        if (data().playersTeam.get(killed.getUniqueId()).equals(ownerTeamName)) return;
        if (owner.getLocation().getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 50) return;

        GameManager.getInstance().addScore(ownerTeamName, 125, ScoreType.FLAT);
    }
}
