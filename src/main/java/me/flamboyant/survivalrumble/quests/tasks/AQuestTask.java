package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AQuestTask {
    private Quest ownerQuest;
    protected Player player;
    protected String subQuestMessage;

    public AQuestTask(Quest ownerQuest) {
        this.ownerQuest = ownerQuest;
    }

    public void startQuest(Player player) {
        this.player = player;
    }

    public String getSubQuestMessage() {
        return " - " + subQuestMessage;
    }

    protected void stopQuest(boolean success) {
        if (success) {
            player.sendMessage(ChatUtils.feedback(ChatColor.BLUE + "[Validé]" + ChatUtils.feedbackColor + getSubQuestMessage()));
            ownerQuest.questComponentValidated();
        }
        player = null;
    }
}
