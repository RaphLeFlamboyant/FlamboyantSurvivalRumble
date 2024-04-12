package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class BalanceComeBackPower extends AComeBackPower {
    private BukkitTask poisonTask;

    @Override
    protected void onDeactivate() {
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
        var players = SurvivalRumbleData.getSingleton().getAttackingPlayers(powerOwner);
        for (Player player : players) {
            player.setHealth(player.getHealth() - 1);
        }
    }
}
