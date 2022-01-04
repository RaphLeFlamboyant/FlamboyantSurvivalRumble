package flamboyant.survivalrumble.utils;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class TeamHelper {
    public static List<String> teamNames = Arrays.asList("RED", "BLUE", "GREEN", "GOLD");

    public static ChatColor getTeamColor(String teamName) {
        switch (teamName) {
            case "RED":
                return ChatColor.RED;
            case "BLUE":
                return ChatColor.BLUE;
            case "GREEN":
                return ChatColor.GREEN;
            case "GOLD":
                return ChatColor.GOLD;
            default:
                return ChatColor.BLACK;
        }
    }

    public static Material getTeamFlagMaterial(String teamName) {
        switch (teamName) {
            case "RED":
                return Material.RED_BANNER;
            case "BLUE":
                return Material.BLUE_BANNER;
            case "GREEN":
                return Material.GREEN_BANNER;
            case "GOLD":
                return Material.YELLOW_BANNER;
            default:
                return Material.BLACK_BANNER;
        }
    }

    public static Material getTeamConcreteMaterial(String teamName) {
        switch (teamName) {
            case "RED":
                return Material.RED_CONCRETE;
            case "BLUE":
                return Material.BLUE_CONCRETE;
            case "GREEN":
                return Material.GREEN_CONCRETE;
            case "GOLD":
                return Material.YELLOW_CONCRETE;
            default:
                return Material.BLACK_CONCRETE;
        }
    }

    public static Material getTeamBedMaterial(String teamName) {
        switch (teamName) {
            case "RED":
                return Material.RED_BED;
            case "BLUE":
                return Material.BLUE_BED;
            case "GREEN":
                return Material.GREEN_BED;
            case "GOLD":
                return Material.YELLOW_BED;
            default:
                return Material.BLACK_BED;
        }
    }

    public static String getTeamHeadquarterName(Location location) {
        for (String teamName : SurvivalRumbleData.getSingleton().teams) {
            if (isLocationInHeadQuarter(location, teamName)) {
                return teamName;
            }
        }

        return null;
    }

    public static Boolean isLocationInHeadQuarter(Location location, String teamName) {
        Location teamHQLocation = SurvivalRumbleData.getSingleton().teamHeadquarterLocation.get(teamName);

        return Math.abs(location.getBlockX() - teamHQLocation.getBlockX()) < 25
                && Math.abs(location.getBlockZ() - teamHQLocation.getBlockZ()) < 25;
    }
}
