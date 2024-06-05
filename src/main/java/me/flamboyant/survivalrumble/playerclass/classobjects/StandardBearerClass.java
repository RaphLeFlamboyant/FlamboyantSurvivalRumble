package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StandardBearerClass extends APlayerClass {
    private BukkitTask intervalTask;
    private int checkInterval = 1;
    private float leftovers = 0f;
    private HashMap<Integer, Float> scoreBySeconds = new HashMap<>() {{
        put(4, 20f);
        put(10, 5f);
        put(20, 2.5f);
        put(30, 1.7f);
        put(40, 1f);
    }};
    private List<Integer> orderedDistance;

    public StandardBearerClass(Player owner) {
        super(owner);

        scoringDescription = "Les ennemis doivent être proches de ta base quand tu y es";
        orderedDistance = scoreBySeconds.keySet().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.STANDARDBEARER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        intervalTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    @Override
    public void disableClass() {
        super.disableClass();

        Bukkit.getScheduler().cancelTask(intervalTask.getTaskId());
    }

    private void updateScoring() {
        if (!owner.getWorld().getName().equals("world")) return;

        var ownerTeam = data().getPlayerTeam(owner);
        if (!TeamHelper.isLocationInHeadQuarter(owner.getLocation(), ownerTeam))
            return;

        var ownerTeamHqLocation = data().getHeadquarterLocation(ownerTeam);
        var closestDistance = Double.MAX_VALUE;
        var playerCount = 0;
        for (Player player : Common.server.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) continue;
            var playerTeam = data().getPlayerTeam(player);
            if (playerTeam == ownerTeam || playerTeam == null) continue;

            var distance = player.getLocation().distance(ownerTeamHqLocation);
            if (distance > 40) continue;

            if (distance < closestDistance)
                closestDistance = distance;

            playerCount++;
        }

        var flatAmount = getFlatAmount((int) closestDistance) * playerCount;
        flatAmount += leftovers;
        leftovers = flatAmount % 1f;

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), (int) flatAmount);
    }

    private float getFlatAmount(int distToHqCenter) {
        float score = 0f;
        for (Integer dist : orderedDistance) {
            if (distToHqCenter <= dist) {
                score = scoreBySeconds.get(dist);
                break;
            }
        }

        return score;
    }
}
