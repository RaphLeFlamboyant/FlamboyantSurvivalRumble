package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class FertilizeQuestTask extends AQuestTask implements Listener {
    private int quantity;
    private boolean inHQ;

    public FertilizeQuestTask(Quest ownerQuest, int quantity, boolean inHQ) {
        super(ownerQuest);

        this.quantity = quantity;
        this.inHQ = inHQ;

        subQuestMessage = "Fertilisez le sol " + quantity + " fois " + (inHQ ? "dans votre base" : "n'importe oé");
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        BlockFertilizeEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() != player) return;
        String ownerTeam = SurvivalRumbleData.getSingleton().getPlayerTeam(player);
        if (inHQ && !TeamHelper.isLocationInHeadQuarter(player.getLocation(), ownerTeam)) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
