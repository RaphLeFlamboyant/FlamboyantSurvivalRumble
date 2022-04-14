package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketFillQuestComponent extends AQuestComponent implements Listener {
    private Material material;
    private int quantity;

    public BucketFillQuestComponent(Player player, Material material, int quantity) {
        super(player);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Remplis " + quantity + " x seaux avec " + material.toString();
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        PlayerBucketFillEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getBlockClicked().getType() != material) return;

        if (quantity <= 0) {
            stopQuest();
        }
    }
}
