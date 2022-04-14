package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AQuestComponent {
    private Quest owner;
    protected Player player;
    protected String subQuestMessage;

    public AQuestComponent(Player player) {
        this.player = player;
    }

    public void startQuest(Quest owner) {
        this.owner = owner;
    }

    public String getSubQuestMessage() {
        return " - " + subQuestMessage;
    }

    protected void stopQuest() {
        player.sendMessage(ChatUtils.feedback(ChatColor.BLUE + "[Validé]" + ChatUtils.feedbackColor + getSubQuestMessage()));
        owner.questComponentValidated(this);
    }
}
