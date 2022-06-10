package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DestroyItemQuestComponent extends AQuestComponent implements Listener {
    private Material itemToBreak;
    private int quantity;

    public DestroyItemQuestComponent(Player player, Material itemToBreak, int quantity) {
        super(player);

        this.itemToBreak = itemToBreak;
        this.quantity = quantity;

        subQuestMessage = "Détruis " + quantity + " fois l'objet " + itemToBreak.toString();
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        EntityDamageEvent.getHandlerList().unregister(this);
        super.stopQuest();
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
            stopQuest();
        }
    }
}
