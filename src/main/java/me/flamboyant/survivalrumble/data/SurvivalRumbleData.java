package me.flamboyant.survivalrumble.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SurvivalRumbleData implements Serializable {
    private static final long serialVersionUID = -4333287600817371123L;
    private static String dataSaveFile = "survival_rumble_save.json";
    private static SurvivalRumbleData instance;
    // Registered on command
    public ArrayList<String> teams = new ArrayList<String>();
    public UUID opPlayer;
    // Registered in team selection view
    public HashMap<UUID, String> playersTeam = new HashMap<UUID, String>();
    public HashMap<String, UUID> players = new HashMap<String, UUID>();
    public HashMap<String, Integer> teamMalus = new HashMap<String, Integer>();
    // Registered in game setup view
    public int minutesBeforeEnd = 240;
    public int pvpIntensity = 2;
    public Material selectedStuff = Material.BEEF;
    // Registered in teams parameter view
    public HashMap<String, Location> teamHeadquarterLocation = new HashMap<String, Location>();
    public HashMap<String, Location> teamSpawnLocation = new HashMap<String, Location>();
    // Registered in class selection view
    public Map<String, List<UUID>> playersByTeam = new HashMap<>();
    public HashMap<UUID, PlayerClassType> playersClass = new HashMap<UUID, PlayerClassType>();
    // Registered in game setup listener when launching game start (game starts with the class selection)
    public HashMap<String, Integer> teamReversibleScores = new HashMap<String, Integer>();
    public HashMap<String, Integer> teamFlatScores = new HashMap<String, Integer>();
    public HashMap<String, Integer> teamPerfectScores = new HashMap<String, Integer>();
    public List<Integer> meetupTimer = new ArrayList<Integer>();

    protected SurvivalRumbleData() {
    }

    public static SurvivalRumbleData getSingleton() {
        if (instance == null)
            instance = new SurvivalRumbleData();

        return instance;
    }

    public static Boolean loadData() {
        try {
            File f = new File(dataSaveFile);
            if (!f.exists()) {
                return false;
            }

            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(dataSaveFile)));
            SurvivalRumbleData toto = (SurvivalRumbleData) in.readObject();
            instance = toto;


            Bukkit.broadcastMessage("é6  - playersTeam : " + toto.playersTeam.size());
            for (UUID key : toto.playersTeam.keySet()) {
                Bukkit.broadcastMessage("    > " + key + " - " + toto.playersTeam.get(key));
            }
            Bukkit.broadcastMessage("é6  - playersTeam : " + instance.playersTeam.size());
            for (UUID key : instance.playersTeam.keySet()) {
                Bukkit.broadcastMessage("    > " + key + " - " + instance.playersTeam.get(key));
            }


            in.close();

            return true;
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalScore(String teamName) {
        if (teamReversibleScores.containsKey(teamName) && teamFlatScores.containsKey(teamName) && teamPerfectScores.containsKey(teamName))
            return teamReversibleScores.get(teamName) + teamFlatScores.get(teamName) + teamPerfectScores.get(teamName);

        return 0; // TODO : guard
    }

    public boolean saveData() {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(dataSaveFile)));
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
