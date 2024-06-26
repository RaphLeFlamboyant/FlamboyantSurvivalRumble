package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MasonClass extends APlayerClass implements Listener {

    private Map<Material, Integer> moneyByMaterial = new HashMap<>() {{
        put(Material.BRICKS, 5);
        put(Material.NETHER_BRICKS, 5);
        put(Material.RED_NETHER_BRICKS, 8);
    }};

    public MasonClass(Player owner) {
        super(owner);

        scoringDescription = "Crafter des blocs à base de briques";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.MASON;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        CraftItemEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getWhoClicked() != owner) return;
        if (!moneyByMaterial.containsKey(event.getInventory().getResult().getType())) return;

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

        var earnedAmount = moneyByMaterial.get(event.getInventory().getResult().getType()) * realQuantity;
        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), earnedAmount);
    }
}
