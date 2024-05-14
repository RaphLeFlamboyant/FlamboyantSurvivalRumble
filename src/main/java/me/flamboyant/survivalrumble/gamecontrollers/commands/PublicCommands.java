package me.flamboyant.survivalrumble.gamecontrollers.commands;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.powers.ChampionPowerType;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import me.flamboyant.survivalrumble.utils.QuestHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PublicCommands implements CommandExecutor {
    private SurvivalRumbleData data;

    public PublicCommands() {
        data = SurvivalRumbleData.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        data = SurvivalRumbleData.getSingleton();

        if (cmd.getName().equalsIgnoreCase("f_sr_gametime")) {
            String message = ChatColors.feedback("" + (data.minutesBeforeEnd / 60) + "h" + (data.minutesBeforeEnd % 60) + "m");
            sender.sendMessage(message);

            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("f_sr_questinfo")) {
            Player player = (Player) sender;
            QuestHelper.showQuestMessage(player);

            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("f_sr_check_buff")) {
            Player player = (Player) sender;
            printChampionPowers(player);

            return true;
        }

        return false;
    }

    private void printChampionPowers(Player player) {
        var data = SurvivalRumbleData.getSingleton();
        var teamName = data.getPlayerTeam(player);
        var msg = "" + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.AQUA + "[Pouvoirs du champion de la team " + teamName + "]" + ChatColor.RESET;

        for (var powerType : ChampionPowerType.values()) {
            var level = data.getChampionPowerTypeLevel(teamName, powerType);

            if (level > 0) {
                msg += "\n" + ChatColor.BOLD + powerType + ChatColor.RESET + " lvl " + ChatColor.GOLD + level + ChatColor.RESET;
            }
        }

        player.sendMessage(msg);
    }

    private String gameInfo() {
        /*
        String res = "";
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();

        if (data.getTeams().size() == 0) return "La partie n'a pas encore commencé";

        res += "Temps restant : " + (data.minutesBeforeEnd / 60) + "h" + (data.minutesBeforeEnd % 60) + "m";
        res += "\n \n ";

        res+= "### BASES ###\n ";
        for (String teamName : data.teams) {
            Location loc = data.teamHeadquarterLocation.get(teamName);
            res += "Base de l'équipe " + teamName + " : " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
            res += "\n ";
        }

        res += "\n ";
        res+= "### CLASSES ###\n ";
        for (String teamName : data.teams) {
            for (UUID playerId : data.playersByTeam.get(teamName)) {
                Player player = Common.server.getPlayer(playerId);
                res += player.getDisplayName() + " a la classe " + PlayerClassHelper.playerClassMetadata.get(data.playersClass.get(playerId)).getDisplayName();
                res += "\n ";
            }
            res += "\n ";
        }

        return res;

         */
        return "";
    }
}
