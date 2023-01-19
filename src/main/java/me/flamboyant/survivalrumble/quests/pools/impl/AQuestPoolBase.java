package me.flamboyant.survivalrumble.quests.pools.impl;

import me.flamboyant.survivalrumble.quests.IQuest;
import me.flamboyant.survivalrumble.quests.pools.IQuestPool;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AQuestPoolBase implements IQuestPool {
    protected List<IQuest> questList = new ArrayList<>();
    private HashMap<Player, IQuest> pendingQuests = new HashMap<>();

    public AQuestPoolBase() {
        buildQuestList();
    }

    @Override
    public IQuest startRandomQuest(Player p) {
        if (questList.size() == 0) return null;
        if (pendingQuests.containsKey(p)) return null;

        IQuest picked = questList.get(Common.rng.nextInt(questList.size()));
        questList.remove(picked);
        pendingQuests.put(p, picked);
        picked.startQuest(p);

        return picked;
    }

    @Override
    public void questFinished(Player p) {
        if (!pendingQuests.containsKey(p)) {
            return;
        }

        questList.add(pendingQuests.get(p));
        pendingQuests.remove(p);
    }

    @Override
    public void showQuestMessage(Player p) {
        if (!pendingQuests.containsKey(p))
            p.sendMessage(ChatUtils.feedback("Vous n'avez pas de quête en cours"));
        else
            pendingQuests.get(p).showQuestMessage();
    }

    protected abstract void buildQuestList();
}
