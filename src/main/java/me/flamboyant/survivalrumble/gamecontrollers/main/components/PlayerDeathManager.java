package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import me.flamboyant.utils.Common;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerDeathManager implements Listener {
    private HashMap<Player, RegisteredDeath> registeredDeath = new HashMap<>();
    private Location zeroWaitingSpawn;

    private static PlayerDeathManager instance;
    protected PlayerDeathManager() {

    }

    public static PlayerDeathManager getInstance() {
        if (instance == null) {
            instance = new PlayerDeathManager();
        }

        return instance;
    }

    //TODO
    public void start() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

        World waitingWorld = Bukkit.getWorld(UsefulConstants.waitingWorldName);
        zeroWaitingSpawn = new Location(waitingWorld, 0, waitingWorld.getHighestBlockYAt(0, 0), 0);

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            Location loc = data().getHeadquarterLocation(data().getPlayerTeam(event.getPlayer()));
            event.setRespawnLocation(loc);
        }

        if (registeredDeath.containsKey(event.getPlayer())) {
            event.setRespawnLocation(zeroWaitingSpawn);
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.DEATH)) {
                playerClass.onPlayerDeathTrigger(event.getEntity(), event.getEntity().getKiller());
            }
        }

        RegisteredDeath playerDeath = new RegisteredDeath();
        playerDeath.deadPlayer = event.getEntity();
        playerDeath.deathLocation = event.getEntity().getLocation();

        List<ItemStack> eventItemDrops = event.getDrops();
        List<ItemStack> keptItems = new ArrayList<>();

        for (ItemStack item : eventItemDrops) {
            keptItems.add(item.clone());
        }

        eventItemDrops.clear();
        playerDeath.keptItems = keptItems;

        registeredDeath.put(playerDeath.deadPlayer, playerDeath);
    }


    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    private class RegisteredDeath {
        public Player deadPlayer;
        public Location deathLocation;
        public Location respawnLocation;
        public List<ItemStack> keptItems;
    }
}
