package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.steps;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.EndStepCallback;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.IDeathWorkflowStep;

import java.util.HashMap;
import java.util.HashSet;

public class WaitForRespawnStep implements IDeathWorkflowStep {
    private IDeathWorkflowStep nextStep;
    private HashSet<DeathWorkflowData> deathWorkflowDataSet = new HashSet<>();

    public WaitForRespawnStep(IDeathWorkflowStep nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public void runStep(DeathWorkflowData deathWorkflowData, EndStepCallback onStepEnds) {
        deathWorkflowDataSet.add(deathWorkflowData);
    }
}
