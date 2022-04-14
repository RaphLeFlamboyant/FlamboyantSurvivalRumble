package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class HookPlayerQuestComponent extends AQuestComponent implements Listener {
    private int quantity;

    public HookPlayerQuestComponent(Player player, int quantity) {
        super(player);

        this.quantity = quantity;

        subQuestMessage = "Attrape " + quantity + " joueur" + (quantity > 1 ? "s" : "") + " avec une canne é péche";
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        PlayerFishEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    protected boolean checkFishCondition(PlayerFishEvent event) {
        return true;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getCaught().getType() != EntityType.PLAYER) return;

        if (checkFishCondition(event) && --quantity <= 0) {
            stopQuest();
        }
    }
}
