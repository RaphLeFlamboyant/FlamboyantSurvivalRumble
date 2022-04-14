package me.flamboyant.survivalrumble.playerclass.factory;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import org.bukkit.entity.Player;

public class FakeClass extends APlayerClass {
    public FakeClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WEREWOLF;
    }

    @Override
    public void enableClass() {

    }
}
