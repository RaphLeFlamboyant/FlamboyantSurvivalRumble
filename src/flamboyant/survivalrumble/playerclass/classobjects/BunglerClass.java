package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.MaterialHelper;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BunglerClass extends APlayerClass {
    public BunglerClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BUNGLER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        if (playerWhoBreaks != owner) return;

        MaterialHelper mh = new MaterialHelper();
        if (!mh.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        changeScore(data().playersTeam.get(owner.getUniqueId()), (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())));
    }
}
