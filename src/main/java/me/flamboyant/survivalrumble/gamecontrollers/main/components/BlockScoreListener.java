package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.MaterialHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class BlockScoreListener implements Listener {
    private HashSet<Vector> newBlockLocation = new HashSet<>();
    private HashMap<Vector, TeamGain> blockLocationToScore = new HashMap<>();
    private Queue<List<Vector>> queuedBlockLocationList = new ArrayDeque<>(60);
    private BukkitTask blocScoreTask;

    public void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);

        blocScoreTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> onTimer(), 60 * 20, 60 * 20);
    }

    public void stop() {
        if (blocScoreTask != null)
            Bukkit.getScheduler().cancelTask(blocScoreTask.getTaskId());

        BlockBreakEvent.getHandlerList().unregister(this);
        BlockBurnEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);

        newBlockLocation.clear();
        blockLocationToScore.clear();
        queuedBlockLocationList.clear();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BREAK)) {
            playerClass.onBlockBreakTrigger(event.getPlayer(), event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        handleBlockDestruction(blockLocation);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BURNED)) {
            playerClass.onBlockBurnedTrigger(event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        handleBlockDestruction(blockLocation);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_EXPLOSION)) {
            playerClass.onExplosionTrigger(event.getBlock());
        }

        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            handleBlockDestruction(blockLocation);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            handleBlockDestruction(blockLocation);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_PLACE)) {
            playerClass.onBlockPlaceTrigger(event.getPlayer(), event.getBlock());
        }

        Location location = event.getBlock().getLocation();
        String concernedTeam = TeamHelper.getTeamHeadquarterName(location);

        if (concernedTeam == null)
            return;

        Vector locationVector = location.toVector();
        newBlockLocation.add(locationVector);

        BlockData blockData = event.getBlock().getBlockData();
        if (MaterialHelper.scoringMaterial.containsKey(blockData.getMaterial())) {
            int scoreChange = MaterialHelper.scoringMaterial.get(blockData.getMaterial());

            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_MODIFIER)) {
                scoreChange = playerClass.onBlockModifierTrigger(scoreChange, blockData, location, concernedTeam);
            }

            if (blockLocationToScore.containsKey(locationVector) && blockData instanceof Slab)
                blockLocationToScore.get(locationVector).earnedMoney += scoreChange;
            else
                blockLocationToScore.put(locationVector, new TeamGain(concernedTeam, scoreChange));
        }
    }

    private void handleBlockDestruction(Location blockLocation) {
        String concernedTeam = TeamHelper.getTeamHeadquarterName(blockLocation);

        if (concernedTeam == null)
            return;

        Vector locationVector = blockLocation.toVector();
        if (blockLocationToScore.containsKey(locationVector)) {
            blockLocationToScore.remove(locationVector);
        }

        if (newBlockLocation.contains(locationVector)) {
            newBlockLocation.remove(locationVector);
        }
    }

    private void onTimer() {
        queuedBlockLocationList.add(new ArrayList<>(newBlockLocation));
        newBlockLocation.clear();

        if (queuedBlockLocationList.size() < 60)
            return;

        List<Vector> reachedLocations = queuedBlockLocationList.poll();

        for (Vector location : reachedLocations) {
            if (!blockLocationToScore.containsKey(location))
                continue;

            TeamGain moneyEarned = blockLocationToScore.get(location);
            blockLocationToScore.remove(location);

            GameManager.getInstance().addAddMoney(moneyEarned.teamName, moneyEarned.earnedMoney);
        }
    }

    private class TeamGain
    {
        private String teamName;
        private int earnedMoney;

        public TeamGain(String teamName, int earnedMoney)
        {
            this.teamName = teamName;
            this.earnedMoney = earnedMoney;
        }
    }
}
