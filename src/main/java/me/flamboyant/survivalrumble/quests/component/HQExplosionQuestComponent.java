package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;

public class HQExplosionQuestComponent extends AQuestComponent implements Listener {
    private int quantity;
    private List<String> targetTeamList;

    public HQExplosionQuestComponent(Player player, int quantity, List<String> targetTeamList) {
        super(player);

        this.quantity = quantity;
        this.targetTeamList = targetTeamList;

        subQuestMessage = "Fais exploser " + quantity + " TNT dans une/des base(s) parmi les suivantes : [";
        for (String team : targetTeamList) {
            subQuestMessage += team + ", ";
        }
        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2) + "]";
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        ExplosionPrimeEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        Location explosionLocation = event.getEntity().getLocation();

        for (String team : targetTeamList) {
            if (TeamHelper.isLocationInHeadQuarter(explosionLocation, team)) {
                if (--quantity <= 0)
                    stopQuest();
                return;
            }
        }
    }
}
