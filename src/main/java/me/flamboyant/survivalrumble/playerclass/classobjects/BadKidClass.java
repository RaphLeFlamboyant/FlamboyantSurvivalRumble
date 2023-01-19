package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class BadKidClass extends APlayerClass implements Listener {
    public BadKidClass(Player owner) {
        super(owner);

        scoringDescription = "Tirer des oeufs ou des boules de neige sur les adversaires";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BADKID;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.SNOWBALL && event.getEntity().getType() != EntityType.EGG) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player shooter = (Player) event.getEntity().getShooter();
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;

        shooter.setCooldown(event.getEntityType() == EntityType.SNOWBALL ? Material.SNOWBALL : Material.EGG, 60 * 60 * 5);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL && event.getEntity().getType() != EntityType.EGG) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        shooter.setCooldown(event.getEntityType() == EntityType.SNOWBALL ? Material.SNOWBALL : Material.EGG, 0);
        if (event.getHitEntity() == null) return;
        Entity ety = event.getHitEntity();
        if (!(ety instanceof Player)) return;
        Player player = (Player) ety;
        if (data().playersTeam.get(owner.getUniqueId()).equals(data().playersTeam.get(player.getUniqueId()))) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, 30, ScoreType.FLAT);
    }
}
