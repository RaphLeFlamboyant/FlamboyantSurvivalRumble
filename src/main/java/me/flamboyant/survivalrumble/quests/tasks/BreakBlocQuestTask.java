package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlocQuestTask extends AQuestTask implements Listener {
    private Material material;
    private int quantity;

    public BreakBlocQuestTask(Quest ownerQuest, Material material, int quantity) {
        super(ownerQuest);
        this.material = material;
        this.quantity = quantity;

        subQuestMessage = "Casse " + quantity + " x " + material.toString();
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        BlockBreakEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getBlock().getType() != material) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
