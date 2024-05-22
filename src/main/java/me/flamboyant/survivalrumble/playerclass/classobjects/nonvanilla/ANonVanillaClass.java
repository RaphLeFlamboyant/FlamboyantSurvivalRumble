package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.PlayerClassHelper;
import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class ANonVanillaClass extends APlayerClass {
    public ANonVanillaClass(Player owner) {
        super(owner);
    }

    @Override
    protected final String getClassDescription() {
        return ChatColors.personalAnnouncementChaptered("Ta classe : " + PlayerClassHelper.playerClassMetadata.get(getClassType()).getDisplayName(),
                Arrays.asList("Capacité spéciale :", "Gain d'argent :"),
                Arrays.asList(getAbilityDescription(), getScoringDescription()));
    }

    protected abstract String getAbilityDescription();
    protected abstract String getScoringDescription();
}
