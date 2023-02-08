package me.flamboyant.survivalrumble.quests.pools;

import me.flamboyant.survivalrumble.quests.IQuest;
import org.bukkit.entity.Player;

public interface IQuestPool {
    IQuest startRandomQuest(Player p);
    void questFinished(Player p);
    void showQuestMessage(Player p);
}
