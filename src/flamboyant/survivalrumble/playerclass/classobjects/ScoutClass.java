package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoutClass extends APlayerClass {
    private String ownerTeam;

    public ScoutClass(Player owner) {
        super(owner);

        ownerTeam = data().playersTeam.get(owner.getUniqueId());
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.SCOUT;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> updateScoring(server), 0l, 100l);
    }

    private void updateScoring(Server server) {
        owner = server.getPlayer(owner.getUniqueId()); // TODO : check si c'est nécessaire
        String hqNearby = teamHQNearby(owner.getLocation());
        if (hqNearby == null) return;

        Location hqLocation = data().teamHeadquarterLocation.get(hqNearby);
        int maxDist = Math.max(Math.abs(hqLocation.getBlockX() - owner.getLocation().getBlockX()), Math.abs(hqLocation.getBlockZ() - owner.getLocation().getBlockZ()));

        changeScore(ownerTeam, getScoring(maxDist));
    }

    private int getScoring(int distToHqCenter) {
        int scoreBase = distToHqCenter < 8 ? 20
                : distToHqCenter < 15 ? 10
                : distToHqCenter < 25 ? 5
                : distToHqCenter < 50 ? 2
                : distToHqCenter < 100 ? 1
                : 0;

        scoreBase = (int)(scoreBase * ScoringHelper.scoreAltitudeCoefficient(owner.getLocation().getBlockY()));

        return scoreBase;
    }

    private String teamHQNearby(Location location)
    {
        for(String teamName : data().teamHeadquarterLocation.keySet()) {
            if (ownerTeam.equals(teamName)) continue;

            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            if (Math.abs(hqLocation.getBlockX() - location.getBlockX()) < 100
                    && Math.abs(hqLocation.getBlockZ() - location.getBlockZ()) < 100)
                return teamName;
        }

        return null;
    }
}
