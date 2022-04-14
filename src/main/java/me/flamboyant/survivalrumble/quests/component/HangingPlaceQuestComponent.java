package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class HangingPlaceQuestComponent extends AQuestComponent implements Listener {
    private EntityType entityType;
    private int quantity;

    public HangingPlaceQuestComponent(Player player, EntityType entityType, int quantity) {
        super(player);

        this.entityType = entityType;
        this.quantity = quantity;

        subQuestMessage = "Pose " + quantity + " x " + entityType.toString();
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        HangingPlaceEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getEntity().getType() != entityType) return;

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
