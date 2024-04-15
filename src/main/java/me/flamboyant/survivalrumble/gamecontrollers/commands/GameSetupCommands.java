package me.flamboyant.survivalrumble.gamecontrollers.commands;

import me.flamboyant.survivalrumble.gamecontrollers.launch.GameLauncher;
import me.flamboyant.survivalrumble.utils.ScoreboardBricklayer;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GameSetupCommands implements CommandExecutor {
    private JavaPlugin plugin;
    private Server server;
    private ScoreboardBricklayer scoreboardBricklayer;

    public GameSetupCommands(JavaPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        this.scoreboardBricklayer = ScoreboardBricklayer.getSingleton();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player senderPlayer = (Player) sender;

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
        if (Bukkit.getWorld(UsefulConstants.waitingWorldName) == null) {
            Bukkit.getLogger().info("Creating the custom world");
            new WorldCreator(UsefulConstants.waitingWorldName)
                    .environment(World.Environment.NORMAL)
                    .type(WorldType.FLAT)
                    .generatorSettings("{\"layers\": [{\"block\": \"bedrock\", \"height\": 1}, {\"block\": \"grass_block\", \"height\": 1}], \"biome\":\"plains\"}")
                    .generateStructures(false)
                    .createWorld();
        }

        GameLauncher listener = new GameLauncher();
        listener.start(senderPlayer);

        return true;
    }
}
