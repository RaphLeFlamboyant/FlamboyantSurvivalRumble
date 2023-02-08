package me.flamboyant.survivalrumble.quests;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.tasks.AQuestTask;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.QuestHelper;
import me.flamboyant.survivalrumble.utils.ScoreType;
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
    protected int flatPoints;
    protected int perfectPoints;

    public Quest(String questTitle, int flatPoints, int perfectPoints) {
        this.flatPoints = flatPoints;
        this.perfectPoints = perfectPoints;
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
            String concernedTeam = SurvivalRumbleData.getSingleton().playersTeam.get(ownerPlayer.getUniqueId());
            GameManager.getInstance().addScore(concernedTeam, flatPoints, ScoreType.FLAT);
            GameManager.getInstance().addScore(concernedTeam, perfectPoints, ScoreType.PERFECT);
        }

        QuestHelper.submitQuestOver(ownerPlayer);
        ownerPlayer.sendMessage(ChatUtils.feedback("La quête [" + questTitle + "] est terminée et vous a apporté " + (flatPoints + perfectPoints) + " points !"));

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

        String message = ChatUtils.questAnnouncement(questTitle, "" + (flatPoints == 0 ? perfectPoints : flatPoints), corpus);
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
