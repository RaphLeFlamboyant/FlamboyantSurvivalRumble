package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.shop.ItemShopHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return keptItems.stream()
                .filter((i) -> !ItemShopHelper.teamShopMaterials.contains(i.getType()))
                .collect(Collectors.toList());
    }

    @Override
    protected int getUnitaryPrice(ItemStack item) {
        int price = ItemShopHelper.materialToPrice.get(item.getType());
        Map<Enchantment, Integer> enchantmentToLevel = item.getEnchantments();

        for (int enchantmentLevel : enchantmentToLevel.values()) {
            price += enchantmentLevel * 2;
        }

        return price;
    }
}
