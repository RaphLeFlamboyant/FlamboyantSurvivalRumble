package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;

public class FisherClass extends APlayerClass implements Listener {
    public FisherClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.FISHER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH && event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;
        Location location = event.getPlayer().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || !data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        changeScore(concernedTeamName, (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())));
    }
}
