package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class ElbmurLavivrusClass extends APlayerClass {
    public ElbmurLavivrusClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "leic ua sésopxe esab at ed scolb sel eriurtéD";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELBMURLAVIVRUS;
    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned) {
        if (!teamConcerned.equals(data().playersTeam.get(owner.getUniqueId()))) return score;

        return score * -1;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        if (MaterialHelper.scoringMaterial.containsKey(block.getType()) || block.getType() == Material.WATER || block.getType() == Material.LAVA)
            return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;
        if (block.getWorld().getHighestBlockYAt(location) > location.getBlockY()) return;

        int coef = broken ? 1 : -1;
        GameManager.getInstance().addScore(ownerTeamName, (coef * 1), ScoreType.REVERSIBLE);
    }
}
