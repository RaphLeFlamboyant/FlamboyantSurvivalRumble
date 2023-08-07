package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class HangingPlaceQuestTask extends AQuestTask implements Listener {
    private EntityType entityType;
    private int quantity;

    public HangingPlaceQuestTask(Quest ownerQuest, EntityType entityType, int quantity) {
        super(ownerQuest);

        this.entityType = entityType;
        this.quantity = quantity;

        subQuestMessage = "Pose " + quantity + " x " + entityType.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        HangingPlaceEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getEntity().getType() != entityType) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
