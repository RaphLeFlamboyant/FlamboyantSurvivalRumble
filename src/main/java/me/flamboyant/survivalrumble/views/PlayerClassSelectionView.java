package me.flamboyant.survivalrumble.views;

import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.playerclass.factory.PlayerClassFactory;
import me.flamboyant.survivalrumble.utils.ItemHelper;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerClassSelectionView implements Listener {
    private static PlayerClassSelectionView instance;
    public PlayerClassType lastChosenClass;
    private ArrayList<Inventory> pages = new ArrayList<>();
    private int currentPage;
    private PlayerClassFactory factory;

    protected PlayerClassSelectionView() {
        factory = new PlayerClassFactory();
    }

    public static PlayerClassSelectionView getInstance() {
        if (instance == null) {
            instance = new PlayerClassSelectionView();
        }

        return instance;
    }

    public static String getViewID() {
        return "Class selection view";
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public Inventory getViewInstance() {
        if (pages.size() > 0) return pages.get(0);

        int i = 0;
        int size = PlayerClassHelper.playerClassMetadata.size();
        Inventory currentInventory = null;
        for (PlayerClassType classType : PlayerClassHelper.playerClassMetadata.keySet()) {
            if (i % 36 == 0) {
                if (currentInventory != null) {
                    if (i < size) currentInventory.setItem(53, getNextPageItem());
                    if (i > 36) currentInventory.setItem(45, getPreviousPageItem());
                    pages.add(currentInventory);
                }

                currentInventory = Bukkit.createInventory(null, 54, getViewID());
            }

            PlayerClassMetadata metadata = PlayerClassHelper.playerClassMetadata.get(classType);
            ItemStack item = getIntenvoryItem(classType.toString(), metadata.getPlayerClassRepresentation(), metadata.getDisplayName());
            currentInventory.setItem(i % 36, item);
            i++;
        }

        pages.add(currentInventory);
        return pages.get(0);
    }

    private ItemStack getNextPageItem() {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page suivante", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getPreviousPageItem() {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page précédente", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getIntenvoryItem(String className, Material material, String displayName) {
        return ItemHelper.generateItem(material, 1, displayName, Arrays.asList(className), false, null, true, false);
    }

    public Boolean isInView(Inventory inv) {
        return pages.contains(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != getViewInstance()) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir() || clicked.containsEnchantment(Enchantment.ARROW_DAMAGE))
            return;

        if (clicked.getItemMeta().getDisplayName() == "Page suivante") {
            if (pages.size() > currentPage - 1) currentPage++;
            player.openInventory(pages.get(currentPage));
        } else if (clicked.getItemMeta().getDisplayName() == "Page précédente") {
            if (0 < currentPage) currentPage--;
            player.openInventory(pages.get(currentPage));
        } else {
            String className = clicked.getItemMeta().getLore().get(0);
            APlayerClass playerClass = factory.generatePlayerClass(PlayerClassType.valueOf(className), player);
            lastChosenClass = playerClass.getClassType();
            PlayerClassMechanicsHelper.getSingleton().declarePlayerClass(player, playerClass);
            data().playersClass.put(player.getUniqueId(), playerClass.getClassType());

            Inventory currentInv = pages.get(currentPage);
            int i;
            for (i = 0; i < 36; i++) {
                ItemStack itemFromInv = currentInv.getItem(i);
                if (itemFromInv == null || itemFromInv.getType() == Material.AIR)
                    break;

                if (ItemHelper.isExactlySameItemKind(itemFromInv, clicked)) {
                    clicked.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                    clicked.getItemMeta().setDisplayName(clicked.getItemMeta().getDisplayName() + " - " + player.getDisplayName());
                }
            }

            currentPage = 0;
            player.closeInventory();
        }
        // TODO : bug potentiel : le mec ferme l'inventaire sans avoir sélectionné de classe -> mettre une sélection aléatoire ?
    }

    public void unregisterEvents() {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
