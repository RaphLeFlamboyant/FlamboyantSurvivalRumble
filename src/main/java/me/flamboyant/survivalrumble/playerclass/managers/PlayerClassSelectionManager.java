package me.flamboyant.survivalrumble.playerclass.managers;

import me.flamboyant.survivalrumble.data.PlayerClassMetadata;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ItemHelper;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.views.PlayerClassSelectionView;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlayerClassSelectionManager implements Listener {
    private JavaPlugin plugin;
    private Server server;
    private Player opPlayer;
    private UUID currentPlayerId;
    private List<List<UUID>> playerSorted = new ArrayList<>();
    private int currentPlayerTeamIndex = 0;
    private int currentPlayerIndex = -1;

    public PlayerClassSelectionManager(JavaPlugin plugin, Server server, Player opPlayer) {
        this.plugin = plugin;
        this.server = server;
        this.opPlayer = opPlayer;

        for (String teamName : data().teams)
            playerSorted.add(data().playersByTeam.get(teamName));

        playerSorted.sort(Comparator.comparingInt(List::size));
        Collections.reverse(playerSorted);

        for (int i = 0; i < playerSorted.size(); i++) {
            List<UUID> line = playerSorted.get(i);
            System.out.println(i + "rd line size = " + line.size());
            for (UUID id : line) {
                System.out.println(">>  " + id);
            }
        }
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void dispatchSelectionView() {
        UUID playerId;
        if (currentPlayerIndex == -1) {
            currentPlayerIndex = 0;
            playerId = playerSorted.get(0).get(0);
        } else {
            if (++currentPlayerTeamIndex == playerSorted.size()) {
                currentPlayerTeamIndex = 0;
                currentPlayerIndex++;
            }

            if (currentPlayerIndex == playerSorted.get(currentPlayerTeamIndex).size()) {
                unregisterEvents();
                PlayerClassSelectionView.getInstance().unregisterEvents();

                opPlayer.getPlayer().getInventory().setItem(4, ItemHelper.getLaunchItem());
                opPlayer.sendMessage(ChatUtils.debugMessage("Now use the launch item to start the game"));
                return;
            }

            playerId = playerSorted.get(currentPlayerTeamIndex).get(currentPlayerIndex);
        }

        Player player = server.getPlayer(playerId);
        disclaimWhosTurn(player.getDisplayName());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            currentPlayerId = playerId;
            player.openInventory(PlayerClassSelectionView.getInstance().getViewInstance());
        }, 60L);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (PlayerClassSelectionView.getInstance().isInView(event.getInventory())) {
            if (currentPlayerId != PlayerClassSelectionView.getInstance().lastPlayerClick) {
                Bukkit.getScheduler().runTaskLater(Common.plugin, () ->
                        Common.server.getPlayer(currentPlayerId).openInventory(PlayerClassSelectionView.getInstance().getViewInstance()), 1L);
                return;
            }

            disclaimClassChoice();
            Bukkit.getScheduler().runTaskLater(plugin, () -> dispatchSelectionView(), 60L);
        }
    }

    private void disclaimWhosTurn(String playerName) {
        for (UUID playerId : data().playersTeam.keySet()) {
            Player player = server.getPlayer(playerId);
            if (player.getDisplayName().equals(playerName)) continue;
            Location playerLocation = player.getLocation();

            player.playSound(playerLocation, Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1, 1);
            player.sendTitle(ChatColor.RED + playerName, ChatColor.BLUE + "À lui/elle de choisir", 10, 80, 10);
        }
    }

    private void disclaimClassChoice() {
        PlayerClassType lastSelection = PlayerClassSelectionView.getInstance().lastChosenClass;
        PlayerClassMetadata playerClassMetadata = PlayerClassHelper.playerClassMetadata.get(lastSelection);

        for (UUID playerId : data().playersTeam.keySet()) {
            Player player = server.getPlayer(playerId);
            Location playerLocation = player.getLocation();

            player.playSound(playerLocation, playerClassMetadata.getSound(), SoundCategory.MASTER, 1, 1);
            player.sendTitle(ChatColor.RED + playerClassMetadata.getTitle(), ChatColor.BLUE + playerClassMetadata.getSubtitle(), 10, 80, 10);
        }
    }

    private void unregisterEvents() {
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
}
