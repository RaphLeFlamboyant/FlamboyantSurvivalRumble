package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import me.flamboyant.survivalrumble.data.PlayerClassCategory;
import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerClassSelectionView implements Listener {
    private static PlayerClassSelectionView instance;
    private ArrayList<Inventory> pages = new ArrayList<>();
    private int currentPage;
    private HashMap<String, String> playerClassSelection = new HashMap<>();
    private ITriggerVisitor visitor;

    private HashMap<PlayerClassCategory, String> categoryToName = new HashMap<>() {{
        put(PlayerClassCategory.SURVIVAL, "Survie");
        put(PlayerClassCategory.ATTACK, "Attaque");
        put(PlayerClassCategory.PVP, "PvP");
        put(PlayerClassCategory.DEFENSE, "Défense");
        put(PlayerClassCategory.REDSTONE, "Redstone");
        put(PlayerClassCategory.NON_VANILLA, "Non Vanilla");
    }};

    protected PlayerClassSelectionView() {

    }

    public static PlayerClassSelectionView getInstance() {
        if (instance == null) {
            instance = new PlayerClassSelectionView();
        }

        return instance;
    }

    public HashMap<String, String> getPlayerClassSelection() {
        return playerClassSelection;
    }

    public void start(ITriggerVisitor visitor) {
        this.visitor = visitor;
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        InventoryClickEvent.getHandlerList().unregister(this);
        playerClassSelection.clear();
        currentPage = 0;
        visitor = null;
    }

    public static String getViewID() {
        return "Class selection view";
    }

    public Inventory getViewInstance() {
        if (pages.size() > 0) return pages.get(0);

        var currentInventory = Bukkit.createInventory(null, 54, getViewID());
        pages.add(currentInventory);

        var classMetadataList = new ArrayList<>(PlayerClassHelper.playerClassMetadata.values());
        Collections.sort(classMetadataList, (PlayerClassMetadata a, PlayerClassMetadata b) ->
                a.getPlayerClassCategory() == b.getPlayerClassCategory() ? a.getDisplayName().compareTo(b.getDisplayName()) : a.getPlayerClassCategory().compareTo(b.getPlayerClassCategory()));

        var currentCategory = PlayerClassCategory.SURVIVAL;
        for (PlayerClassMetadata classMetadata : classMetadataList) {
            if (classMetadata.getPlayerClassType() == PlayerClassType.FAKE_CLASS) continue;

            if (currentCategory != classMetadata.getPlayerClassCategory()) {
                currentCategory = classMetadata.getPlayerClassCategory();
                var pane = new ItemStack(Material.GLASS_PANE);
                var meta = pane.getItemMeta();
                meta.setDisplayName(categoryToName.get(currentCategory));
                pane.setItemMeta(meta);

                handleItemAdd(pane);
            }
            ItemStack item = getInventoryItem(classMetadata.getPlayerClassType().toString(), classMetadata.getPlayerClassRepresentation(), classMetadata.getDisplayName());
            handleItemAdd(item);
        }

        pages.add(currentInventory);
        return pages.get(0);
    }

    private int currentItemIndex = 0;
    private void handleItemAdd(ItemStack item) {
        var currentInventory = pages.get(pages.size() - 1);
        var itemListSize = PlayerClassHelper.playerClassMetadata.size() - 1;

        Bukkit.getLogger().info("Try add item " + item.getItemMeta().getDisplayName());
        Bukkit.getLogger().info("  # Current item index is " + currentItemIndex);
        Bukkit.getLogger().info("  # itemListSize is " + itemListSize);
        Bukkit.getLogger().info("  # pages size is " + pages.size());

        currentInventory.setItem(currentItemIndex++, item);

        if (currentItemIndex % 36 == 0) {
            Bukkit.getLogger().info("  ## Modulo is 0");
            if (currentItemIndex < itemListSize) currentInventory.setItem(53, getNextPageItem());
            if (currentItemIndex > 36) currentInventory.setItem(45, getPreviousPageItem());

            currentItemIndex = 0;

            currentInventory = Bukkit.createInventory(null, 54, getViewID());
            pages.add(currentInventory);
        }
    }

    private ItemStack getNextPageItem() {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page suivante", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getPreviousPageItem() {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page précédente", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getInventoryItem(String className, Material material, String displayName) {
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

        if (clicked == null
                || clicked.getType() == Material.GLASS_PANE
                || clicked.getType().isAir()
                || clicked.containsEnchantment(Enchantment.ARROW_DAMAGE))
            return;

        Bukkit.getLogger().info("CLICK ON " + clicked.getItemMeta().getDisplayName());
        if (clicked.getItemMeta().getDisplayName().equals("Page suivante")) {
            Bukkit.getLogger().info("  # pages size is " + pages.size());
            Bukkit.getLogger().info("  # currentPage is " + currentPage);
            if (pages.size() > currentPage - 1) currentPage++;
            player.openInventory(pages.get(currentPage));
        } else if (clicked.getItemMeta().getDisplayName().equals("Page précédente")) {
            Bukkit.getLogger().info("  # currentPage is " + currentPage);
            if (0 < currentPage) currentPage--;
            player.openInventory(pages.get(currentPage));
        } else {
            String className = clicked.getItemMeta().getLore().get(0);
            Bukkit.getLogger().info("Player " + player.getDisplayName() + " chose class " + className);
            playerClassSelection.put(player.getDisplayName(), className);
            visitor.onAction();

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
}
