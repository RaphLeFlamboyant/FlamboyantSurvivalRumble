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

        String teamName = SurvivalRumbleData.getSingleton().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(teamName, getScoreMalus(), ScoreType.PERFECT);
    }

    @Override
    protected final String getClassDescription() {
        return ChatUtils.personalAnnouncement("Ta classe : " + PlayerClassHelper.playerClassMetadata.get(getClassType()).getDisplayName(),
                "Ta façon principale de marquer des points est la suivante : " + scoringDescription
                        + ". Tu peux également marquer des points gréce é ta suite de quétes secondaires.");
    }

    protected abstract String getClassDescriptionCorpus();
    protected abstract int getScoreMalus();
}
