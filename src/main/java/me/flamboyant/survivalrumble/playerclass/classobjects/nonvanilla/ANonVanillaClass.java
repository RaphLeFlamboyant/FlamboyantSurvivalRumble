package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import org.bukkit.entity.Player;

public abstract class ANonVanillaClass extends APlayerClass {
    public ANonVanillaClass(Player owner) {
        super(owner);
    }

    @Override
    protected final String getClassDescription() {
        return getClassDescriptionCorpus();
    }

    protected abstract String getClassDescriptionCorpus();
}
