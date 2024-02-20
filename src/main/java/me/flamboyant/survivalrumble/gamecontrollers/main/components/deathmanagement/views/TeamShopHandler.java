package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TeamShopHandler extends AShopHandler {
    private String teamName;

    public TeamShopHandler(String teamName) {
        this.teamName = teamName;
    }

    @Override
    protected DeathWorkflowStepType GetStepType() {
        return DeathWorkflowStepType.TEAM_SHOP;
    }

    @Override
    protected List<ItemStack> FilterKeptItem(List<ItemStack> keptItems) {
        // TODO
    }

    @Override
    protected int getUnitaryPrice(ItemStack item) {
        // TODO
    }

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (SurvivalRumbleData.getSingleton().getPlayerTeam(deathWorkflowData.deadPlayer) != teamName) return;
        super.onNextStep(deathWorkflowStepType, deathWorkflowData);
    }
}
