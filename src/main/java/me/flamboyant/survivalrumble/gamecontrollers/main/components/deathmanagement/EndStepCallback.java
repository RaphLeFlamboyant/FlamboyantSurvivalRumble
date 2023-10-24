package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface EndStepCallback {
    void goToNextStep(IDeathWorkflowStep nextStep, Player player);
}
