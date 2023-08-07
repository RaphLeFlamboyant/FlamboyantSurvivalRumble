package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.CrafterClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ScoreHelper;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CrafterClass extends APlayerClass implements Listener {
    private CrafterClassData classData;

    public CrafterClass(Player owner) {
        super(owner);

        scoringDescription = "Crafter, dans ta base, des items que tu n'as pas encore crafté";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CRAFTER;
    }

    @Override
    public PlayerClassData buildClassData() { return new CrafterClassData(); }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (CrafterClassData) data().getPlayerClassData(owner);
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        CraftItemEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().equals(owner)) return;
        if (classData.carftedItems.contains(event.getInventory().getResult().getType())) return;
        Location location = event.getWhoClicked().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (!concernedTeamName.equals(data().getPlayerTeam(owner))) return;

        GameManager.getInstance().addAddMoney(concernedTeamName, (int) (5 * ScoreHelper.scoreAltitudeCoefficient(location.getBlockY())));
        classData.carftedItems.add(event.getInventory().getResult().getType());
    }
}
