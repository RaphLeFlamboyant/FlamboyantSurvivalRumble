package flamboyant.survivalrumble.playerclass.factory;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.playerclass.classobjects.*;
import org.bukkit.entity.Player;

public class PlayerClassFactory {
    public APlayerClass generatePlayerClass(PlayerClassType playerClass, Player owner) {
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
                return new ElbmurLavivrusSurvivorClass(owner);
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
            default:
                return new FakeClass(owner);
        }
    }
}
