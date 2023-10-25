package me.flamboyant.survivalrumble.views.respawnmodeselection;

import me.flamboyant.gui.view.builder.InventoryGuiBuilder;
import me.flamboyant.gui.view.common.IInventoryGuiVisitor;
import me.flamboyant.gui.view.common.InventoryGui;
import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.survivalrumble.utils.PlayerCallback;
import me.flamboyant.survivalrumble.views.CallbackIconItem;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RespawnModeSelectionView {
    private static String viewName = "Respawn mode";
    private InventoryGui inventoryGui;

    public RespawnModeSelectionView(PlayerCallback onClassicSpawnSelection, PlayerCallback onSpecialSpawnSelection) {
        IIconItem classicSpawnItem = new CallbackIconItem(onClassicSpawnSelection,
                "-",
                ItemHelper.generateItem(Material.WHITE_BED, 1, "Respawn classique", Arrays.asList("Spawn classique Minecraft"), false, Enchantment.ARROW_FIRE, false, false),
                true);
        IIconItem specialSpawnItem = new CallbackIconItem(onSpecialSpawnSelection,
                "-",
                ItemHelper.generateItem(Material.WHITE_BED,
                        1,
                        "Respawn spécial",
                        Arrays.asList("Spawn à la base", "Shop de rachat de stuff", "Mode fantome pendant 10 secondes"),
                        false, Enchantment.ARROW_FIRE, false, false),
                true);

        inventoryGui = InventoryGuiBuilder.getInstance().buildView(Arrays.asList(classicSpawnItem, specialSpawnItem), viewName, 9*3, true);
    }

    public void open(Player player) {
        inventoryGui.open(player);
    }
}
