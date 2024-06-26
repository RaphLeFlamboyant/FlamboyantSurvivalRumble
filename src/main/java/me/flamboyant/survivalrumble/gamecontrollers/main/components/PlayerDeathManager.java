package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers.CommonShopStepHandler;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers.GhostModeStepHandler;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers.RespawnModeSelectionStepHandler;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers.TeamShopStepHandler;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowOrchestrator;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
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
    private RespawnModeSelectionStepHandler respawnModeSelectionHandler;
    private List<TeamShopStepHandler> teamShopStepHandlers = new ArrayList<>();
    private CommonShopStepHandler commonShopStepHandler;
    private GhostModeStepHandler ghostModeStepHandler;

    private static PlayerDeathManager instance;
    protected PlayerDeathManager()
    {
        respawnModeSelectionHandler = new RespawnModeSelectionStepHandler();
        commonShopStepHandler = new CommonShopStepHandler();
        ghostModeStepHandler = new GhostModeStepHandler();

        for (String teamName : SurvivalRumbleData.getSingleton().getTeams()) {
            TeamShopStepHandler teamShopStepHandler = new TeamShopStepHandler(teamName);
            teamShopStepHandlers.add(teamShopStepHandler);
        }
    }

    public static PlayerDeathManager getInstance() {
        if (instance == null) {
            instance = new PlayerDeathManager();
        }

        return instance;
    }

    public void start() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

        World waitingWorld = Bukkit.getWorld(UsefulConstants.waitingWorldName);
        zeroWaitingSpawn = new Location(waitingWorld, 0, waitingWorld.getHighestBlockYAt(0, 0), 0);

        Common.server.getPluginManager().registerEvents(this, Common.plugin);

        DeathWorkflowOrchestrator.getInstance().addVisitor(this);
        DeathWorkflowOrchestrator.getInstance().addVisitor(respawnModeSelectionHandler);
        DeathWorkflowOrchestrator.getInstance().addVisitor(commonShopStepHandler);
        DeathWorkflowOrchestrator.getInstance().addVisitor(ghostModeStepHandler);
        for (TeamShopStepHandler teamShopStepHandler : teamShopStepHandlers) {
            DeathWorkflowOrchestrator.getInstance().addVisitor(teamShopStepHandler);
        }
    }

    public void stop() {
        Bukkit.getWorld(UsefulConstants.overworldName).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);

        DeathWorkflowOrchestrator.getInstance().removeVisitor(this);
        DeathWorkflowOrchestrator.getInstance().removeVisitor(respawnModeSelectionHandler);
        DeathWorkflowOrchestrator.getInstance().removeVisitor(commonShopStepHandler);
        DeathWorkflowOrchestrator.getInstance().removeVisitor(ghostModeStepHandler);
        for (TeamShopStepHandler teamShopStepHandler : teamShopStepHandlers) {
            DeathWorkflowOrchestrator.getInstance().removeVisitor(teamShopStepHandler);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        var location = event.getRespawnLocation();
        if (!event.isBedSpawn()) {
            location = data().getHeadquarterLocation(data().getPlayerTeam(event.getPlayer()));
            event.setRespawnLocation(location);
        }

        if (playerToPendingDeathWorkflowData.containsKey(event.getPlayer())) {
            DeathWorkflowData deathWorkflowData = playerToPendingDeathWorkflowData.get(event.getPlayer());
            deathWorkflowData.respawnLocation = location;
            event.setRespawnLocation(zeroWaitingSpawn);
            event.getPlayer().setGameMode(GameMode.CREATIVE);

            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.RESPAWN, deathWorkflowData);
            }, 1);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        DeathWorkflowData playerDeathData = new DeathWorkflowData();
        playerDeathData.deadPlayer = event.getEntity();
        playerDeathData.deathLocation = event.getEntity().getLocation();

        List<ItemStack> eventItemDrops = event.getDrops();
        List<ItemStack> keptItems = new ArrayList<>();

        PlayerClassMechanicsHelper.getSingleton().disablePlayerClass(event.getEntity().getUniqueId());

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

        player.teleport(deathWorkflowData.respawnLocation);
        player.setGameMode(GameMode.SURVIVAL);

        World deathWorld = deathWorkflowData.deathLocation.getWorld();
        for (ItemStack item : deathWorkflowData.keptItems) {
            deathWorld.dropItem(deathWorkflowData.deathLocation, item);
        }

        PlayerClassMechanicsHelper.getSingleton().enablePlayerClass(player.getUniqueId());
        DeathWorkflowOrchestrator.getInstance().onEventTriggered(DeathWorkflowEventType.RESPAWN, deathWorkflowData);
    }
}
