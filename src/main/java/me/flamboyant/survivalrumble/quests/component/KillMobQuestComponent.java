package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobQuestComponent extends AQuestComponent implements Listener {
    private EntityType entityType;
    private int quantity;

    public KillMobQuestComponent(Player player, EntityType entityType, int quantity) {
        super(player);
        this.quantity = quantity;
        this.entityType = entityType;

        subQuestMessage = "Tue " + quantity + " x " + entityType.toString();
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        EntityDeathEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (event.getEntity().getKiller().getUniqueId() != player.getUniqueId()) return;
        if (entityType != event.getEntity().getType()) return;

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
