package me.flamboyant.survivalrumble.views.respawnmodeselection;

import me.flamboyant.gui.view.IconController;
import me.flamboyant.gui.view.InventoryView;
import me.flamboyant.survivalrumble.delegates.RunOnPlayerCallback;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RespawnModeSelectionView {
    private static String viewName = "Respawn mode";
    private InventoryView inventoryView;

    public RespawnModeSelectionView(RunOnPlayerCallback onClassicSpawnSelection, RunOnPlayerCallback onSpecialSpawnSelection) {
        IconController classicIconController = new IconController();//1);
        classicIconController.setItemIcon(ItemHelper.generateItem(Material.WHITE_BED, 1, "Respawn classique", Arrays.asList("Spawn classique Minecraft"), false, Enchantment.ARROW_FIRE, false, false));
        classicIconController.setLeftClickCallback((p) -> onClassicSpawnSelection.runOnPlayer(p));

        IconController specialIconController = new IconController();//2);
        specialIconController.setItemIcon(
                ItemHelper.generateItem(Material.WHITE_BED,
                        1,
                        "Respawn spécial",
                        Arrays.asList("Spawn à la base", "Shop de rachat de stuff", "Mode fantome pendant 10 secondes"),
                        false, Enchantment.ARROW_FIRE, false, false));
        specialIconController.setLeftClickCallback((p) -> onSpecialSpawnSelection.runOnPlayer(p));

        inventoryView = new InventoryView(viewName, Arrays.asList(classicIconController, specialIconController));
    }

    public void open(Player player) {
        inventoryView.openPlayerView(player);
    }

    public void close(Player player) {
        inventoryView.closePlayerView(player);
    }
}
