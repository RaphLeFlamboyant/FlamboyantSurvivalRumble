package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketFillQuestTask extends AQuestTask implements Listener {
    private Material material;
    private int quantity;

    public BucketFillQuestTask(Quest ownerQuest, Material material, int quantity) {
        super(ownerQuest);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Remplis " + quantity + " x seaux avec " + material.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        PlayerBucketFillEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getBlockClicked().getType() != material) return;

        quantity--;

        if (quantity <= 0) {
            stopQuest(true);
        }
    }
}
