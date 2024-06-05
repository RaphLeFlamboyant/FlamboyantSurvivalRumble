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

public class EngineerClass extends APlayerClass implements Listener {
    private HashMap<Material, Float> materialToScore = new HashMap<>() {{
        put(Material.ACACIA_BUTTON, 0.2f);
        put(Material.BAMBOO_BUTTON, 0.2f);
        put(Material.BIRCH_BUTTON, 0.2f);
        put(Material.CHERRY_BUTTON, 0.2f);
        put(Material.CRIMSON_BUTTON, 0.2f);
        put(Material.DARK_OAK_BUTTON, 0.2f);
        put(Material.JUNGLE_BUTTON, 0.2f);
        put(Material.MANGROVE_BUTTON, 0.2f);
        put(Material.OAK_BUTTON, 0.2f);
        put(Material.POLISHED_BLACKSTONE_BUTTON, 0.2f);
        put(Material.STONE_BUTTON, 0.2f);
        put(Material.WARPED_BUTTON, 0.2f);

        put(Material.ACACIA_PRESSURE_PLATE, 0.2f);
        put(Material.BAMBOO_PRESSURE_PLATE, 0.2f);
        put(Material.BIRCH_PRESSURE_PLATE, 0.2f);
        put(Material.CHERRY_PRESSURE_PLATE, 0.2f);
        put(Material.CRIMSON_PRESSURE_PLATE, 0.2f);
        put(Material.DARK_OAK_PRESSURE_PLATE, 0.2f);
        put(Material.JUNGLE_PRESSURE_PLATE, 0.2f);
        put(Material.MANGROVE_PRESSURE_PLATE, 0.2f);
        put(Material.OAK_PRESSURE_PLATE, 0.2f);
        put(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, 0.2f);
        put(Material.STONE_PRESSURE_PLATE, 0.2f);
        put(Material.WARPED_PRESSURE_PLATE, 0.2f);
        put(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 0.2f);
        put(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 0.2f);

        put(Material.LEVER, 0.2f);
        put(Material.REDSTONE_TORCH, 0.4f);
        put(Material.CALIBRATED_SCULK_SENSOR, 20f);
        put(Material.CHISELED_BOOKSHELF, 0.3f);
        put(Material.DAYLIGHT_DETECTOR, 5f);
        put(Material.DETECTOR_RAIL, 10f);
        put(Material.JUKEBOX, 5f);
        put(Material.OBSERVER, 5f);
        put(Material.TARGET, 1f);
        put(Material.TRAPPED_CHEST, 1f);
        put(Material.TRIPWIRE_HOOK, 2f);
        put(Material.REPEATER, 2f);
        put(Material.COMPARATOR, 2f);
        put(Material.ACTIVATOR_RAIL, 10f);
        put(Material.DISPENSER, 5f);
        put(Material.DROPPER, 1f);
        put(Material.HOPPER, 10f);
        put(Material.PISTON, 3f);
        put(Material.POWERED_RAIL, 15f);
        put(Material.RAIL, 1f);
        put(Material.REDSTONE_LAMP, 2f);
        put(Material.TNT, 2f);
        put(Material.STICKY_PISTON, 2f);
        put(Material.SLIME_BLOCK, 5f);
        put(Material.HONEY_BLOCK, 5f);
    }};

    private float leftovers = 0f;

    public EngineerClass(Player owner) {
        super(owner);

        scoringDescription = "Crafter des items de redstone rapport de l'argent";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ENGINEER;
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
        if (!event.getWhoClicked().equals(owner)) return;
        if (!materialToScore.containsKey(event.getInventory().getResult().getType())) return;

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

        float amount = materialToScore.get(event.getInventory().getResult().getType()) * realQuantity;
        amount += leftovers;
        leftovers = amount % 1f;

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), (int) amount);
    }
}
