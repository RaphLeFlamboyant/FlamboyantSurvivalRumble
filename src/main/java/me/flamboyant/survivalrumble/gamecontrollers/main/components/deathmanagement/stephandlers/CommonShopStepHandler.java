package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommonShopStepHandler extends AShopStepHandler {
    @Override
    protected DeathWorkflowStepType GetStepType() {
        return DeathWorkflowStepType.COMMON_SHOP;
    }

    @Override
    protected DeathWorkflowEventType GetEventType() {
        return DeathWorkflowEventType.END_OF_COMMON_SHOP;
    }

    @Override
    protected String GetViewName() {
        return "Common death shop";
    }

    @Override
    protected List<ItemStack> FilterKeptItem(List<ItemStack> keptItems) {
        // TODO
    }

    @Override
    protected int getUnitaryPrice(ItemStack item) {
        // TODO
    }
}
