package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TargetPlayerDeathByFoesQuestTask extends TargetPlayerDeathQuestTask {
    public TargetPlayerDeathByFoesQuestTask(Quest ownerQuest, Player playerToDie, int quantity) {
        super(ownerQuest, playerToDie, quantity);
    }

    @Override
    public String getSubQuestMessage() {
        if (subQuestMessage == null) {
            subQuestMessage = "Le joueur " + playerToDie.getDisplayName() + " doit mourir " + (quantity >= 0 ? (quantity + " fois ") : "") + "de la main d'un de ses ennemis";
        }

        return super.getSubQuestMessage();
    }

    @Override
    protected boolean checkDeathCondition(PlayerDeathEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        boolean conditionCheck = event.getEntity().equals(playerToDie);
        conditionCheck &= event.getEntity().getKiller() != null;
        conditionCheck &= !data.playersTeam.get(playerToDie.getUniqueId()).equals(data.playersTeam.get(event.getEntity().getKiller().getUniqueId()));

        return conditionCheck;
    }
}
