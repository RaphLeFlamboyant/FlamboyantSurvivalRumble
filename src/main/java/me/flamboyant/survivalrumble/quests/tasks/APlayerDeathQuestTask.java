package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class APlayerDeathQuestTask extends AQuestTask implements Listener {
    public APlayerDeathQuestTask(Quest ownerQuest) {
        super(ownerQuest);
    }

    protected abstract void doOnEvent(PlayerDeathEvent event);

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        PlayerDeathEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        doOnEvent(event);
    }
}
