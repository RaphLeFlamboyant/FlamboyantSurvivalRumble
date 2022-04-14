package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlocQuestComponent extends AQuestComponent implements Listener {
    private Material material;
    private int quantity;
    private boolean inHQ;

    public PlaceBlocQuestComponent(Player player, Material material, int quantity, boolean inHQ) {
        super(player);
        this.material = material;
        this.quantity = quantity;
        this.inHQ = inHQ;

        subQuestMessage = "Place " + quantity + " x " + material.toString() + (inHQ ? " dans la base" : "");
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        BlockPlaceEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        System.out.println(" ----- BlockPlaceEvent by " + event.getPlayer().getName() + " ----- ");
        if (event.getPlayer() != player) return;
        System.out.println("Placed : " + event.getBlock().getType());
        if (event.getBlock().getType() != material) return;
        String teamName = SurvivalRumbleData.getSingleton().playersTeam.get(player.getUniqueId());
        System.out.println("HQ mode : " + inHQ + "; Test : " + TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), teamName));
        if (inHQ && !TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), teamName)) return;

        System.out.println("Remaining quantity : " + (quantity - 1));

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
