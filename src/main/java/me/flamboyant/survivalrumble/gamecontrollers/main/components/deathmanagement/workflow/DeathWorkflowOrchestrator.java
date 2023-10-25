package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.workflow.WorkflowOrchestrator;
import me.flamboyant.workflow.WorkflowStep;

import java.util.List;

public class DeathWorkflowOrchestrator extends WorkflowOrchestrator<DeathWorkflowEventType, DeathWorkflowStepType, DeathWorkflowData> {
    private static DeathWorkflowOrchestrator instance;

    private DeathWorkflowOrchestrator(List<WorkflowStep<DeathWorkflowEventType, DeathWorkflowStepType>> workflowSteps) {
        super(workflowSteps);
    }

    public static DeathWorkflowOrchestrator getInstance() {
        if (instance == null) {
            instance = new DeathWorkflowOrchestrator(DeathWorkflowBuilder.buildWorkflowSteps());
        }

        return instance;
    }
}
