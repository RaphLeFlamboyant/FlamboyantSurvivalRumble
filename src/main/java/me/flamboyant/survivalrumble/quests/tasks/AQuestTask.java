package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.utils.ChatHelper;
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
            player.sendMessage(ChatHelper.feedback(ChatColor.BLUE + "[Validé]" + ChatColors.feedbackColor + getSubQuestMessage()));
            ownerQuest.questComponentValidated();
        }
        player = null;
    }
}
