package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class TargetPlayerSpecificDeathQuestTask extends TargetPlayerDeathQuestTask {
    private List<EntityDamageEvent.DamageCause> deathCauses;

    public TargetPlayerSpecificDeathQuestTask(Quest ownerQuest, Player playerToDie, int quantity, List<EntityDamageEvent.DamageCause> deathCauses) {
        super(ownerQuest, playerToDie, quantity);

        this.deathCauses = deathCauses;
    }

    @Override
    public String getSubQuestMessage() {
        if (subQuestMessage == null) {
            String subject = playerToDie == this.player ? " - Mourrez" : " - Le joueur " + playerToDie.getDisplayName() + " doit mourir";
            subject += "\n  Causes du décés pour valier la mission : [";

            for (EntityDamageEvent.DamageCause cause : deathCauses) {
                subject += cause + ", ";
            }
            subQuestMessage = subject;
        }

        return super.getSubQuestMessage();
    }

    @Override
    protected boolean checkDeathCondition(PlayerDeathEvent event) {
        return deathCauses.contains(event.getEntity().getLastDamageCause().getCause());
    }
}
