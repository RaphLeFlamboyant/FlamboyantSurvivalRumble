package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobQuestTask extends AQuestTask implements Listener {
    private EntityType entityType;
    private int quantity;

    public KillMobQuestTask(Quest ownerQuest, EntityType entityType, int quantity) {
        super(ownerQuest);
        this.quantity = quantity;
        this.entityType = entityType;

        subQuestMessage = "Tue " + quantity + " x " + entityType.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        EntityDeathEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (event.getEntity().getKiller().getUniqueId() != player.getUniqueId()) return;
        if (entityType != event.getEntity().getType()) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
