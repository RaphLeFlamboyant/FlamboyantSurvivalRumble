package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views.RespawnModeSelectionHandler;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import me.flamboyant.survivalrumble.views.respawnmodeselection.RespawnModeSelectionView;
import me.flamboyant.utils.Common;
import me.flamboyant.workflow.WorkflowVisitor;
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

public class PlayerDeathManager implements Listener, WorkflowVisitor<DeathWorkflowStepType, DeathWorkflowData> {
    private Location zeroWaitingSpawn;
    private HashMap<Player, DeathWorkflowData> playerToPendingDeathWorkflowData = new HashMap<>();

    private RespawnModeSelectionHandler respawnModeSelectionHandler;

    private static PlayerDeathManager instance;
    protected PlayerDeathManager()
    {
        respawnModeSelectionHandler = new RespawnModeSelectionHandler();
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

        DeathWorkflowOrchestrator.getInstance().addVisitor(this);
        DeathWorkflowOrchestrator.getInstance().addVisitor(respawnModeSelectionHandler);
    }

    public void stop() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);

        DeathWorkflowOrchestrator.getInstance().removeVisitor(this);
        DeathWorkflowOrchestrator.getInstance().removeVisitor(respawnModeSelectionHandler);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            Location loc = data().getHeadquarterLocation(data().getPlayerTeam(event.getPlayer()));
            event.setRespawnLocation(loc);
        }

        if (playerToPendingDeathWorkflowData.containsKey(event.getPlayer())) {
            event.setRespawnLocation(zeroWaitingSpawn);
            event.getPlayer().setGameMode(GameMode.CREATIVE);

            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.RESPAWN, playerToPendingDeathWorkflowData.get(event.getPlayer()));
            }, 1);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.DEATH)) {
                playerClass.onPlayerDeathTrigger(event.getEntity(), event.getEntity().getKiller());
            }
        }

        DeathWorkflowData playerDeathData = new DeathWorkflowData();
        playerDeathData.deadPlayer = event.getEntity();
        playerDeathData.deathLocation = event.getEntity().getLocation();

        List<ItemStack> eventItemDrops = event.getDrops();
        List<ItemStack> keptItems = new ArrayList<>();

        for (ItemStack item : eventItemDrops) {
            keptItems.add(item.clone());
        }

        eventItemDrops.clear();
        playerDeathData.keptItems = keptItems;

        DeathWorkflowOrchestrator.getInstance().startWorkflow(playerDeathData);
    }


    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    @Override
    public void onWorkflowStart(DeathWorkflowData deathWorkflowData) {
        playerToPendingDeathWorkflowData.put(deathWorkflowData.deadPlayer, deathWorkflowData);
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (deathWorkflowStepType == DeathWorkflowStepType.NORMAL_RESPAWN) {
            handleNormalRespawnStep(deathWorkflowData);
        }
    }

    @Override
    public void onWorkflowEnd(DeathWorkflowData deathWorkflowData) {
        playerToPendingDeathWorkflowData.remove(deathWorkflowData.deadPlayer);
    }

    private void handleNormalRespawnStep(DeathWorkflowData deathWorkflowData) {
        Player player = deathWorkflowData.deadPlayer;

        player.teleport(deathWorkflowData.deathLocation);
        player.setGameMode(GameMode.SURVIVAL);

        World deathWorld = deathWorkflowData.deathLocation.getWorld();
        for (ItemStack item : deathWorkflowData.keptItems) {
            deathWorld.dropItem(deathWorkflowData.deathLocation, item);
        }

        DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.RESPAWN, deathWorkflowData);
    }
}
