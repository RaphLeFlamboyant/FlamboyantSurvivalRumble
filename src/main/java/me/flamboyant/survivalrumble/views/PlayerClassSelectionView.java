package me.flamboyant.survivalrumble.views;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.playerclass.factory.PlayerClassFactory;
import me.flamboyant.survivalrumble.utils.ItemHelper;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.utils.ScoreType;
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
import java.util.Collections;
import java.util.UUID;

public class PlayerClassSelectionView implements Listener {
    private static PlayerClassSelectionView instance;
    public PlayerClassType lastChosenClass;
    public UUID lastPlayerClick;
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
        int size = PlayerClassHelper.playerClassMetadata.size() - 1;
        Inventory currentInventory = null;
        ArrayList<PlayerClassMetadata> classMetadataList = new ArrayList<>(PlayerClassHelper.playerClassMetadata.values());
        Collections.sort(classMetadataList, (PlayerClassMetadata a, PlayerClassMetadata b) ->
                a.getPlayerClassCategory() == b.getPlayerClassCategory() ? a.getDisplayName().compareTo(b.getDisplayName()) : a.getPlayerClassCategory().compareTo(b.getPlayerClassCategory()));
        for (PlayerClassMetadata classMetadata : classMetadataList) {
            if (classMetadata.getPlayerClassType() == PlayerClassType.FAKE_CLASS) continue;
            if (i % 36 == 0) {
                if (currentInventory != null) {
                    if (i < size) currentInventory.setItem(53, getNextPageItem());
                    if (i > 36) currentInventory.setItem(45, getPreviousPageItem());
                    pages.add(currentInventory);
                }

                currentInventory = Bukkit.createInventory(null, 54, getViewID());
            }

            ItemStack item = getIntenvoryItem(classMetadata.getPlayerClassType().toString(), classMetadata.getPlayerClassRepresentation(), classMetadata.getDisplayName());
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
            lastPlayerClick = player.getUniqueId();
            PlayerClassMechanicsHelper.getSingleton().declarePlayerClass(player, playerClass);
            data().playersClass.put(player.getUniqueId(), playerClass.getClassType());
            data().playerClassDataList.put(playerClass.getClassType(), playerClass.buildClassData());
            applyMalus(player, playerClass);

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

    private void applyMalus(Player player, APlayerClass playerClass) {
        String teamName = SurvivalRumbleData.getSingleton().playersTeam.get(player.getUniqueId());
        GameManager.getInstance().addScore(teamName, playerClass.getScoreMalus(), ScoreType.PERFECT);
    }

    public void unregisterEvents() {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
