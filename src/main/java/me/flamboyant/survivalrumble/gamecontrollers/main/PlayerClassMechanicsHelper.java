package me.flamboyant.survivalrumble.gamecontrollers.main;

import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.QuestPoolType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerClassMechanicsHelper {
    private static PlayerClassMechanicsHelper instance;
    public HashMap<ScoringTriggerType, List<APlayerClass>> connectedClasses = new HashMap<ScoringTriggerType, List<APlayerClass>>();
    private HashMap<UUID, APlayerClass> playerClasses = new HashMap<>();

    protected PlayerClassMechanicsHelper() {
        connectedClasses.put(ScoringTriggerType.BLOCK_BREAK, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.BLOCK_BURNED, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.BLOCK_EXPLOSION, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.BLOCK_MODIFIER, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.BLOCK_PLACE, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.CHEST_MODIFICATION, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.DEATH, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.HURT, new ArrayList<APlayerClass>());
        connectedClasses.put(ScoringTriggerType.ON_TIMER, new ArrayList<APlayerClass>());
    }

    public static PlayerClassMechanicsHelper getSingleton() {
        if (instance == null)
            instance = new PlayerClassMechanicsHelper();

        return instance;
    }

    private void addPlayerClassToTriggers(APlayerClass playerClass) {
        for (ScoringTriggerType trigger : playerClass.triggers) {
            System.out.println("Setup trigger " + trigger.toString() + " for class " + playerClass.getClassType().toString());
            connectedClasses.get(trigger).add(playerClass);
        }
    }

    public void declarePlayerClass(Player player, APlayerClass playerClass) {
        addPlayerClassToTriggers(playerClass);

        playerClasses.put(player.getUniqueId(), playerClass);
    }

    public void enablePlayerClass(UUID playerId) {
        playerClasses.get(playerId).enableClass();
    }

    public QuestPoolType getQuestPoolType(UUID playerId) {
        return playerClasses.get(playerId).getQuestPoolTpye();
    }

    public void disablePlayerClass(UUID playerId) {
        playerClasses.get(playerId).disableClass();
    }

    public void enablePlayersClasses() {
        for (UUID playerId : playerClasses.keySet()) {
            playerClasses.get(playerId).enableClass();
        }
    }

    public void disablePlayersClasses() {
        for (UUID playerId : playerClasses.keySet()) {
            playerClasses.get(playerId).disableClass();
        }
    }
}
