package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.*;
import me.flamboyant.utils.Common;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashSet;

public class BunglerClass extends AAttackClass implements Listener {
    private static final float blocAmountReward = 4f;
    private static final float foesAwayMalusRatio = 0.06f;
    private static final double validFoesDistance = 50;

    private HashSet<Block> blocksPlacedByOwnerTeam = new HashSet<>();

    public BunglerClass(Player owner) {
        super(owner);

        scoringDescription = "Détruire les blocs exposés au ciel dans une base adverse";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BUNGLER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockBurnEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        var playerWhoBreaks = event.getPlayer();

        if (blocksPlacedByOwnerTeam.contains(block)) {
            blocksPlacedByOwnerTeam.remove(block);
            return;
        }

        if (playerWhoBreaks != owner) return;

        var location = block.getLocation();
        if (location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) != location.getBlockY())
            return;

        var concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        var ownerTeam = data().getPlayerTeam(owner);
        if (concernedTeamName == null || ownerTeam.equals(concernedTeamName))
            return;

        applyAmount(blocAmountReward);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (blocksPlacedByOwnerTeam.contains(event.getBlock())) {
            blocksPlacedByOwnerTeam.remove(event.getBlock());
            return;
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (blocksPlacedByOwnerTeam.contains(event.getBlock())) {
            blocksPlacedByOwnerTeam.remove(event.getBlock());
            return;
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (blocksPlacedByOwnerTeam.contains(block)) {
                blocksPlacedByOwnerTeam.remove(block);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        var playerWhoPlaces = event.getPlayer();

        var ownerTeam = data().getPlayerTeam(owner);
        if (!data().getPlayerTeam(playerWhoPlaces).equals(ownerTeam)) return;
        var location = block.getLocation();
        var concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || ownerTeam.equals(concernedTeamName)) return;

        blocksPlacedByOwnerTeam.add(block);
    }

    @Override
    protected float getMalusRatio() {
        return foesAwayMalusRatio;
    }

    @Override
    protected double getValidationDistance() {
        return validFoesDistance;
    }
}
