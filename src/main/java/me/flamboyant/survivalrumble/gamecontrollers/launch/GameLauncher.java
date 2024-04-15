package me.flamboyant.survivalrumble.gamecontrollers.launch;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.classselection.ClassSelectionListener;
import me.flamboyant.survivalrumble.gamecontrollers.classselection.IClassSelectionVisitor;
import me.flamboyant.survivalrumble.gamecontrollers.main.MainGameManager;
import me.flamboyant.survivalrumble.gamecontrollers.setup.SetupListener;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.playerclass.factory.PlayerClassFactory;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.survivalrumble.gamecontrollers.main.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.utils.ScoreHelper;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public class GameLauncher implements ITriggerVisitor, IClassSelectionVisitor {
    private boolean isLaunched;

    public boolean isLaunched() {
        return isLaunched;
    }

    public void start(Player opPlayer) {
        if (isLaunched || MainGameManager.getInstance().isLaunched()) {
            Bukkit.broadcastMessage(ChatHelper.feedback("Le plugin est déjà lancé"));
            return;
        }

        SetupListener.getInstance().launch(opPlayer, this);

        isLaunched = true;
    }

    public void stop() {
        if (!isLaunched && !MainGameManager.getInstance().isLaunched()) {
            Bukkit.broadcastMessage(ChatHelper.feedback("Le plugin n'est pas lancé"));
            return;
        }

        if (MainGameManager.getInstance().isLaunched())
            MainGameManager.getInstance().stop();

        close();
    }

    @Override
    public void onAction() {
        String message = "Voici l'ordre d'attaque des équipes : ";
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        for (String teamName : data.getTeams()) {
            message += "\nL'équipe " + data.getTeamAssaultTeam(teamName) + " attaquera le champions de l'équipe " + teamName;
        }

        Bukkit.broadcastMessage(message);

        ClassSelectionListener.getInstance().start(this);
    }

    @Override
    public void onClassesSelected(HashMap<String, String> playerClasses) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        ScoreboardBricklayer sbl = ScoreboardBricklayer.getSingleton();
        Objective scoreObj = sbl.createObjective("Score", "Score", DisplaySlot.SIDEBAR);

        for (String team : data.getTeams()) {
            sbl.addNewTeam(team);
            scoreObj.getScore(team).setScore(0);
        }

        for (Player player : Common.server.getOnlinePlayers()) {
            if (!playerClasses.containsKey(player.getDisplayName())) continue;

            APlayerClass playerClass = PlayerClassFactory.generatePlayerClass(PlayerClassType.valueOf(playerClasses.get(player.getDisplayName())), player);
            PlayerClassMechanicsHelper.getSingleton().declarePlayerClass(player, playerClass);
            data.setPlayerClass(player, playerClass);

            String team = SurvivalRumbleData.getSingleton().getPlayerTeam(player);
            sbl.getTeam(team).addPlayer(player);
            GameManager.getInstance().addAddMoney(team, playerClass.getScoreMalus());
        }

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> launchOnCountdown(5), 20);
    }

    private void close() {
        if (isLaunched) {
            SetupListener.getInstance().close();
            ClassSelectionListener.getInstance().stop();
            isLaunched = false;
        }
    }

    private void launchOnCountdown(int countdown) {
        if (countdown > 0) {
            for (Player player : Common.server.getOnlinePlayers()) {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0.8f + 0.1f * countdown);
                player.sendTitle("" + countdown, "", 0, 10, 10);
            }

            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> launchOnCountdown(countdown - 1), 20);
            return;
        }

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        for (Player player : Common.server.getOnlinePlayers()) {
            if (data.getPlayerTeam(player) == null) continue;

            Location spawn = data.getHeadquarterLocation(data.getPlayerTeam(player));
            // TODO : launch message
            player.teleport(spawn);
        }

        MainGameManager.getInstance().start();

        close();
    }
}
