package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BunglerClass extends APlayerClass {
    private static final int scoreCoef = 5;
    private static final int malusCoef = -4;

    public BunglerClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);

        scoringDescription = "Détruire des blocs de construction dans une base adverse";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BUNGLER;
    }

    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        if (playerWhoBreaks != owner) return;

        if (!MaterialHelper.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), (int) (scoreCoef * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        if (!MaterialHelper.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        if (!data().playersTeam.get(playerWhoBreaks.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, (int) (malusCoef * ScoringHelper.scoreAltitudeCoefficient(block.getLocation().getBlockY())), ScoreType.FLAT);
    }
}
