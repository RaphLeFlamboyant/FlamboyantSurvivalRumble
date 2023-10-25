package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow;

import me.flamboyant.workflow.WorkflowStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeathWorkflowBuilder {
    public static List<WorkflowStep<DeathWorkflowEventType, DeathWorkflowStepType>> buildWorkflowSteps() {
        List<WorkflowStep<DeathWorkflowEventType, DeathWorkflowStepType>> result = new ArrayList<>();

        // WAIT_FOR_RESPAWN
        HashMap<DeathWorkflowEventType, DeathWorkflowStepType> stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.RESPAWN, DeathWorkflowStepType.RESPAWN_MODE_SELECTION);
        }};
        WorkflowStep<DeathWorkflowEventType, DeathWorkflowStepType> step = new WorkflowStep<>(DeathWorkflowStepType.WAIT_FOR_RESPAWN, stepTriggers);
        result.add(step);

        // RESPAWN_MODE_SELECTION
        stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.SELECT_NORMAL_RESPAWN, DeathWorkflowStepType.NORMAL_RESPAWN);
            put(DeathWorkflowEventType.SELECT_SPECIAL_RESPAWN, DeathWorkflowStepType.PERSONAL_SHOP);
        }};
        step = new WorkflowStep<>(DeathWorkflowStepType.RESPAWN_MODE_SELECTION, stepTriggers);
        result.add(step);

        // NORMAL_RESPAWN
        stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.RESPAWN, DeathWorkflowStepType.END);
        }};
        step = new WorkflowStep<>(DeathWorkflowStepType.NORMAL_RESPAWN, stepTriggers);
        result.add(step);

        // PERSONAL_SHOP
        stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.END_OF_PERSONAL_SHOP, DeathWorkflowStepType.COMMON_SHOP);
        }};
        step = new WorkflowStep<>(DeathWorkflowStepType.PERSONAL_SHOP, stepTriggers);
        result.add(step);

        // COMMON_SHOP
        stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.END_OF_COMMON_SHOP, DeathWorkflowStepType.GHOST_MODE);
        }};
        step = new WorkflowStep<>(DeathWorkflowStepType.COMMON_SHOP, stepTriggers);
        result.add(step);

        // GHOST_MODE
        stepTriggers = new HashMap<DeathWorkflowEventType, DeathWorkflowStepType>() {{
            put(DeathWorkflowEventType.END_OF_GHOST_MODE, DeathWorkflowStepType.END);
        }};
        step = new WorkflowStep<>(DeathWorkflowStepType.GHOST_MODE, stepTriggers);
        result.add(step);

        return result;
    }
}
