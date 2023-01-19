package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class GetItemQuestTask extends AQuestTask implements Listener {
    private Material material;
    private int quantity;

    public GetItemQuestTask(Quest ownerQuest, Material material, int quantity) {
        super(ownerQuest);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Obtiens " + quantity + " x " + material.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        EntityPickupItemEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity().getUniqueId() != player.getUniqueId()) return;
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType() != material) return;

        if (itemStack.getAmount() >= quantity || player.getInventory().containsAtLeast(itemStack, quantity - itemStack.getAmount())) {
            stopQuest(true);
        }
    }
}
