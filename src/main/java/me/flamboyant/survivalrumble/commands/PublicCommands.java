package me.flamboyant.survivalrumble.commands;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PublicCommands implements CommandExecutor {
    private SurvivalRumbleData data;

    public PublicCommands() {
        data = SurvivalRumbleData.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        data = SurvivalRumbleData.getSingleton();

        if (cmd.getName().equalsIgnoreCase("f_sr_get_gametime")) {
            String message = ChatUtils.feedback("" + (data.minutesBeforeEnd / 60) + "h" + (data.minutesBeforeEnd % 60) + "m");
            sender.sendMessage(message);

            return true;
        }

        return false;
    }
}