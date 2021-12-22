package flamboyant.survivalrumble.playerclass.classobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;

public abstract class APlayerClass {
	protected Player owner;
	protected Map<String, Object> parameters = new HashMap<>();
	protected SurvivalRumbleData data()
	{
		return SurvivalRumbleData.getSingleton();
	}

	public ArrayList<ScoringTriggerType> triggers = new ArrayList<ScoringTriggerType>();

	public APlayerClass(Player owner)
	{
		this.owner = owner;
	}

	public abstract PlayerClassType getClassType();
	public abstract void gameStarted(Server server, Plugin plugin);
	// TODO : ajouter un game ended

	public void setParameters(Map<String, Object> parameters)
	{
		this.parameters = parameters;
	}
	
	public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned)
	{
		return score;
	}
	
	public void onPlayerHurtTrigger()
	{
	}
	
	public void onPlayerDeathTrigger(Player killed, Player killer)
	{
	}
	
	public void onTimerTrigger()
	{
	}
	
	public void onChestModificationTrigger()
	{
	}

	public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block)
	{
	}
	
	public void onExplosionTrigger(Block block)
	{
	}
	
	public void onBlockBurnedTrigger(Block block)
	{
	}

	public void onBlockBreakTrigger(Player playerWhoBreaks, Block block)
	{
	}

	protected void changeScore(String teamName, int modifier)
	{
		int points = data().teamScores.get(teamName) + modifier;
		ScoreboardBricklayer.getSingleton().setTeamScore("Score", teamName, points);
		data().teamScores.put(teamName, points);
		data().saveData();
	}
}
