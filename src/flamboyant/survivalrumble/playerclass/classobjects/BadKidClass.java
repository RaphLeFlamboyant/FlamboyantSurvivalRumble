package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

public class BadKidClass extends APlayerClass implements Listener {
    public BadKidClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BADKID;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL && event.getEntity().getType() != EntityType.EGG) return;
        if (event.getHitEntity() == null) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        Entity ety = event.getHitEntity();
        if (!(ety instanceof Player)) return;
        Player player = (Player) ety;
        if (data().playersTeam.get(owner.getUniqueId()).equals(data().playersTeam.get(player.getUniqueId()))) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        ScoringHelper.addScore(ownerTeamName, 15, ScoreType.FLAT);
    }
}
