package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.stephandlers;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.DeathWorkflowData;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowEventType;
import me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement.workflow.DeathWorkflowStepType;
import me.flamboyant.survivalrumble.shop.ItemShopHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamShopStepHandler extends AShopStepHandler {
    private String teamName;

    public TeamShopStepHandler(String teamName) {
        super(teamName + " death shop");
        this.teamName = teamName;
    }

    @Override
    protected DeathWorkflowStepType GetStepType() {
        return DeathWorkflowStepType.TEAM_SHOP;
    }

    @Override
    protected DeathWorkflowEventType GetEventType() {
        return DeathWorkflowEventType.END_OF_TEAM_SHOP;
    }

    @Override
    protected List<ItemStack> FilterKeptItem(List<ItemStack> keptItems) {
        return keptItems.stream()
                .filter((i) -> ItemShopHelper.teamShopMaterials.contains(i.getType()))
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

    @Override
    public void onNextStep(DeathWorkflowStepType deathWorkflowStepType, DeathWorkflowData deathWorkflowData) {
        if (SurvivalRumbleData.getSingleton().getPlayerTeam(deathWorkflowData.deadPlayer) != teamName) return;
        super.onNextStep(deathWorkflowStepType, deathWorkflowData);
    }
}
