package me.flamboyant.survivalrumble.gamecontrollers.commands;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class DebugCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("babki")) {
            var data = SurvivalRumbleData.getSingleton();

            for (var teamName : data.getTeams()) {
                addScore(teamName, 50000);
            }

            return true;
        }
        if (cmd.getName().equalsIgnoreCase("bastoooooon")) {
            SurvivalRumbleData.getSingleton().minutesBeforeEnd = 1;
            return true;
        }
        return false;
    }

    private void addScore(String teamName, int scoreToAdd) {
        GameManager.getInstance().addAddMoney(teamName, scoreToAdd);
    }
}
