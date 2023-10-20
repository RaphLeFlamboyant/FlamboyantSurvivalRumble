package me.flamboyant.survivalrumble.views.respawnmodeselection;

import me.flamboyant.gui.view.common.IInventoryGuiVisitor;
import me.flamboyant.gui.view.common.InventoryGui;
import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.survivalrumble.views.CallbackIconItem;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RespawnModeSelectionView {
    private static String viewName = "Respawn mode";
    private InventoryGui inventoryGui;

    public RespawnModeSelectionView() {
        IIconItem classicSpawnItem = new CallbackIconItem(() -> ClassicSpawnSelection(),
                "-",
                ItemHelper.generateItem(Material.WHITE_BED, 1, "Respawn classique", Arrays.asList("Spawn classique Minecraft"), false, Enchantment.ARROW_FIRE, false, false));
        IIconItem specialSpawnItem = new CallbackIconItem(() -> ClassicSpawnSelection(),
                "-",
                ItemHelper.generateItem(Material.WHITE_BED,
                        1,
                        "Respawn spécial",
                        Arrays.asList("Spawn à la base", "Shop de rachat de stuff", "Mode fantome pendant 10 secondes"),
                        false, Enchantment.ARROW_FIRE, false, false));


        Inventory inventory = Bukkit.createInventory(null, 9*3, viewName);
        inventory.setItem(13, classicSpawnItem.getItem());
        inventory.setItem(14, classicSpawnItem.getItem());

        inventoryGui = new InventoryGui(viewName, Arrays.asList(inventory), Arrays.asList(classicSpawnItem, specialSpawnItem), true);
    }

    public void open(Player player) {
        inventoryGui.open(player);
    }

    public void addVisitor(IInventoryGuiVisitor visitor) {
        inventoryGui.addVisitor(visitor);
    }

    public void removeVisitor(IInventoryGuiVisitor visitor) {
        inventoryGui.removeVisitor(visitor);
    }


    private void ClassicSpawnSelection () {

    }

    private void SpecialSpawnSelection() {

    }
}
