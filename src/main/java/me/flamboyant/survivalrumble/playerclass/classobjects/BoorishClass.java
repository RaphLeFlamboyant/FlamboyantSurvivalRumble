package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoreHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BoorishClass extends APlayerClass {
    private Map<Material, Integer> pointsByOre = new HashMap<Material, Integer>() {{
        put(Material.COAL_ORE, 3);
        put(Material.REDSTONE_ORE, 4);
        put(Material.IRON_ORE, 2);
        put(Material.NETHER_GOLD_ORE, 4);
        put(Material.NETHER_QUARTZ_ORE, 4);
        put(Material.GILDED_BLACKSTONE, 3);
        put(Material.LAPIS_ORE, 7);
        put(Material.GOLD_ORE, 3);
        put(Material.EMERALD_ORE, 25);
        put(Material.DIAMOND_ORE, 20);
    }};

    public BoorishClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "Placer des blocs de minerais entre la couche 70 et la couche " + ScoreHelper.fullScoreMaxY;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BOORISH;
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
        if (!pointsByOre.containsKey(block.getType())) return;
        Location location = block.getLocation();
        if (location.getBlockY() <= 70 || location.getBlockY() > ScoreHelper.fullScoreMaxY) return;
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().getPlayerTeam(owner);
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addAddMoney(ownerTeamName, (coef * (int) (pointsByOre.get(block.getType()) * ScoreHelper.scoreAltitudeCoefficient(location.getBlockY()))));
    }
}
