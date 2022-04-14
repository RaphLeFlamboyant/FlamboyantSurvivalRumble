package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.CraftQuestComponent;
import me.flamboyant.survivalrumble.quests.component.KillMobQuestComponent;
import me.flamboyant.survivalrumble.quests.component.KillPlayerQuestComponent;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class WarriorClass extends APlayerClass {
    public WarriorClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Tue des adversaires";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Il est dangereux de se promener seul", null, 15, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_SWORD, 1))));
        questList.add(new Quest(this, owner, "Premier acte de vaillance", null, 40, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.PIG, 2),
                new KillMobQuestComponent(owner, EntityType.COW, 2),
                new KillMobQuestComponent(owner, EntityType.SHEEP, 2),
                new KillMobQuestComponent(owner, EntityType.CHICKEN, 2))));
        questList.add(new Quest(this, owner, "Se préparer au danger", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.LEATHER_BOOTS, 1),
                new CraftQuestComponent(owner, Material.STONE_SWORD, 1))));
        questList.add(new Quest(this, owner, "Occire les monstres", null, 100, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.ZOMBIE, 5),
                new KillMobQuestComponent(owner, EntityType.SKELETON, 5),
                new KillMobQuestComponent(owner, EntityType.SPIDER, 3))));
        questList.add(new Quest(this, owner, "Attirail de professionnel", null, 30, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_SWORD, 1),
                new CraftQuestComponent(owner, Material.SHIELD, 1))));
        questList.add(new Quest(this, owner, "Protéger les lieux", null, 100, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.CREEPER, 5),
                new KillMobQuestComponent(owner, EntityType.ENDERMAN, 1))));
        questList.add(new Quest(this, owner, "Pour les fréres d'armes", null, 40, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_SWORD, 3))));
        questList.add(new Quest(this, owner, "Une horde cauchemardesque", null, 400, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.ZOMBIFIED_PIGLIN, 30))));
        questList.add(new Quest(this, owner, "L'ultime préparation", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_SWORD, 1))));
        questList.add(new Quest(this, owner, "Par bravoure !", null, 400, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.PIGLIN, 20),
                new KillMobQuestComponent(owner, EntityType.PIGLIN_BRUTE, 5))));
        questList.add(new Quest(this, owner, "Défier la mort", null, 400, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.WITHER_SKELETON, 10),
                new KillMobQuestComponent(owner, EntityType.BLAZE, 10))));

        if (data().teams.size() > 1) {
            String ownerTeam = data().playersTeam.get(owner.getUniqueId());
            for (String team : data().playersByTeam.keySet()) {
                if (team.equals(ownerTeam) || data().playersByTeam.get(team).size() == 0) continue;
                UUID targetId = TeamHelper.getRandomPlayer(Arrays.asList(team), 1).get(0);
                Player target = owner.getServer().getPlayer(targetId);
                questList.add(new Quest(this, owner, "Déclarer la guerre", null, 750, 0, Arrays.asList(
                        new KillPlayerQuestComponent(owner, target, 1))));
            }
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WARRIOR;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killer == owner) {
            String teamName = data().playersTeam.get(owner.getUniqueId());
            if (!data().playersTeam.get(killed.getUniqueId()).equals(teamName)) {
                GameManager.getInstance().addScore(teamName, 250, ScoreType.FLAT);
            }
        }
    }
}
