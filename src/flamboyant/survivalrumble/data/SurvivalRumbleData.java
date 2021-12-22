package flamboyant.survivalrumble.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class SurvivalRumbleData implements Serializable {
	// Registered on command
	public ArrayList<String> teams = new ArrayList<String>();
	public UUID opPlayer;

	// Registered in team selection view
	public HashMap<UUID, String> playersTeam  = new HashMap<UUID, String>();
	public HashMap<String, UUID> players = new HashMap<String, UUID>();

	// Registered in game setup view
	public int minutesBeforeEnd = Integer.MAX_VALUE;
	public int pvpIntensity = 0;

	// Registered in HQ parameter view
	public HashMap<String, Location> teamHeadquarterLocation = new HashMap<String, Location>();

	// Registered in class selection view
	public Map<String, List<UUID>> playersByTeam = new HashMap<>();
	public HashMap<UUID, PlayerClassType> playersClass = new HashMap<UUID, PlayerClassType>();

	// Registered in game setup listener when launching game start (game starts with the class selection)
	public HashMap<String, Integer> teamScores  = new HashMap<String, Integer>();


	public List<Integer> meetupTimer = new ArrayList<Integer>();

	private static final long serialVersionUID = -4333287600817371123L;
	private static String dataSaveFile = "survival_rumble_save.json";
	
	private static SurvivalRumbleData instance;
	public static SurvivalRumbleData getSingleton()
	{
		if (instance == null)
			instance = new SurvivalRumbleData();
		
		return instance;		
	}
	
	protected SurvivalRumbleData() {}
	
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
	
	public static Boolean loadData() {
        try {
        	File f = new File(dataSaveFile);
        	if(!f.exists()) {
        		return false;
        	}

            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(dataSaveFile)));
            SurvivalRumbleData toto = (SurvivalRumbleData) in.readObject();
            instance = toto;
            

			Bukkit.broadcastMessage("§6  - playersTeam : " + toto.playersTeam.size());	
			for(UUID key : toto.playersTeam.keySet())
			{
				Bukkit.broadcastMessage("    > " + key + " - " + toto.playersTeam.get(key));
			}
			Bukkit.broadcastMessage("§6  - playersTeam : " + instance.playersTeam.size());	
			for(UUID key : instance.playersTeam.keySet())
			{
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
}
