package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TargetPlayerDeathQuestTask extends APlayerDeathQuestTask {
    protected Player playerToDie;
    protected int quantity;

    public TargetPlayerDeathQuestTask(Quest ownerQuest, Player playerToDie, int quantity) {
        super(ownerQuest);
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
            stopQuest(true);
        }
    }
}
