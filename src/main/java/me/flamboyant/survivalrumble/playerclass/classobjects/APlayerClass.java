package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.QuestPoolType;
import org.bukkit.entity.Player;

public abstract class APlayerClass {
    protected Player owner;
    protected String scoringDescription;
    private boolean firstAttempt = true;

    public APlayerClass(Player owner) {
        this.owner = owner;
    }

    protected SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public abstract PlayerClassType getClassType();

    public PlayerClassData buildClassData() { return new PlayerClassData(); }

    public QuestPoolType getQuestPoolTpye() {
        return QuestPoolType.CLASSIC ;
    }

    public void enableClass() {
        String message = getClassDescription();

        if (firstAttempt)
            owner.sendMessage(message);
        firstAttempt = false;
    }

    public void disableClass() {

    }

    protected String getClassDescription() {
        return ChatColors.personalAnnouncement("Ta classe : " + PlayerClassHelper.playerClassMetadata.get(getClassType()).getDisplayName(),
                "Ta façon principale de marquer des points est la suivante : " + scoringDescription);
    }

    public int getScoreMalus() { return 0; }
}
