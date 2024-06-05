package me.flamboyant.survivalrumble.playerclass.factory;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.playerclass.classobjects.*;
import me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla.*;
import org.bukkit.entity.Player;

public class PlayerClassFactory {
    public static APlayerClass generatePlayerClass(PlayerClassType playerClass, Player owner) {
        switch (playerClass) {
            case MASON:
                return new MasonClass(owner);
            case WARRIOR:
                return new WarriorClass(owner);
            case BUNGLER:
                return new BunglerClass(owner);
            case SCOUT:
                return new ScoutClass(owner);
            case PYROMANIAC:
                return new PyromaniacClass(owner);
            case TRAPPER:
                return new TrapperClass(owner);
            case PEASANT:
                return new PeasantClass(owner);
            case ELBMURLAVIVRUS:
                return new ElbmurLavivrusClass(owner);
            case ARCHER:
                return new ArcherClass(owner);
            case HUNTER:
                return new HunterClass(owner);
            case ELECTRICIAN:
                return new ElectricianClass(owner);
            case BADKID:
                return new BadKidClass(owner);
            case BLACKSMITH:
                return new BlackSmithClass(owner);
            case CRAFTER:
                return new CrafterClass(owner);
            case BOMBER:
                return new BomberClass(owner);
            case ANTIQUARIAN:
                return new AntiquarianClass(owner);
            case WITCH:
                return new WitchClass(owner);
            case DEFEATIST:
                return new DefeatistClass(owner);
            case CHEERLEADER:
                return new CheerleaderClass(owner);
            case FISHER:
                return new FisherClass(owner);
            case BOORISH:
                return new BoorishClass(owner);
            case WEREWOLF:
                return new WerewolfClass(owner);
            case ASSASSIN:
                return new AssassinClass(owner);
            case THIEF:
                return new ThiefClass(owner);
            case STANDARDBEARER:
                return new StandardBearerClass(owner);
            case NINJA:
                return new NinjaClass(owner);
            case SHAMELESS:
                return new ShamelessClass(owner);
            case NUDIST:
                return new NudistClass(owner);
            case NARCOLEPTIC:
                return new NarcolepticClass(owner);
            default:
                return new FakeClass(owner);
        }
    }
}
