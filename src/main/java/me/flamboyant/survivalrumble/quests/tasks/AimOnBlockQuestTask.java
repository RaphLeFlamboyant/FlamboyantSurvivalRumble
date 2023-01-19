package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AimOnBlockQuestTask extends AAimQuestTask {
    private Material target;

    public AimOnBlockQuestTask(Quest ownerQuest, EntityType entityTypeToUse, int quantity, Material target) {
        super(ownerQuest, entityTypeToUse, quantity);

        this.target = target;

        subQuestMessage = "Touche " + quantity + " fois un " + target + " é l'aide de " + entityTypeToUse;
    }

    protected boolean doOnEvent(ProjectileHitEvent event) {
        return event.getHitBlock() != null && event.getHitBlock().getType() == target;
    }
}
