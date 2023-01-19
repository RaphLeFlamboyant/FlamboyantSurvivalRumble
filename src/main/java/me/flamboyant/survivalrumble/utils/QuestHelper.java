package me.flamboyant.survivalrumble.utils;

import me.flamboyant.survivalrumble.quests.pools.IQuestPool;
import me.flamboyant.survivalrumble.quests.pools.impl.ClassicQuestPool;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class QuestHelper {

    private static HashMap<QuestPoolType, IQuestPool> questPools = new HashMap<QuestPoolType, IQuestPool>() {{
        put(QuestPoolType.CLASSIC, new ClassicQuestPool());
    }};

    public static IQuestPool getQuestPool(QuestPoolType type) {
        return questPools.get(type);
    }

    public static void submitQuestOver(Player p) {
        for (IQuestPool pool : questPools.values()) {
            pool.questFinished(p);
        }
    }

    public static void showQuestMessage(Player p) {
        for (IQuestPool pool : questPools.values()) {
            pool.showQuestMessage(p);
        }
    }
}
