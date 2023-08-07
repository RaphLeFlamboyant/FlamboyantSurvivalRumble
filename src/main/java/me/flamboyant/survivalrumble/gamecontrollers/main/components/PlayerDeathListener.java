package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {
    private static PlayerDeathListener instance;
    protected PlayerDeathListener() {

    }

    public static PlayerDeathListener getInstance() {
        if (instance == null) {
            instance = new PlayerDeathListener();
        }

        return instance;
    }

    //TODO
    public void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            Location loc = data().getHeadquarterLocation(data().getPlayerTeam(event.getPlayer()));
            event.setRespawnLocation(loc);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.DEATH)) {
                playerClass.onPlayerDeathTrigger(event.getEntity(), event.getEntity().getKiller());
            }
        }
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }
}
