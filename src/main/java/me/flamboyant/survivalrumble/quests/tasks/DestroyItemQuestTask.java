package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DestroyItemQuestTask extends AQuestTask implements Listener {
    private Material itemToBreak;
    private int quantity;

    public DestroyItemQuestTask(Quest ownerQuest, Material itemToBreak, int quantity) {
        super(ownerQuest);

        this.itemToBreak = itemToBreak;
        this.quantity = quantity;

        subQuestMessage = "Détruis " + quantity + " fois l'objet " + itemToBreak.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        EntityDamageEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.DROPPED_ITEM) return;
        Item item = ((Item) event.getEntity());
        if (item.getItemStack().getType() != itemToBreak) return;
        if (event.getEntity().getLocation().getWorld() != player.getWorld()
                || event.getEntity().getLocation().distance(player.getLocation()) > 16) return;

        quantity -= item.getItemStack().getAmount();

        if (quantity <= 0) {
            stopQuest(true);
        }
    }
}
