package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TargetPlayerDeathByFoesQuestComponent extends TargetPlayerDeathQuestComponent {
    public TargetPlayerDeathByFoesQuestComponent(Player player, Player playerToDie, int quantity) {
        super(player, playerToDie, quantity);
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
