package me.flamboyant.survivalrumble.gamecontrollers.commands;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ConsistencyCommands implements CommandExecutor {
    private ScoreboardBricklayer scoreboardBricklayer;

    public ConsistencyCommands() {
        this.scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        Player senderPlayer = (Player) sender;

        switch (cmd.getName()) {
            case "c_sr_reset_server":
                return resetServerConfig(senderPlayer);
            default:
                break;
        }

        return false;
    }

    private Boolean resetServerConfig(Player senderPlayer) {
        scoreboardBricklayer.deleteAllObjectives();
        senderPlayer.sendMessage("Objectifs supprimés");
        System.out.println("Objectifs supprimés");
        scoreboardBricklayer.deleteAllTeams();
        senderPlayer.sendMessage("Equipes supprimées");
        System.out.println("Equipes supprimées");

        return true;
    }
}
