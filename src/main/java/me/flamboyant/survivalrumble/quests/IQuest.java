package me.flamboyant.survivalrumble.quests;

import org.bukkit.entity.Player;

public interface IQuest {
    void startQuest(Player owner);
    Player getPlayer();
    void stopQuest();
    void showQuestMessage();
}
