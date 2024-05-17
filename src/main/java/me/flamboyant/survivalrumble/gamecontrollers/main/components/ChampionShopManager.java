package me.flamboyant.survivalrumble.gamecontrollers.main.components;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.powers.shop.ChampionPowerShopItem;
import me.flamboyant.survivalrumble.powers.shop.ChampionPowerHelper;
import me.flamboyant.survivalrumble.shop.ChampionPowerShop;
import me.flamboyant.survivalrumble.shop.IShopChangesListener;
import me.flamboyant.survivalrumble.shop.ShopItem;
import me.flamboyant.survivalrumble.shop.TeamMoneyManager;
import me.flamboyant.survivalrumble.views.shop.ShopView;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class ChampionShopManager implements Listener, IShopChangesListener {
    private String teamName;
    private ChampionPowerShop championPowerShop;
    private ShopView shopView;
    private List<ChampionPowerShopItem> championPowerShopItems;

    public ChampionShopManager(String teamName) {
        this.teamName = teamName;
    }

    public void start() {
        championPowerShopItems = generateChampionPowers();

        championPowerShop = new ChampionPowerShop(TeamMoneyManager.getInstance());
        for (ChampionPowerShopItem championPowerShopItem : championPowerShopItems) {
            championPowerShop.addChampionPowerToShop(championPowerShopItem);
        }
        championPowerShop.addShopContentChangeListener(this);

        shopView = new ShopView("Pouvoirs du champion", championPowerShop.getAllShopItemControllers());

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop () {
        PlayerInteractEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        if (data.getPlayerTeam(event.getPlayer()) != teamName) return;
        if (event.getClickedBlock() == null) return;
        if (!Arrays.asList(Material.BEDROCK, Material.DIAMOND_BLOCK).contains(event.getClickedBlock().getType())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        onPlayerOpen(event.getPlayer());
    }

    @Override
    public void ItemAdded(ShopItem shopItem) {
        // TODO c'est mal fait
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsAdded(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    @Override
    public void ItemRemoved(ShopItem shopItem) {
        Bukkit.getLogger().info("[AShopStepHandler.ItemRemoved]");
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsRemoved(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    @Override
    public void ItemUpdated(ShopItem shopItem) {
        Bukkit.getLogger().info("[AShopStepHandler.ItemUpdated]");
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    @Override
    public void ItemsUpdated(List<ShopItem> shopItems) {
        shopView.resetItemControllerList(championPowerShop.getAllShopItemControllers());
    }

    private void onPlayerOpen(Player player) {
        shopView.open(player);
    }

    private List<ChampionPowerShopItem> generateChampionPowers() {
        return ChampionPowerHelper.buildChampionPowerList();
    }
}
