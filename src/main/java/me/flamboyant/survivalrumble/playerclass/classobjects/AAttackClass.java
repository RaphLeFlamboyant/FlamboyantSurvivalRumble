package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AAttackClass extends APlayerClass {
    private String ownerTeam;
    private float leftovers = 0f;

    public AAttackClass(Player owner) {
        super(owner);

        ownerTeam = data().getPlayerTeam(owner);
    }

    protected abstract float getMalusRatio();
    protected abstract double getValidationDistance();

    protected void applyAmount(float flatAmount) {
        var closestTeamHQ = getClosestValidHeadQuarter();
        if (closestTeamHQ == null) return;

        Boolean isAnyFoeClose = false;
        for (Player player : data().getPlayers(closestTeamHQ)) {
            isAnyFoeClose |= ((player.getWorld() == owner.getWorld()) && player.getLocation().distance(owner.getLocation()) < getValidationDistance());
        }

        if (!isAnyFoeClose) flatAmount *= getMalusRatio();
        flatAmount += leftovers;
        leftovers = flatAmount % 1f;

        GameManager.getInstance().addAddMoney(ownerTeam, (int) flatAmount);
    }

    protected String getClosestValidHeadQuarter() {
        double minDist = getValidationDistance();
        String res = null;
        for (String teamName : data().getTeams()) {
            if (ownerTeam.equals(teamName)) continue;

            Location hqLocation = data().getHeadquarterLocation(teamName);
            var dist = hqLocation.distance(owner.getLocation());
            if (dist < minDist) {
                minDist = dist;
                res = teamName;
            }
        }

        return res;
    }
}
