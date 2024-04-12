package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class EnnemyDetectionPower implements IChampionPower {
    private Player powerOwner;
    private BukkitTask playerDetectionTask;
    private PotionEffect glowingEffect = new PotionEffect(PotionEffectType.GLOWING, 100, 1);

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        playerDetectionTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> detectClosePlayers(), 40, 40);
    }

    @Override
    public void deactivate() {
        Bukkit.getScheduler().cancelTask(playerDetectionTask.getTaskId());
    }

    private void detectClosePlayers() {
        var data = SurvivalRumbleData.getSingleton();
        var assaultTeamName = data.getTeamAssaultTeam(data.getPlayerTeam(powerOwner));

        for (var player : data.getPlayers(assaultTeamName)) {
            if (player == data.getTeamChampion(assaultTeamName))
                continue;

            if (player.getLocation().distance(powerOwner.getLocation()) > 30)
                continue;

            if (!player.hasPotionEffect(PotionEffectType.GLOWING))
                powerOwner.playSound(powerOwner, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);

            player.addPotionEffect(glowingEffect);
        }
    }
}
