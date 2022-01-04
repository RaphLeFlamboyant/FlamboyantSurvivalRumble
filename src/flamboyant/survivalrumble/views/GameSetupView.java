package flamboyant.survivalrumble.views;

import flamboyant.survivalrumble.data.SurvivalRumbleData;
import flamboyant.survivalrumble.utils.ItemHelper;
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
import java.util.List;

public class GameSetupView implements Listener {
    private static GameSetupView instance;
    private Inventory view;
    private List<String> stuffList = Arrays.asList("Stuff basique", "Stuff déplacements rapides", "Stuff minage");

    protected GameSetupView() {
    }

    public static GameSetupView getInstance() {
        if (instance == null) {
            instance = new GameSetupView();
        }

        return instance;
    }

    public static String getViewID() {
        return "Game setup view";
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public Inventory getViewInstance() {
        if (view == null) {
            // TODO  : rendre cette portion générique pour qu'on ait un flag par team
            Inventory myInventory = Bukkit.createInventory(null, 45, getViewID());
            myInventory.setItem(9 + 4, gameTimeItem());
            myInventory.setItem(9 + 5, pvpIntensityItem());
            myInventory.setItem(9 * 3 + 3, basicStuffItem());
            myInventory.setItem(9 * 3 + 4, goFastStuffItem());
            myInventory.setItem(9 * 3 + 5, fastStartStuffItem());
            view = myInventory;
        }

        return view;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != getViewInstance()) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir())
            return;

        if (ItemHelper.isSameItemKind(clicked, gameTimeItem()))
            HandleGameTimeItemClicked(event, clicked);
        else if (ItemHelper.isSameItemKind(clicked, pvpIntensityItem()))
            HandlePvpIntensityItemClicked(event, clicked);
        else if (Arrays.asList(Material.COOKED_BEEF, Material.ENDER_PEARL, Material.IRON_PICKAXE).contains(clicked.getType())) {
            for (int i = 9 * 3 + 3; i < 9 * 3 + 6; i++) {
                ItemStack item = event.getInventory().getItem(i);
                item.removeEnchantment(Enchantment.KNOCKBACK);
            }

            clicked.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
            data().selectedStuff = clicked.getType();
        }
    }

    private void HandlePvpIntensityItemClicked(InventoryClickEvent event, ItemStack clicked) {
        UpdateItemOnClick(event, clicked);

        data().pvpIntensity = clicked.getAmount();
        data().saveData();
    }

    private void HandleGameTimeItemClicked(InventoryClickEvent event, ItemStack clicked) {
        UpdateItemOnClick(event, clicked);

        data().minutesBeforeEnd = clicked.getAmount() * 60;
        data().saveData();
    }

    private void UpdateItemOnClick(InventoryClickEvent event, ItemStack clicked) {
        int amount = clicked.getAmount();
        System.out.println("Before change : " + amount + "; IsRightClick : " + event.isRightClick());
        if (event.isRightClick())
            amount++;
        else
            amount--;

        System.out.println("After change : " + amount);
        if (amount == 0)
            return;

        else clicked.setAmount(amount);
    }

    private ItemStack gameTimeItem() {
        return ItemHelper.generateItem(Material.CLOCK, 4, "Game time (h)", new ArrayList<String>(), false, null, false, false);
    }

    private ItemStack pvpIntensityItem() {
        return ItemHelper.generateItem(Material.DIAMOND_SWORD, 2, "Pvp intensity (meetup, etc)", new ArrayList<String>(), false, null, false, false);
    }

    private ItemStack basicStuffItem() {
        return ItemHelper.generateItem(Material.COOKED_BEEF, 1, stuffList.get(0), new ArrayList<String>(), true, Enchantment.KNOCKBACK, true, false);
    }

    private ItemStack goFastStuffItem() {
        return ItemHelper.generateItem(Material.ENDER_PEARL, 1, stuffList.get(1), new ArrayList<String>(), false, null, true, false);
    }

    private ItemStack fastStartStuffItem() {
        return ItemHelper.generateItem(Material.IRON_PICKAXE, 1, stuffList.get(2), new ArrayList<String>(), false, null, true, false);
    }

    public void unregisterEvents() {
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
