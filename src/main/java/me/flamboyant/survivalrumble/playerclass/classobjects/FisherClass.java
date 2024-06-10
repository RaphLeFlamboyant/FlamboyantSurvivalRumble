package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FisherClass extends APlayerClass implements Listener {
    public FisherClass(Player owner) {
        super(owner);

        scoringDescription = "Pécher des poissons dans ta base";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.FISHER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        PlayerFishEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH && event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;
        Location location = event.getPlayer().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || !data().getPlayerTeam(owner).equals(concernedTeamName)) return;

        GameManager.getInstance().addAddMoney(concernedTeamName, 10);
    }
}
