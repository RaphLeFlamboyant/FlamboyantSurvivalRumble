package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class HQProximityQuestComponent extends AQuestComponent {
    private String targetTeam = "";
    private int durationInSeconds;
    private int distance;
    private boolean squareShapedDistance;
    private BukkitTask checkHQProximity = null;

    public HQProximityQuestComponent(Player player, int durationInSeconds, int distance, boolean squareShapedDistance) {
        super(player);

        this.durationInSeconds = durationInSeconds;
        this.distance = distance;
        this.squareShapedDistance = squareShapedDistance;

        String duration = "";
        if (durationInSeconds / 60 > 0) {
            duration += (durationInSeconds / 60) + " minutes";
            durationInSeconds -= (durationInSeconds / 60) * 60;
            if (durationInSeconds > 0)
                duration += " et ";
        }

        subQuestMessage = "Approche toi é moins de " + distance + " blocs du centre d'une base ennemie pendant " + duration;
    }

    public HQProximityQuestComponent(Player player, int durationInSeconds, int distance, boolean squareShapedDistance, String targetTeam) {
        super(player);

        this.targetTeam = targetTeam;
        this.durationInSeconds = durationInSeconds;
        this.distance = distance;
        this.squareShapedDistance = squareShapedDistance;

        String duration = "";
        if (durationInSeconds / 60 > 0) {
            duration += (durationInSeconds / 60) + " minutes";
            durationInSeconds -= (durationInSeconds / 60) * 60;
            if (durationInSeconds > 0)
                duration += " et ";
        }

        subQuestMessage = "Approche toi é moins de " + distance + " blocs du centre de la base de l'équipe " + targetTeam + " pendant " + duration;
    }


    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        checkHQProximity = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> checkPlayerProximityToFoesHQ(), 20L, 20L);
    }

    @Override
    protected void stopQuest() {
        super.stopQuest();
        Bukkit.getScheduler().cancelTask(checkHQProximity.getTaskId());
    }

    private void checkPlayerProximityToFoesHQ() {
        if (!player.getWorld().getName().equals("world")) return;
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String ownerTeam = data.playersTeam.get(player.getUniqueId());

        for (String team : data.teams) {
            if (!targetTeam.equals("") && !targetTeam.equals(team)) continue;
            if (targetTeam.equals("") && team.equals(ownerTeam)) continue;

            Location hqLocation = data.teamHeadquarterLocation.get(team);
            int currentDistance = getDistanceFrom(hqLocation);
            if (currentDistance <= distance) {
                if (--durationInSeconds <= 0) stopQuest();
                break;
            }
        }
    }

    private int getDistanceFrom(Location location) {
        Location playerLocation = player.getLocation();

        if (squareShapedDistance)
            return Integer.max(Math.abs(playerLocation.getBlockX() - location.getBlockX()), Math.abs(playerLocation.getBlockZ() - location.getBlockZ()));

        return (int) playerLocation.distance(location);
    }
}
