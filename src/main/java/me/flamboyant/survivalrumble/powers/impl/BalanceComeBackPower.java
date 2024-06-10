package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.survivalrumble.gamecontrollers.assault.IAssaultStepListener;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BalanceComeBackPower extends AComeBackPower implements IAssaultStepListener {
    private int hpLimit = 10;
    private BukkitTask poisonTask;
    private List<Player> assaultPlayers = new ArrayList<>();

    @Override
    public void onTeamEliminated() {
        refreshAssaultPlayers();
    }

    @Override
    protected void onActivate() {
        AssaultManager.getInstance().addAssaultStepListener(this);
        refreshAssaultPlayers();
    }

    @Override
    protected void onDeactivate() {
        AssaultManager.getInstance().removeAssaultStepListener(this);
        if (hasAlreadyBeenUsed) {
            Bukkit.getScheduler().cancelTask(poisonTask.getTaskId());
        }
    }

    @Override
    protected void onPowerTriggered() {
        poisonTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> damageAssaultPlayers(), 20 * 5, 20 * 5);
    }

    @Override
    protected int getHealthTrigger() {
        return 10;
    }

    private void damageAssaultPlayers() {
        for (Player player : assaultPlayers) {
            if (player.getGameMode() == GameMode.SPECTATOR)
                continue;
            if (player.getHealth() <= hpLimit)
                continue;

            player.setHealth(player.getHealth() - 1);
        }
    }

    private void refreshAssaultPlayers() {
        assaultPlayers = SurvivalRumbleData.getSingleton().getAttackingPlayers(powerOwner);
    }
}
