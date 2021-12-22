package flamboyant.survivalrumble.playerclass.factory;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FakeClass extends APlayerClass {
    public FakeClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WEREWOLF;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }
}
