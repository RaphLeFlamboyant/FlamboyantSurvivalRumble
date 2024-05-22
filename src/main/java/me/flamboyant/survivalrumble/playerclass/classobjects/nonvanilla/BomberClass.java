package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BomberClass extends ANonVanillaClass implements Listener {
    public BomberClass(Player owner) {
        super(owner);
    }

    @Override
    protected String getAbilityDescription() {
        return "Utiliser une TNT provoque une énorme explosion. En contrepartie, l'explosion te tue et ton équipe perd 200 points";
    }

    @Override
    protected String getScoringDescription() {
        return "Tu ne rapportes rien à ton équipe";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BOMBER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        super.disableClass();
        PlayerInteractEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getUniqueId().equals(owner.getUniqueId())) return;
        if (!event.hasItem() || event.getItem().getType() != Material.TNT) return;

        event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 3.5f, true);
        event.getPlayer().setHealth(0);
        String playerTeam = data().getPlayerTeam(owner);
        GameManager.getInstance().addAddMoney(playerTeam, -200);
    }
}
