package me.flamboyant.survivalrumble.quests;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.tasks.AQuestTask;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.QuestHelper;
import me.flamboyant.utils.ChatHelper;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Quest implements IQuest {
    private ArrayList<AQuestTask> components = new ArrayList<>();
    private int tasksValidated = 0;
    protected Player ownerPlayer;
    protected String questTitle;
    protected int questPrice;

    public Quest(String questTitle, int questPrice) {
        this.questPrice = questPrice;
        this.questTitle = questTitle;
    }

    public void setTasks(List<AQuestTask> components) {
        this.components.addAll(components);
    }

    public void startQuest(Player owner) {
        tasksValidated = 0;
        this.ownerPlayer = owner;
        for (AQuestTask component : components) {
            component.startQuest(owner);
        }
        showQuestMessage();
    }

    @Override
    public Player getPlayer() {
        return ownerPlayer;
    }

    public void stopQuest() {
        stopQuest(false);
    }

    protected void stopQuest(boolean success) {
        if (success) {
            SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
            String concernedTeam = data.getPlayerTeam(ownerPlayer);
            data.addMoney(concernedTeam, questPrice);
        }

        QuestHelper.submitQuestOver(ownerPlayer);
        ownerPlayer.sendMessage(ChatHelper.feedback("La quête [" + questTitle + "] est terminée et vous a apporté " + (questPrice) + " points !"));

        this.ownerPlayer = null;
    }

    public void showQuestMessage() {
        if (ownerPlayer == null) {
            Bukkit.getLogger().warning("Called Quest.showQuestMessage() but owner player is null for quest " + questTitle);
            return;
        }

        String corpus = "";
        for (AQuestTask component : components) {
            corpus += component.getSubQuestMessage() + "\n";
        }

        String message = ChatHelper.titledMessage(questTitle, "" + (questPrice) + "\n" + corpus);
        ownerPlayer.playSound(ownerPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        ownerPlayer.sendMessage(message);
    }

    public void questComponentValidated() {
        tasksValidated++;

        if (tasksValidated >= components.size())
            stopQuest(true);
    }

    public String getQuestTitle() { return questTitle; }
}
