package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class BomberClass extends APlayerClass implements Listener {
    public BomberClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BOMBER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getUniqueId().equals(owner.getUniqueId())) return;
        if (!event.hasItem() || event.getItem().getType() != Material.TNT) return;

        event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 3.5f, true);
        event.getPlayer().setHealth(0);
        changeScore(data().playersTeam.get(owner.getUniqueId()), -200);
    }
}
