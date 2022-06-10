package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import me.flamboyant.survivalrumble.utils.ScoreType;
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
