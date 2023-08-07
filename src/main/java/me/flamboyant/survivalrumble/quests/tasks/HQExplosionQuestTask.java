package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;

public class HQExplosionQuestTask extends AQuestTask implements Listener {
    private int quantity;
    private List<String> targetTeamList;

    public HQExplosionQuestTask(Quest ownerQuest, int quantity, List<String> targetTeamList) {
        super(ownerQuest);

        this.quantity = quantity;
        this.targetTeamList = targetTeamList;

        subQuestMessage = "Fais exploser " + quantity + " TNT dans une/des base(s) parmi les suivantes : [";
        for (String team : targetTeamList) {
            subQuestMessage += team + ", ";
        }
        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2) + "]";
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        ExplosionPrimeEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        Location explosionLocation = event.getEntity().getLocation();

        for (String team : targetTeamList) {
            if (TeamHelper.isLocationInHeadQuarter(explosionLocation, team)) {
                if (--quantity <= 0)
                    stopQuest(true);
                return;
            }
        }
    }
}
