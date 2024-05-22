package me.flamboyant.survivalrumble.gamecontrollers.main;

import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.QuestPoolType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerClassMechanicsHelper {
    private static PlayerClassMechanicsHelper instance;
    private HashMap<UUID, APlayerClass> playerClasses = new HashMap<>();

    protected PlayerClassMechanicsHelper() {
    }

    public static PlayerClassMechanicsHelper getSingleton() {
        if (instance == null)
            instance = new PlayerClassMechanicsHelper();

        return instance;
    }

    public void declarePlayerClass(Player player, APlayerClass playerClass) {
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
