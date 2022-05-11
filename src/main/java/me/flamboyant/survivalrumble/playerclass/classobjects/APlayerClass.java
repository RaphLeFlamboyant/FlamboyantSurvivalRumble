package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class APlayerClass {
    public ArrayList<ScoringTriggerType> triggers = new ArrayList<ScoringTriggerType>();
    protected Player owner;
    protected ArrayList<Quest> questList = new ArrayList<>();
    protected String scoringDescription;

    public APlayerClass(Player owner) {
        this.owner = owner;
    }

    protected SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public abstract PlayerClassType getClassType();

    public PlayerClassData buildClassData() { return new PlayerClassData(); }

    public void enableClass() {
        buildQuestList();

        String message = getClassDescription();
        owner.sendMessage(message);

        if (questList.size() > 0)
            questList.get(data().playerClassDataList.get(getClassType()).currentQuestIndex).startQuest();
    }

    protected String getClassDescription() {
        return ChatUtils.personalAnnouncement("Ta classe : " + PlayerClassHelper.playerClassMetadata.get(getClassType()).getDisplayName(),
                "Ta façon principale de marquer des points est la suivante : " + scoringDescription
                        + ". Tu peux également marquer des points gréce é ta suite de quétes secondaires.");
    }

    public int getScoreMalus() { return 0; }

    protected void buildQuestList() {
    }

    // TODO : ajouter un game ended pour disable les listeners

    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned) {
        return score;
    }

    public void onPlayerHurtTrigger() {
    }

    public void onPlayerDeathTrigger(Player killed, Player killer) {
    }

    public void onTimerTrigger() {
    }

    public void onChestModificationTrigger() {
    }

    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
    }

    public void onExplosionTrigger(Block block) {
    }

    public void onBlockBurnedTrigger(Block block) {
    }

    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
    }

    public void onQuestOver() {
        if (++data().playerClassDataList.get(getClassType()).currentQuestIndex >= questList.size()) {
            String message = ChatUtils.personalAnnouncement("QUéTES TERMINéES", "Bravo ! Tu as terminé toutes tes quétes secondaires !");
            owner.sendMessage(message);
            return;
        }

        questList.get(data().playerClassDataList.get(getClassType()).currentQuestIndex).startQuest();
    }
}
