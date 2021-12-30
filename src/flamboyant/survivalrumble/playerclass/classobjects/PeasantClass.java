package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PeasantClass extends APlayerClass {
    public PeasantClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);
    }

    @Override
    public PlayerClassType getClassType() { return PlayerClassType.PEASANT; }

    @Override
    public void gameStarted(Server server, Plugin plugin) { }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block)
    {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block)
    {
        handleBlockBreak(block);
    }

    @Override
    public void onBlockBurnedTrigger(Block block)
    {
        handleBlockBreak(block);
    }

    @Override
    public void onExplosionTrigger(Block block)
    {
        handleBlockBreak(block);
    }

    private void handleBlockBreak(Block block)
    {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken)
    {
        int coef = broken ? -1 : 1;
        if (block.getType() != Material.HAY_BLOCK) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        changeScore(ownerTeamName, (coef * (int)(10 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()))));
    }
}
