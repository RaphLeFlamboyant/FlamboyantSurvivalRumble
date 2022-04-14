package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

public class DefeatistClass extends APlayerClass {
    public DefeatistClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Mourir de la main d'un ennemi";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Oh non c'est cassé :(", null, 20, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_PICKAXE, 1),
                new DestroyItemQuestComponent(owner, Material.WOODEN_PICKAXE, 1))));
        questList.add(new Quest(this, owner, "Oh non éa fait mal :(", null, 40, 0, Arrays.asList(
                new TargetPlayerDeathByMobQuestComponent(owner, owner, 5, Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER, EntityType.ENDERMAN)))));
        questList.add(new Quest(this, owner, "Oh non c'est haut :(", null, 30, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 5, Arrays.asList(EntityDamageEvent.DamageCause.FALL)))));
        questList.add(new Quest(this, owner, "Oh non c'est lourd :(", null, 100, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, Arrays.asList(EntityDamageEvent.DamageCause.FALLING_BLOCK)))));
        questList.add(new Quest(this, owner, "Oh non éa bréle :(", null, 30, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 2, Arrays.asList(EntityDamageEvent.DamageCause.HOT_FLOOR)))));
        questList.add(new Quest(this, owner, "Oh non j'sais pas nager :(", null, 30, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 5, Arrays.asList(EntityDamageEvent.DamageCause.DROWNING)))));
        questList.add(new Quest(this, owner, "Oh non j'suis agoraphobe :(", null, 500, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, Arrays.asList(EntityDamageEvent.DamageCause.CRAMMING)))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Oh non ils sont lé :(", null, 250, 0, Arrays.asList(
                    new TargetPlayerDeathByFoesQuestComponent(owner, owner, 1))));
        }
        questList.add(new Quest(this, owner, "Bon y en a marre !", null, 500, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_SWORD, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_BOOTS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_HELMET, 1),
                new CraftQuestComponent(owner, Material.BUCKET, 1),
                new CraftQuestComponent(owner, Material.GOLDEN_APPLE, 3),
                new CraftQuestComponent(owner, Material.BOW, 1),
                new CraftQuestComponent(owner, Material.ARROW, 32),
                new GetItemQuestComponent(owner, Material.ENDER_PEARL, 2))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Oh non ils sont trop forts :(", null, 250, 0, Arrays.asList(
                    new TargetPlayerDeathByFoesQuestComponent(owner, owner, 1))));
        }
        if (data().playersByTeam.get(data().playersTeam.get(owner.getUniqueId())).size() > 1) {
            questList.add(new Quest(this, owner, "Oh non méme eux :(", null, 1000, 0, Arrays.asList(
                    new TargetPlayerDeathByAlliesQuestComponent(owner, owner, 1))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.DEFEATIST;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        if (killed == owner && killer != null) {
            String teamName = data().playersTeam.get(owner.getUniqueId());
            if (!data().playersTeam.get(killer.getUniqueId()).equals(teamName)) {
                GameManager.getInstance().addScore(teamName, 50, ScoreType.PERFECT);
            }
        }
    }
}
