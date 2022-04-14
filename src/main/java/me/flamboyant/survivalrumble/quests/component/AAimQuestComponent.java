package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public abstract class AAimQuestComponent extends AQuestComponent implements Listener {
    protected EntityType entityTypeToUse;
    protected int quantity;

    public AAimQuestComponent(Player player, EntityType entityTypeToUse, int quantity) {
        super(player);

        this.entityTypeToUse = entityTypeToUse;
        this.quantity = quantity;
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        ProjectileHitEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    protected abstract boolean doOnEvent(ProjectileHitEvent event);

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getEntity().getType() != entityTypeToUse) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player shooter = (Player) event.getEntity().getShooter();
        if (shooter != player) return;

        if (!doOnEvent(event)) return;

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
