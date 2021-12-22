package flamboyant.survivalrumble.utils;

import java.util.*;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.plugin.Plugin;

public class PlayerClassMechanicsHelper
{
	public HashMap<ScoringTriggerType, List<APlayerClass>> connectedClasses = new HashMap<ScoringTriggerType, List<APlayerClass>>();
	private SurvivalRumbleData data()
	{
		return SurvivalRumbleData.getSingleton();
	}
	private HashMap<UUID, APlayerClass> playerClasses = new HashMap<>();
	
	private static PlayerClassMechanicsHelper instance;
	public static PlayerClassMechanicsHelper getSingleton()
	{
		if (instance == null)
			instance = new PlayerClassMechanicsHelper();
		
		return instance;		
	}
	
	protected PlayerClassMechanicsHelper()
	{
		connectedClasses.put(ScoringTriggerType.BLOCK_BREAK, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.BLOCK_BURNED, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.BLOCK_EXPLOSION, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.BLOCK_MODIFIER, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.CHEST_MODIFICATION, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.DEATH, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.HURT, new ArrayList<APlayerClass>());
		connectedClasses.put(ScoringTriggerType.ON_TIMER, new ArrayList<APlayerClass>());
	}
	
	private void addPlayerClassToTriggers(APlayerClass playerClass)
	{
		for (ScoringTriggerType trigger : playerClass.triggers)
		{
			System.out.println("Setup trigger " + trigger.toString() + " for class " + playerClass.getClassType().toString());
			connectedClasses.get(trigger).add(playerClass);
		}
	}
	
	public void declarePlayerClass(Player player, APlayerClass playerClass)
	{
		addPlayerClassToTriggers(playerClass);

		playerClasses.put(player.getUniqueId(), playerClass);
	}

	public void initClassesAtGameLaunch(Server server, Plugin plugin)
	{
		for(UUID playerId : playerClasses.keySet())
		{
			playerClasses.get(playerId).gameStarted(server, plugin);
		}
	}
}
