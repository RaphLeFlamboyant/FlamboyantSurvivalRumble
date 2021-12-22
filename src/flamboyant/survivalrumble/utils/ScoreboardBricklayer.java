package flamboyant.survivalrumble.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreboardBricklayer {
	private ScoreboardManager scoreBoardManager;
	private Scoreboard scoreboard;
	private HashMap<String, Team> teams;
	private HashMap<String, Objective> objectives;

	private static ScoreboardBricklayer instance;
	public static ScoreboardBricklayer getSingleton()
	{
		if (instance == null)
			instance = new ScoreboardBricklayer();
		
		return instance;		
	}
	
	private ScoreboardBricklayer()
	{
		scoreBoardManager = Bukkit.getScoreboardManager();
		scoreboard = scoreBoardManager.getMainScoreboard();
		
		objectives = new HashMap<>();
		for (Objective o : scoreboard.getObjectives())
		{
			objectives.put(o.getName(), o);
		}
		
		teams = new HashMap<String, Team>();		
		for (Team team : scoreboard.getTeams())
		{
			System.out.println("Ajout de la team " + team.getName() + " pour l'init de ScoreboardBricklayer");
			teams.put(team.getName(), team);
		}
	}
	
	public Team addNewTeam(String teamName)
	{
		Team team = scoreboard.registerNewTeam(teamName);
		teams.put(team.getName(), team);
		System.out.println("Test TEAM : " + teamName + " and is registered as " + team.getName());
		return team;
	}
	
	public void deleteTeam(String teamName)
	{
		teams.get(teamName).unregister();
		teams.remove(teamName);
	}
	
	public Team getTeam(String teamName)
	{
		if (teams.containsKey(teamName))
			return teams.get(teamName);
		
		return null;
	}
	
	public Objective createObjective(String objectiveName, String displayName, DisplaySlot slot)
	{
		Objective objective = scoreboard.registerNewObjective(objectiveName, "dummy", objectiveName);
		objective.setDisplaySlot(slot);
		objective.setDisplayName(displayName);
		objectives.put(objectiveName, objective);
		
		return objective;
	}
	
	public void setTeamScore(String objectiveName, String teamName, int score)
	{
		objectives.get(objectiveName).getScore(teamName).setScore(score);
	}
	
	public void deleteObjective(String objectiveName)
	{
		objectives.get(objectiveName).unregister();
	}
	
	public Objective getObjective(String objectiveName)
	{
		if (objectives.containsKey(objectiveName))
			return objectives.get(objectiveName);
		
		return null;
	}
	
	public void deleteAllObjectives()
	{
		for(Objective objective : objectives.values())
		{
			System.out.println("Unregister objective " + objective.getName());
			objective.unregister();
		}
	}
	
	public void deleteAllTeams()
	{
		for(Team team : teams.values())
		{
			System.out.println("Unregister team " + team.getName());
			team.unregister();
		}
	}
}
