package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.*;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ElbmurLavivrusClass extends APlayerClass implements Listener {
    public ElbmurLavivrusClass(Player owner) {
        super(owner);

        scoringDescription = "esab at snad scolb sed ressac tneviod seriasrevda seT";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELBMURLAVIVRUS;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        BlockBreakEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        var playerWhoBreaks = event.getPlayer();
        var ownerTeam = data().getPlayerTeam(owner);

        if (data().getPlayerTeam(playerWhoBreaks).equals(ownerTeam)) return;

        var location = block.getLocation();
        if (!TeamHelper.isLocationInHeadQuarter(location, ownerTeam))
            return;

        GameManager.getInstance().addAddMoney(ownerTeam, 2);
    }
}
