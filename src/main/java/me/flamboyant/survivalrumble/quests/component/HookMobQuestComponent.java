package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class HookMobQuestComponent extends AQuestComponent implements Listener {
    private int quantity;
    private List<EntityType> validTarget;

    public HookMobQuestComponent(Player player, int quantity, List<EntityType> validTarget) {
        super(player);

        this.quantity = quantity;
        this.validTarget = validTarget;

        subQuestMessage = "Attrape " + quantity + " fois une des entités suivantes avec une canne é péche :";
        for (EntityType target : validTarget) {
            subQuestMessage += "\n    -> " + target;
        }
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

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getCaught() == null) return;
        if (event.getPlayer() != player) return;
        if (!validTarget.contains(event.getCaught().getType())) return;

        if (--quantity <= 0) {
            stopQuest();
        }
    }
}
