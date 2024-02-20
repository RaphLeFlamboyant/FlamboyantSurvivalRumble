package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.views;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommonShopHandler extends AShopHandler {
    @Override
    protected DeathWorkflowStepType GetStepType() {
        return DeathWorkflowStepType.COMMON_SHOP;
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
