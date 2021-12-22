package flamboyant.survivalrumble.views;

import flamboyant.survivalrumble.data.PlayerClassMetadata;
import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import flamboyant.survivalrumble.playerclass.factory.PlayerClassFactory;
import flamboyant.survivalrumble.utils.PlayerClassHelper;
import flamboyant.survivalrumble.utils.ItemHelper;
import flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
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

public class PlayerClassSelectionView implements Listener
{
    private ArrayList<Inventory> pages = new ArrayList<>();
    private int currentPage;
    private PlayerClassFactory factory;
    private SurvivalRumbleData data() { return SurvivalRumbleData.getSingleton(); }

    private static PlayerClassSelectionView instance;
    public static PlayerClassSelectionView getInstance()
    {
        if (instance == null)
        {
            instance = new PlayerClassSelectionView();
        }

        return instance;
    }

    protected PlayerClassSelectionView()
    {
        factory = new PlayerClassFactory();
    }

    public static String getViewID()
    {
        return "Class selection view";
    }

    public Inventory getViewInstance()
    {
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
        }

        return pages.get(0);
    }

    private ItemStack getNextPageItem()
    {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page suivante", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getPreviousPageItem()
    {
        return ItemHelper.generateItem(Material.PAPER, 1, "Page pr�c�dente", Arrays.asList(), false, null, true, false);
    }

    private ItemStack getIntenvoryItem(String className, Material material, String displayName)
    {
        return ItemHelper.generateItem(material, 1, displayName, Arrays.asList(className), false, null, true, false);
    }

    public Boolean isInView(Inventory inv)
    {
        return pages.contains(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();
        if (inventory != getViewInstance()) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir() || clicked.containsEnchantment(Enchantment.ARROW_DAMAGE))
            return;

        if (clicked.getItemMeta().getDisplayName() == "Page suivante")
        {
            if (pages.size() > currentPage - 1) currentPage++;
            player.openInventory(pages.get(currentPage));
        }
        else if (clicked.getItemMeta().getDisplayName() == "Page pr�c�dente")
        {
            if (0 < currentPage) currentPage--;
            player.openInventory(pages.get(currentPage));
        }
        else
        {
            String className = clicked.getItemMeta().getLore().get(0);
            APlayerClass playerClass = factory.generatePlayerClass(PlayerClassType.valueOf(className), player);
            lastChosenClass = playerClass.getClassType();
            PlayerClassMechanicsHelper.getSingleton().declarePlayerClass(player, playerClass);
            data().playersClass.put(player.getUniqueId(), playerClass.getClassType());

            Inventory currentInv = pages.get(currentPage);
            int i;
            for (i = 0; i < 36; i++)
            {
                ItemStack itemFromInv = currentInv.getItem(i);
                if (itemFromInv == null || itemFromInv.getType() == Material.AIR)
                    break;

                if (ItemHelper.isExactlySameItemKind(itemFromInv, clicked))
                {
                    clicked.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                    clicked.getItemMeta().setDisplayName(clicked.getItemMeta().getDisplayName() + " - " + player.getDisplayName());
                }
            }

            currentPage = 0;
            player.closeInventory();
        }
        // TODO : bug potentiel : le mec ferme l'inventaire sans avoir s�lectionn� de classe -> mettre une s�lection al�atoire ?
    }

    public PlayerClassType lastChosenClass;

    public void unregisterEvents()
    {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
