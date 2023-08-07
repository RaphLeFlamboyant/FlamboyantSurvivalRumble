package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlocQuestTask extends AQuestTask implements Listener {
    private Material material;
    private int quantity;
    private boolean inHQ;

    public PlaceBlocQuestTask(Quest ownerQuest, Material material, int quantity, boolean inHQ) {
        super(ownerQuest);
        this.material = material;
        this.quantity = quantity;
        this.inHQ = inHQ;

        subQuestMessage = "Place " + quantity + " x " + material.toString() + (inHQ ? " dans la base" : "");
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        BlockPlaceEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        System.out.println(" ----- BlockPlaceEvent by " + event.getPlayer().getName() + " ----- ");
        if (event.getPlayer() != player) return;
        System.out.println("Placed : " + event.getBlock().getType());
        if (event.getBlock().getType() != material) return;
        String teamName = SurvivalRumbleData.getSingleton().getPlayerTeam(player);
        System.out.println("HQ mode : " + inHQ + "; Test : " + TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), teamName));
        if (inHQ && !TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), teamName)) return;

        System.out.println("Remaining quantity : " + (quantity - 1));

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
