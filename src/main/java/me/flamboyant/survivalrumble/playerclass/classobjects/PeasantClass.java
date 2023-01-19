package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PeasantClass extends APlayerClass {
    public PeasantClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);

        scoringDescription = "Pose des blocs de paille dans ta base";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PEASANT;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockBreak(block);
    }

    private void handleBlockBreak(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        int coef = broken ? -1 : 1;
        if (block.getType() != Material.HAY_BLOCK) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (coef * (int) (10 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()))), ScoreType.REVERSIBLE);
    }
}
