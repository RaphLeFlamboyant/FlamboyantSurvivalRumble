package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class CrafterClass extends APlayerClass implements Listener {
    private HashSet<Material> carftedItems = new HashSet<>();

    public CrafterClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CRAFTER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(owner.getUniqueId())) return;
        if (carftedItems.contains(event.getInventory().getResult().getType())) return;
        Location location = event.getWhoClicked().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (!concernedTeamName.equals(data().playersTeam.get(owner.getUniqueId()))) return;

        ScoringHelper.addScore(concernedTeamName, (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
        carftedItems.add(event.getInventory().getResult().getType());
    }
}
