package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.assault.AssaultManager;
import me.flamboyant.survivalrumble.gamecontrollers.assault.IAssaultStepListener;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EnemiesDetectionPower implements IChampionPower, IAssaultStepListener {
    private Player powerOwner;
    private BukkitTask playerDetectionTask;
    private PotionEffect glowingEffect = new PotionEffect(PotionEffectType.GLOWING, 100, 1);
    private List<Player> assaultPlayers = new ArrayList<>();

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        AssaultManager.getInstance().addAssaultStepListener(this);
        refreshAssaultPlayers();
        playerDetectionTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> detectClosePlayers(), 40, 40);
    }

    @Override
    public void deactivate() {
        AssaultManager.getInstance().removeAssaultStepListener(this);
        Bukkit.getScheduler().cancelTask(playerDetectionTask.getTaskId());
    }

    private void detectClosePlayers() {

        for (var player : assaultPlayers) {
            if (player.getLocation().distance(powerOwner.getLocation()) > 30)
                continue;

            if (!player.hasPotionEffect(PotionEffectType.GLOWING))
                powerOwner.playSound(powerOwner, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);

            player.addPotionEffect(glowingEffect);
        }
    }

    @Override
    public void onTeamEliminated() {
        refreshAssaultPlayers();
    }

    private void refreshAssaultPlayers() {
        assaultPlayers = SurvivalRumbleData.getSingleton().getAttackingPlayers(powerOwner);
    }
}
