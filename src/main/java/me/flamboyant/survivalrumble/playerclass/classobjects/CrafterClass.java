package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.CrafterClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.TeamHelper;
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
        classData = (CrafterClassData) data().playerClassDataList.get(getClassType());
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().equals(owner)) return;
        if (classData.carftedItems.contains(event.getInventory().getResult().getType())) return;
        Location location = event.getWhoClicked().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (!concernedTeamName.equals(data().playersTeam.get(owner.getUniqueId()))) return;

        GameManager.getInstance().addScore(concernedTeamName, (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
        classData.carftedItems.add(event.getInventory().getResult().getType());
    }
}
