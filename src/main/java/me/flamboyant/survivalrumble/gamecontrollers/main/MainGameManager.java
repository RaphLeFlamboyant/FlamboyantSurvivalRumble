package me.flamboyant.survivalrumble.gamecontrollers.main;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.*;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.PlayerDeathManager;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class MainGameManager implements Listener, ITriggerVisitor {
    private GameTimeManager timeManager = new GameTimeManager();
    private List<ChampionShopManager> championShopManagers = new ArrayList<>();
    private QuestListener questListener = new QuestListener();
    private BukkitTask questTask;
    private boolean isLaunched;

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    private static MainGameManager instance;
    protected MainGameManager() {

    }

    public static MainGameManager getInstance() {
        if (instance == null) {
            instance = new MainGameManager();
        }

        return instance;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void start() {
        if (isLaunched) {
            Bukkit.broadcastMessage(ChatHelper.feedback("Le MainGameManager est déjà lancé"));
            return;
        }

        PlayerClassMechanicsHelper.getSingleton().enablePlayersClasses();
        PlayerDeathManager.getInstance().start();
        timeManager.launchGameTimeManagement(this);

        for (String teamName : data().getTeams()) {
            ChampionShopManager championShopManager = new ChampionShopManager(teamName);
            championShopManagers.add(championShopManager);
            championShopManager.start();
        }

        //questListener;
        //// questTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> handleQuests(), 15 * 60 * 20L, 5 * 60 * 20L);

        isLaunched = true;

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        if (!isLaunched) {
            Bukkit.broadcastMessage(ChatHelper.feedback("Le MainGameManager n'est pas déjà lancé"));
            return;
        }

        PlayerClassMechanicsHelper.getSingleton().disablePlayersClasses();
        PlayerDeathManager.getInstance().stop();
        for (ChampionShopManager championShopManager : championShopManagers) {
            championShopManager.stop();
        }
        //questListener;

        isLaunched = false;

        PlayerJoinEvent.getHandlerList().unregister(this);
    }
    /*
    private void handleQuests() {
        for(UUID playerId : data().players.values()) {
            QuestPoolType poolType = PlayerClassMechanicsHelper.getSingleton().getQuestPoolType(playerId);
            QuestHelper.getQuestPool(poolType).startRandomQuest(Common.server.getPlayer(playerId));
        }
    }
    */

    // On timer's end
    @Override
    public void onAction() {
        stop();
        AssaultManager.getInstance().start();
    }

    @EventHandler
    public void onPlayerConnects(PlayerJoinEvent event) {
        var player = event.getPlayer();
        SurvivalRumbleData.getSingleton().onConnect(player);

        if (SurvivalRumbleData.getSingleton().getPlayerTeam(player) != null) return;

        player.setGameMode(GameMode.SPECTATOR);
    }
}
