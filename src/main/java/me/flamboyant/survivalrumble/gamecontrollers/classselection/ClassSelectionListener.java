package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ITriggerVisitor;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.utils.Common;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class ClassSelectionListener implements Listener, ITriggerVisitor {
    private int currentPlayerIndex;
    private List<Player> playerOrder;
    private IClassSelectionVisitor visitor;

    private static ClassSelectionListener instance;
    protected ClassSelectionListener() {

    }

    public static ClassSelectionListener getInstance() {
        if (instance == null) {
            instance = new ClassSelectionListener();
        }

        return instance;
    }

    public void start(IClassSelectionVisitor visitor) {
        this.visitor = visitor;

        playerOrder.clear();
        playerOrder = PickOrderHelper.getPlayerPickOrder();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
        PlayerClassSelectionView.getInstance().start(this);

        currentPlayerIndex = 0;

        for (Player player : Common.server.getOnlinePlayers()) {
            player.playSound(player, Sound.EVENT_RAID_HORN, SoundCategory.MASTER, 1, 1);
            player.sendTitle("Sélection des classes", "Chacun votre tour, sélectionnez une classe", 10, 80, 10);
        }
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> nextPlayerSelect(), 100);
    }

    public void stop() {
        if (visitor != null) {
            PlayerClassSelectionView.getInstance().stop();
            InventoryCloseEvent.getHandlerList().unregister(this);
            PlayerJoinEvent.getHandlerList().unregister(this);
            visitor = null;
        }
    }

    @Override
    public void onAction() {
        PlayerClassMetadata metadata = PlayerClassHelper.playerClassMetadata.get(PlayerClassType.valueOf(PlayerClassSelectionView.getInstance().getPlayerClassSelection().get(playerOrder.get(currentPlayerIndex).getDisplayName())));

        for (Player player : Common.server.getOnlinePlayers()) {
            if (player.getDisplayName().equals(playerOrder.get(currentPlayerIndex))) continue;
            player.playSound(player, metadata.getSound(), SoundCategory.MASTER, 1, 1);
            player.sendTitle(ChatColor.RED + metadata.getTitle(), ChatColor.BLUE + metadata.getSubtitle(), 10, 80, 10);
        }

        if (++currentPlayerIndex < playerOrder.size()) {
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> nextPlayerSelect(), 100);
        }
        else {
            visitor.onClassesSelected(PlayerClassSelectionView.getInstance().getPlayerClassSelection());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player currentPlayer = playerOrder.get(currentPlayerIndex);
        if (event.getPlayer() == currentPlayer
                && !PlayerClassSelectionView.getInstance().getPlayerClassSelection().containsKey(currentPlayer.getDisplayName())) {
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> currentPlayer.openInventory(PlayerClassSelectionView.getInstance().getViewInstance()), 1);
        }
    }

    @EventHandler
    public void onPlayerConnects(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (SurvivalRumbleData.getSingleton().getPlayerTeam(player) != null) return;

        player.setGameMode(GameMode.SPECTATOR);
    }

    private void nextPlayerSelect() {
        for (Player player : Common.server.getOnlinePlayers()) {
            player.playSound(player, Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1, 1);
            player.sendTitle(ChatColor.RED + player.getDisplayName(), ChatColor.BLUE + "À lui/elle de choisir", 10, 80, 10);
        }

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            playerOrder.get(currentPlayerIndex).openInventory(PlayerClassSelectionView.getInstance().getViewInstance());
        }, 100);
    }
}
