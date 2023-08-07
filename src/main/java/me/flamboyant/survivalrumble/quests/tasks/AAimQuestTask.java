package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public abstract class AAimQuestTask extends AQuestTask implements Listener {
    protected EntityType entityTypeToUse;
    protected int quantity;

    public AAimQuestTask(Quest ownerQuest, EntityType entityTypeToUse, int quantity) {
        super(ownerQuest);

        this.entityTypeToUse = entityTypeToUse;
        this.quantity = quantity;
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        ProjectileHitEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
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
            stopQuest(true);
        }
    }
}
