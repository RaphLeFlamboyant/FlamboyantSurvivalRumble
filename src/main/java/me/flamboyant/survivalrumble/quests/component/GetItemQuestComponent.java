package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class GetItemQuestComponent extends AQuestComponent implements Listener {
    private Material material;
    private int quantity;

    public GetItemQuestComponent(Player player, Material material, int quantity) {
        super(player);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Obtiens " + quantity + " x " + material.toString();
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        EntityPickupItemEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity().getUniqueId() != player.getUniqueId()) return;
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType() != material) return;

        if (itemStack.getAmount() >= quantity || player.getInventory().containsAtLeast(itemStack, quantity - itemStack.getAmount())) {
            stopQuest();
        }
    }
}
