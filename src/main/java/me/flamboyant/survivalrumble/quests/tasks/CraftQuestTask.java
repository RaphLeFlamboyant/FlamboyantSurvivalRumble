package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftQuestTask extends AQuestTask implements Listener {
    private Material material;
    private int quantity;

    public CraftQuestTask(Quest ownerQuest, Material material, int quantity) {
        super(ownerQuest);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Craft " + quantity + " x " + material.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        CraftItemEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    protected boolean checkOtherConditions(CraftItemEvent event) {
        return true;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        System.out.println(" ----- CraftItemEvent by " + event.getWhoClicked().getName() + " ----- ");
        if (!event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) return;
        System.out.println("Crafted : " + event.getInventory().getResult().getType());
        if (event.getInventory().getResult().getType() != material) return;
        ItemStack cursorStack = event.getCursor();
        System.out.println("Cursor is shift : " + event.isShiftClick() + (!event.isShiftClick() ? "; Cursor content : " + cursorStack.getAmount() + " x " + cursorStack.getType() : ""));
        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getType() != cursorStack.getType())
            return;
        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getAmount() + cursorStack.getAmount() > cursorStack.getMaxStackSize())
            return;
        if (!checkOtherConditions(event)) return;

        int realQuantity = 9999;
        if (event.isShiftClick()) {
            int factor = event.getInventory().getResult().getAmount();
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null) realQuantity = Math.min(realQuantity, item.getAmount() * factor);
            }
            System.out.println("Shift click gave " + realQuantity + " quantity");
        } else {
            realQuantity = event.getInventory().getResult().getAmount();
            System.out.println("Normal click gave " + realQuantity + " quantity");
        }

        // TODO ; gérer le cas inventaire full

        quantity -= realQuantity;
        System.out.println("Remaining quantity : " + quantity);

        if (quantity <= 0) {
            stopQuest(true);
        }
    }
}
