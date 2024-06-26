package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.data.classes.ThiefClassData;
import me.flamboyant.survivalrumble.utils.*;
import me.flamboyant.survivalrumble.views.PlayerSelectionView;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ThiefClass extends ANonVanillaClass implements Listener {
    private ThiefClassData classData;
    private PlayerSelectionView currentOpenedView;

    public ThiefClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.THIEF;
    }

    @Override
    public PlayerClassData buildClassData() { return new ThiefClassData(); }

    @Override
    protected String getAbilityDescription() {
        return "Toute la partie, tu recevra les craft d'un joueur adverse.";
    }

    @Override
    protected String getScoringDescription() {
        return "Pose les blocs volés dans ta base.";
    }

    @Override
    public int getScoreMalus() {
        return -2000;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (ThiefClassData) data().getPlayerClassData(owner);
        owner.getInventory().addItem(getPlayerSelectionItem());
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        super.disableClass();
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        CraftItemEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        var playerWhoPlaces = event.getPlayer();

        if (playerWhoPlaces != owner) return;
        // TODO : j'ai pas accès au player sur l'event modifier mais le check de location y est déjà fait donc ici ça fait doublon
        String concernedTeam = TeamHelper.getTeamHeadquarterName(block.getLocation());
        String ownerTeam = data().getPlayerTeam(owner);
        if (concernedTeam == null || !concernedTeam.equals(ownerTeam)) return;

        if (lastInteractThief) {
            GameManager.getInstance().addAddMoney(ownerTeam, 2);
        }
    }

    private boolean lastInteractThief;
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getItem() == null) return;

        if (ItemHelper.isSameItemKind(event.getItem(), getPlayerSelectionItem())) {
            currentOpenedView = new PlayerSelectionView(owner, data().getPlayers(data().getPlayerTeam(owner)));
            owner.openInventory(currentOpenedView.getViewInstance());
        }
        else {
            lastInteractThief = event.getItem().getItemMeta().getDisplayName().equals("Objet volé");
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        if (event.getPlayer() != owner) return;

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> owner.getInventory().addItem(getPlayerSelectionItem()), 5L);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() != owner) return;
        if (currentOpenedView == null) return;

        Inventory inv = owner.getInventory();
        ItemStack content[] = inv.getContents();
        int i;
        for (i = 0; i < content.length; i++) {
            if (ItemHelper.isSameItemKind(content[i], getPlayerSelectionItem()))
                break;
        }

        classData.targetPlayerId = currentOpenedView.getSelectedPlayer().getUniqueId();
        content[i] = getPlayerSelectionItem();

        currentOpenedView = null;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(classData.targetPlayerId)) return;
        ItemStack cursorStack = event.getCursor();
        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getType() != cursorStack.getType())
            return;
        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getAmount() + cursorStack.getAmount() > cursorStack.getMaxStackSize())
            return;

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

        // TODO ; gérer le cas inventaire full
        ItemStack itemCopy = new ItemStack(event.getInventory().getResult().getType(), realQuantity);

        ItemMeta meta = itemCopy.getItemMeta();
        meta.setDisplayName("Objet volé");
        itemCopy.setItemMeta(meta);

        owner.getInventory().addItem(itemCopy);
    }

    private ItemStack getPlayerSelectionItem() {
        ItemStack res = ItemHelper.generateItem(Material.ECHO_SHARD, 1, "Joueur volé", Arrays.asList(), true, Enchantment.ARROW_FIRE, true, true);
        if (classData.targetPlayerId != null) {
            Player p = Common.server.getPlayer(classData.targetPlayerId);
            res.setType(Material.PLAYER_HEAD);
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
            skull.setLore(Arrays.asList(p.getDisplayName()));
            skull.setOwningPlayer(p);
            playerHead.setItemMeta(skull);
        }

        return res;
    }
}
