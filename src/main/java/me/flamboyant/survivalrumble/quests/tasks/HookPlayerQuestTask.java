package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class HookPlayerQuestTask extends AQuestTask implements Listener {
    private int quantity;

    public HookPlayerQuestTask(Quest ownerQuest, int quantity) {
        super(ownerQuest);

        this.quantity = quantity;

        subQuestMessage = "Attrape " + quantity + " joueur" + (quantity > 1 ? "s" : "") + " avec une canne é péche";
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        PlayerFishEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    protected boolean checkFishCondition(PlayerFishEvent event) {
        return true;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getCaught().getType() != EntityType.PLAYER) return;

        if (checkFishCondition(event) && --quantity <= 0) {
            stopQuest(true);
        }
    }
}
