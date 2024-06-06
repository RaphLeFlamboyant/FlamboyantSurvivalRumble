package me.flamboyant.survivalrumble;

import me.flamboyant.FlamboyantPlugin;
import me.flamboyant.survivalrumble.gamecontrollers.commands.ConsistencyCommands;
import me.flamboyant.survivalrumble.gamecontrollers.commands.DebugCommands;
import me.flamboyant.survivalrumble.gamecontrollers.commands.GameSetupCommands;
import me.flamboyant.survivalrumble.gamecontrollers.commands.PublicCommands;

public final class Main extends FlamboyantPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        PublicCommands publicCommands = new PublicCommands();
        DebugCommands debugCommands = new DebugCommands();
        ConsistencyCommands consistencyCommands = new ConsistencyCommands();

        getCommand("i_gametime").setExecutor(publicCommands);
        getCommand("i_questinfo").setExecutor(publicCommands);
        getCommand("i_check_buff").setExecutor(publicCommands);
        getCommand("i_class_selection").setExecutor(publicCommands);
        getCommand("i_headquarters").setExecutor(publicCommands);

        getCommand("c_sr_reset_server").setExecutor(consistencyCommands);

        getCommand("babki").setExecutor(debugCommands);
        getCommand("bastoooooon").setExecutor(debugCommands);

        // ##########################################################################

        GameSetupCommands experimentalCommands = new GameSetupCommands(this, getServer());
        getCommand("sr_setup_start").setExecutor(experimentalCommands);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
