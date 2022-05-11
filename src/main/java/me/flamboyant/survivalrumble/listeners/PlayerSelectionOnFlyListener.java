package me.flamboyant.survivalrumble.listeners;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.PlayerClassMechanicsHelper;
import me.flamboyant.survivalrumble.views.PlayerClassSelectionView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerSelectionOnFlyListener implements Listener {
    private Player player;
    private String teamName;

    public PlayerSelectionOnFlyListener(Player player, String teamName) {
        this.player = player;
        this.teamName = teamName;
    }

    public void start() {
        Common.server.getPluginManager().registerEvents(PlayerClassSelectionView.getInstance(), Common.plugin);
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
        player.openInventory(PlayerClassSelectionView.getInstance().getViewInstance());
    }

    public void stop() {
        InventoryCloseEvent.getHandlerList().unregister(this);
        PlayerClassSelectionView.getInstance().unregisterEvents();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (PlayerClassSelectionView.getInstance().isInView(event.getInventory())) {
            SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

            if (!data.playersClass.containsKey(player.getUniqueId())) {
                Bukkit.getScheduler().runTaskLater(Common.plugin, () ->
                        player.openInventory(PlayerClassSelectionView.getInstance().getViewInstance()), 1L);
                return;
            }

            PlayerClassMechanicsHelper.getSingleton().enablePlayerClass(player.getUniqueId());
            Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("Nouveau joueur !", "Le joueur " + player.getDisplayName() + " a rejoint la partie dans l'équipe " + teamName + " avec la classe " + data.playersClass.get(player.getUniqueId())));

            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                player.teleport(data.teamHeadquarterLocation.get(teamName));
                stop();
            }, 1L);
        }
    }
}
