package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
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
    protected String getClassDescriptionCorpus() {
        return "Utiliser une TNT provoque une énorme explosion. En contrepartie, l'explosion te tue et ton équipe perd 200 points";
    }

    @Override
    protected int getScoreMalus() {
        return 0;
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getUniqueId().equals(owner.getUniqueId())) return;
        if (!event.hasItem() || event.getItem().getType() != Material.TNT) return;

        event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 3.5f, true);
        event.getPlayer().setHealth(0);
        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), -200, ScoreType.PERFECT);
        String playerTeam = data().playersTeam.get(owner.getUniqueId());
        data().teamMalus.put(playerTeam, data().teamMalus.get(playerTeam) - 200);
    }
}
