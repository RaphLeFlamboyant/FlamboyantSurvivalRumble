package me.flamboyant.survivalrumble.quests;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.quests.component.AQuestComponent;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private APlayerClass playerClass;
    private ArrayList<AQuestComponent> components = new ArrayList<>();
    protected Player owner;
    protected String questTitle;
    protected String questMessage;
    protected int flatPoints;
    protected int perfectPoints;

    public Quest(APlayerClass playerClass, Player owner, String questTitle, String questMessage, int flatPoints, int perfectPoints) {
        this.owner = owner;
        this.playerClass = playerClass;
        this.flatPoints = flatPoints;
        this.perfectPoints = perfectPoints;
        this.questTitle = questTitle;
        this.questMessage = questMessage;
    }

    public Quest(APlayerClass playerClass, Player owner, String questTitle, String questMessage, int flatPoints, int perfectPoints, List<AQuestComponent> components) {
        this.owner = owner;
        this.playerClass = playerClass;
        this.flatPoints = flatPoints;
        this.perfectPoints = perfectPoints;
        this.questTitle = questTitle;
        this.questMessage = questMessage;
        this.components = new ArrayList<>(components);
    }

    public void startQuest() {
        for (AQuestComponent component : components) {
            component.startQuest(this);
        }
        showQuestMessage();
    }

    public void showQuestMessage() {
        String corpus = "";
        for (AQuestComponent component : components) {
            corpus += component.getSubQuestMessage() + "\n";
        }

        String message = ChatUtils.questAnnouncement(questTitle, "" + (flatPoints == 0 ? perfectPoints : flatPoints), corpus);
        owner.sendMessage(message);
    }

    protected void stopQuest() {
        String concernedTeam = SurvivalRumbleData.getSingleton().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(concernedTeam, flatPoints, ScoreType.FLAT);
        GameManager.getInstance().addScore(concernedTeam, perfectPoints, ScoreType.PERFECT);
        playerClass.onQuestOver();
    }

    public void questComponentValidated(AQuestComponent component) {
        components.remove(component);

        if (components.size() == 0)
            stopQuest();
    }

    public void addQuestComponent(AQuestComponent component) {
        components.add(component);
    }
}
