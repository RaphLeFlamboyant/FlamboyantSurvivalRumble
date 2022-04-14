package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class APlayerDeathQuestComponent extends AQuestComponent implements Listener {
    public APlayerDeathQuestComponent(Player player) {
        super(player);
    }

    protected abstract void doOnEvent(PlayerDeathEvent event);

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        PlayerDeathEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        doOnEvent(event);
    }
}
