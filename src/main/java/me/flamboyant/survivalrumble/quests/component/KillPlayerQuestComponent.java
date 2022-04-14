package me.flamboyant.survivalrumble.quests.component;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillPlayerQuestComponent extends TargetPlayerDeathQuestComponent {
    public KillPlayerQuestComponent(Player player, Player target, int quantity) {
        super(player, target, quantity);
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
