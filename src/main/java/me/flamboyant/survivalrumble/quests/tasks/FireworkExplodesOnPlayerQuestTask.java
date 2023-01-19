package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class FireworkExplodesOnPlayerQuestTask extends AQuestTask implements Listener {
    private int quantity;
    private Player targetPlayer;

    public FireworkExplodesOnPlayerQuestTask(Quest ownerQuest, Player targetPlayer, int quantity) {
        super(ownerQuest);

        this.quantity = quantity;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player victim = (Player) event.getEntity();
        if (victim != targetPlayer) return;
        if (event.getDamager().getType() != EntityType.FIREWORK) return;
        Firework firework = (Firework) event.getDamager();
        ProjectileSource source = firework.getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (shooter != player) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
