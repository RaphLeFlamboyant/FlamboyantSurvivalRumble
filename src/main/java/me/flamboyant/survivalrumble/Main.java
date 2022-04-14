package me.flamboyant.survivalrumble;

import me.flamboyant.survivalrumble.commands.ConsistencyCommands;
import me.flamboyant.survivalrumble.commands.DebugCommands;
import me.flamboyant.survivalrumble.commands.GameSetupCommands;
import me.flamboyant.survivalrumble.commands.PublicCommands;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        Common.plugin = this;
        Common.server = getServer();

        PublicCommands publicCommands = new PublicCommands();
        DebugCommands debugCommands = new DebugCommands(this, getServer());
        ConsistencyCommands consistencyCommands = new ConsistencyCommands(this, getServer());

        getCommand("f_sr_get_gametime").setExecutor(publicCommands);
        getCommand("sr_reset_server").setExecutor(consistencyCommands);
        getCommand("f_add_score").setExecutor(debugCommands);

        // ##########################################################################

        GameSetupCommands experimentalCommands = new GameSetupCommands(this, getServer());
        getCommand("sr_setup_start").setExecutor(experimentalCommands);

        getCommand("f_snapshot").setExecutor(debugCommands);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
