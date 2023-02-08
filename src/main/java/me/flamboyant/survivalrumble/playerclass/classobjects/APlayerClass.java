package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.QuestPoolType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class APlayerClass {
    public ArrayList<ScoringTriggerType> triggers = new ArrayList<ScoringTriggerType>();
    protected Player owner;
    protected String scoringDescription;

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
        owner.sendMessage(message);
    }

    protected String getClassDescription() {
        return ChatUtils.personalAnnouncement("Ta classe : " + PlayerClassHelper.playerClassMetadata.get(getClassType()).getDisplayName(),
                "Ta façon principale de marquer des points est la suivante : " + scoringDescription
                        + ". Tu peux également marquer des points gréce é ta suite de quétes secondaires.");
    }

    public int getScoreMalus() { return 0; }

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
}
