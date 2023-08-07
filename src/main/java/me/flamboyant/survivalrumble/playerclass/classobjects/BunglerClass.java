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
        String ownerTeam = data().getPlayerTeam(owner);
        if (concernedTeamName == null || ownerTeam.equals(concernedTeamName)) return;

        GameManager.getInstance().addAddMoney(ownerTeam, (int) (scoreCoef * ScoreHelper.scoreAltitudeCoefficient(location.getBlockY())));
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        if (!MaterialHelper.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        String ownerTeam = data().getPlayerTeam(owner);
        if (!data().getPlayerTeam(playerWhoBreaks).equals(ownerTeam)) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || ownerTeam.equals(concernedTeamName)) return;

        GameManager.getInstance().addAddMoney(ownerTeam, (int) (malusCoef * ScoreHelper.scoreAltitudeCoefficient(block.getLocation().getBlockY())));
    }
}
