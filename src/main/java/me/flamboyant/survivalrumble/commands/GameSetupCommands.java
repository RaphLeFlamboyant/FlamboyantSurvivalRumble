package me.flamboyant.survivalrumble.commands;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.listeners.GameSetupListener;
import me.flamboyant.survivalrumble.utils.ItemHelper;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.survivalrumble.views.GameSetupView;
import me.flamboyant.survivalrumble.views.TeamHQParametersView;
import me.flamboyant.survivalrumble.views.TeamSelectionView;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class GameSetupCommands implements CommandExecutor {
    private JavaPlugin plugin;
    private Server server;
    private ScoreboardBricklayer scoreboardBricklayer;

    public GameSetupCommands(JavaPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        this.scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player senderPlayer = (Player) sender;
        Bukkit.broadcastMessage("é_à_§_ù");
        System.out.println("é_à_§_ù");
        System.out.println("Command " + cmd.getName());
        System.out.println("Dir " + System.getProperty("user.dir"));


        switch (cmd.getName()) {
            case "sr_setup_start":
                return startSetup(senderPlayer, args);
            default:
                break;
        }

        System.out.println("End command no match");

        return false;
    }

    private boolean startSetup(Player senderPlayer, String[] args) {
        OfflinePlayer[] players = server.getOfflinePlayers();

        data().opPlayer = senderPlayer.getUniqueId();

        // TODO : gérer la création des équipes autrement (temporary hack)
        for (String teamName : TeamHelper.teamNames) {
            ChatColor color = TeamHelper.getTeamColor(teamName);
            System.out.println("Création de la team " + color + teamName);
            Team team = scoreboardBricklayer.addNewTeam(teamName);

            team.setColor(color);
            data().teams.add(teamName);
            data().teamMalus.put(teamName, 0);
        }

        for (int i = 0; i < players.length; i++) {
            OfflinePlayer player = players[i];
            if (player.getPlayer() == null) {
                System.out.println("Warning : player " + player.getName() + " and ID " + player.getUniqueId() + " is not considered as a Player in the system");
                continue;
            }

            ItemStack item = ItemHelper.getTeamSelectionItem();
            player.getPlayer().getInventory().clear();
            player.getPlayer().getInventory().setItem(2, item);
            if (!player.getPlayer().isOp()){
                player.getPlayer().setGameMode(GameMode.ADVENTURE);
            }
        }

        GameSetupListener listener = new GameSetupListener(plugin, server);
        server.getPluginManager().registerEvents(listener, plugin);
        server.getPluginManager().registerEvents(TeamSelectionView.getInstance(), plugin);

        giveSenderOpItems(senderPlayer, plugin);

        data().saveData();

        return true;
    }

    private void giveSenderOpItems(Player senderPlayer, JavaPlugin plugin) {
        ItemStack setupItem = ItemHelper.getGameSetupItem();
        senderPlayer.getPlayer().getInventory().setItem(5, setupItem);
        server.getPluginManager().registerEvents(GameSetupView.getInstance(), plugin);

        setupItem = ItemHelper.getTeamHQItem();
        senderPlayer.getPlayer().getInventory().setItem(6, setupItem);
        server.getPluginManager().registerEvents(TeamHQParametersView.getInstance(), plugin);

        setupItem = ItemHelper.getLaunchItem();
        senderPlayer.getPlayer().getInventory().setItem(8, setupItem);
    }
}
