package flamboyant.survivalrumble;

import flamboyant.survivalrumble.commands.ConsistencyCommands;
import flamboyant.survivalrumble.commands.DebugCommands;
import flamboyant.survivalrumble.commands.GameSetupCommands;
import flamboyant.survivalrumble.commands.PublicCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();

        PublicCommands publicCommands = new PublicCommands();
        DebugCommands debugCommands = new DebugCommands(this, getServer());
        ConsistencyCommands consistencyCommands = new ConsistencyCommands(this, getServer());

        getCommand("f_sr_get_gametime").setExecutor(publicCommands);

        // V - 1 - Cr�er l'item des param�tres de la partie et le menu associ� (temps de la game, meetup, add player, etc)
        // V - 2 - Cr�er l'item des hq coordinates qui ouvre un menu avec les teams pour s�lectionner la team dont ont veut faire le hq
        // V - 3 - Cr�er l'item de lancemnent de partie + TODO s'assurer que les param�tres de data par d�fauts sont set quand on fait le sr_setup_start
        // V - 4 - Quand la partie se lace, on entre dans une phase de s�lection des classes
        // -> Le premier joueur de l'�quipe A voit un menu de s�lection de classes s'ouvrir, cliquer sur une lui permet de la s�lectionner
        // -> A chaque fois qu'une joueur a s�lectionn� sa classe, le premier joueur, qui n'en a pas encore choisi, de l'�quipe suivante  voit ce menu s'afficher � son tours
        // ! avec les classes prises en moins ou en rouge ou whatever
        // -> Quand c'est fini, l'OP voit un menu de lancement de partie s'ouvrir, il doit cliquer sur un validate
        // V - 5 - Ajouter le listener de go to base et avant v�rifier que la data est bien fill
        // 6 - Cr�er l'item de maintenance
        // 7 - Cr�er le menu de debug ouvert avec une commande

        getCommand("f_sr_maintenance_load_data").setExecutor(consistencyCommands);
        getCommand("f_sr_maintenance_reset_server_config").setExecutor(consistencyCommands);
        getCommand("f_sr_maintenance_make_server_config").setExecutor(consistencyCommands);

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
