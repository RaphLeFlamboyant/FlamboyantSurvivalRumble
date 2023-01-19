package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillPlayerQuestTask extends TargetPlayerDeathQuestTask {
    public KillPlayerQuestTask(Quest ownerQuest, Player target, int quantity) {
        super(ownerQuest, target, quantity);
    }

    @Override
    public String getSubQuestMessage() {
        if (subQuestMessage == null) {
            subQuestMessage = "Tue " + quantity + " fois le joueur " + playerToDie.getDisplayName();
        }

        return super.getSubQuestMessage();
    }

    @Override
    protected boolean checkDeathCondition(PlayerDeathEvent event) {
        return event.getEntity().getKiller() == player;
    }
}
