package me.flamboyant.survivalrumble.utils;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
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

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
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

    public void enablePlayerClasses() {
        for (UUID playerId : playerClasses.keySet()) {
            playerClasses.get(playerId).enableClass();
        }
    }
}
