package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class APlayerInteractQuestComponent extends AQuestComponent implements Listener {
    public APlayerInteractQuestComponent(Player player) {
        super(player);
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        CraftItemEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;

        if (doOnEvent(event)) {
            stopQuest();
        }
    }

    protected boolean doOnEvent(PlayerInteractEvent event) {
        return true;
    }
}
