package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class NudistClass extends AAttackClass {
    private static final int checkInterval = 1;
    private static final float notNudeMalusWeight = 4f;
    private static final float biggestGainPerSecond = 25f;

    private long lastArmorEquippedCount;
    private BukkitTask intervalTask;
    private boolean cancelGain = false;

    public NudistClass(Player owner) {
        super(owner);
    }

    @Override
    protected float getMalusRatio() {
        return 0.04f;
    }

    @Override
    protected double getValidationDistance() {
        return 32;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.NUDIST;
    }

    @Override
    public void enableClass() {
        super.enableClass();

        intervalTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    @Override
    public void disableClass() {
        Bukkit.getScheduler().cancelTask(intervalTask.getTaskId());
    }

    private void updateScoring() {
        var playerInventory = owner.getInventory();
        var armorEquippedCount = Arrays.stream(playerInventory.getArmorContents()).filter(a -> a != null && a.getType() != Material.AIR).count();

        if (lastArmorEquippedCount != armorEquippedCount) {
            cancelGain = true;
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> cancelGain = false, 15 * 60 * 20);
        }
        lastArmorEquippedCount = armorEquippedCount;

        if (cancelGain) return;

        var ratio = 1f;

        if (armorEquippedCount > 0) {
            ratio = armorEquippedCount / (notNudeMalusWeight + playerInventory.getArmorContents().length);
        }

        var emptySlotCount = Arrays.stream(playerInventory.getContents()).filter(a -> a == null ||a.getType() == Material.AIR).count();
        ratio *= emptySlotCount / ((float)playerInventory.getSize());

        applyAmount(ratio * biggestGainPerSecond);
    }
}
