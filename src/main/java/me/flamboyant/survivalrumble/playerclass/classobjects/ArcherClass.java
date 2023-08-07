package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ArcherClass extends APlayerClass implements Listener {
    public ArcherClass(Player owner) {
        super(owner);

        scoringDescription = "Tirer des fléches sur les adversaires";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ARCHER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        ProjectileHitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.ARROW) return;
        if (event.getHitEntity() == null) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        Entity ety = event.getHitEntity();
        if (!(ety instanceof Player)) return;
        Player player = (Player) ety;
        String ownerTeam = data().getPlayerTeam(owner);
        if (ownerTeam.equals(data().getPlayerTeam(player))) return;

        GameManager.getInstance().addAddMoney(ownerTeam, 40);
    }
}
