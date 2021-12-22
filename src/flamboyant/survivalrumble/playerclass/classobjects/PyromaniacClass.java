package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PyromaniacClass extends APlayerClass {
    public PyromaniacClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() { return PlayerClassType.PYROMANIAC; }

    @Override
    public void gameStarted(Server server, Plugin plugin) { }

    @Override
    public void onBlockBurnedTrigger(Block block)
    {
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || ownerTeamName.equals(concernedTeamName)) return;

        changeScore(ownerTeamName, (int)(20 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())));
    }
}
