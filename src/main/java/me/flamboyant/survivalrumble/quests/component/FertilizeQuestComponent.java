package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class FertilizeQuestComponent extends AQuestComponent implements Listener {
    private int quantity;
    private boolean inHQ;

    public FertilizeQuestComponent(Player player, int quantity, boolean inHQ) {
        super(player);

        this.quantity = quantity;
        this.inHQ = inHQ;

        subQuestMessage = "Fertilisez le sol " + quantity + " fois " + (inHQ ? "dans votre base" : "n'importe oé");
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        BlockFertilizeEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() != player) return;
        String ownerTeam = SurvivalRumbleData.getSingleton().playersTeam.get(player.getUniqueId());
        if (inHQ && !TeamHelper.isLocationInHeadQuarter(player.getLocation(), ownerTeam)) return;

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
