package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class APlayerInteractQuestTask extends AQuestTask implements Listener {
    public APlayerInteractQuestTask(Quest ownerQuest) {
        super(ownerQuest);
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        CraftItemEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;

        if (doOnEvent(event)) {
            stopQuest(true);
        }
    }

    protected boolean doOnEvent(PlayerInteractEvent event) {
        return true;
    }
}
