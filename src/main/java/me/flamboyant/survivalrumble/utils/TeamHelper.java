package me.flamboyant.survivalrumble.utils;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

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

    public static Material getTeamBannerMaterial(String teamName) {
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

    public static String getTeamStructureName(String teamName) {
        switch (teamName) {
            case "RED":
                return "sr_spawn_red";
            case "BLUE":
                return "sr_spawn_blue";
            case "GREEN":
                return "sr_spawn_green";
            case "GOLD":
                return "sr_spawn_yellow";
            default:
                return ""; // TODO : erreur
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

    public static List<UUID> getRandomPlayer(List<String> teamToSeekIn, int pickNumber) {
        ArrayList<UUID> players = new ArrayList<>();

        for (String teamName : teamToSeekIn) {
            players.addAll(SurvivalRumbleData.getSingleton().playersByTeam.get(teamName));
        }

        ArrayList<UUID> res = new ArrayList<>();
        Random rnd = new Random();
        for (int i = pickNumber; i > 0; i--) {
            UUID pick = players.get(rnd.nextInt(players.size()));
            players.remove(pick);
            res.add(pick);
        }

        return res;
    }
}
