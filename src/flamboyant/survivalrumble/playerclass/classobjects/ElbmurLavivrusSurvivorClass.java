package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.MaterialHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ElbmurLavivrusSurvivorClass extends APlayerClass {
    public ElbmurLavivrusSurvivorClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELBMURLAVIVRUS;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned)
    {
        if (!teamConcerned.equals(data().playersTeam.get(owner.getUniqueId()))) return score;

        return score * -1;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block)
    {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block)
    {
        handleBlockModification(block, true);
    }

    @Override
    public void onExplosionTrigger(Block block)
    {
        handleBlockModification(block, true);
    }

    @Override
    public void onBlockBurnedTrigger(Block block)
    {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken)
    {
        MaterialHelper mh = new MaterialHelper();
        if (mh.scoringMaterial.containsKey(block.getType())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        int coef = broken ? 1 : -1;
        changeScore(ownerTeamName, (coef * 2));
    }
}
