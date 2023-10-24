package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement;

public interface IDeathWorkflowStep {
    void runStep(DeathWorkflowData deathWorkflowData, EndStepCallback onStepEnds);
}
