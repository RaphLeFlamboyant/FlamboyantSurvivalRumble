package me.flamboyant.survivalrumble.quests.component;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TargetPlayerDeathQuestComponent extends APlayerDeathQuestComponent {
    protected Player playerToDie;
    protected int quantity;

    public TargetPlayerDeathQuestComponent(Player player, Player playerToDie, int quantity) {
        super(player);
        this.playerToDie = playerToDie;
        this.quantity = quantity;
    }

    @Override
    public String getSubQuestMessage() {
        if (subQuestMessage == null) {
            String subject = playerToDie == this.player ? "Mourrez" : "Le joueur " + playerToDie.getDisplayName() + " doit mourir";
            subQuestMessage = subject + " de n'importe quelle faéon";
        }

        return super.getSubQuestMessage();
    }

    protected boolean checkDeathCondition(PlayerDeathEvent event) {
        return true;
    }

    @Override
    protected void doOnEvent(PlayerDeathEvent event) {
        if (event.getEntity().getPlayer() != playerToDie) return;

        if (checkDeathCondition(event) && --quantity <= 0) {
            stopQuest();
        }
    }
}
