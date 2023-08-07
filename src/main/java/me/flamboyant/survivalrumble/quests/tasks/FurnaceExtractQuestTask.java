package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public class FurnaceExtractQuestTask extends AQuestTask implements Listener {
    private Material materialResult;
    private int quantity;

    public FurnaceExtractQuestTask(Quest ownerQuest, Material materialResult, int quantity) {
        super(ownerQuest);

        this.materialResult = materialResult;
        this.quantity = quantity;

        subQuestMessage = "Fais cuire " + quantity + " x " + materialResult + " dans un four";
    }

    @Override
    public void startQuest(Player player) {
        super.startQuest(player);
        this.player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest(boolean success) {
        FurnaceExtractEvent.getHandlerList().unregister(this);
        super.stopQuest(success);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getItemType() != materialResult) return;

        quantity -= event.getItemAmount();

        if (quantity <= 0) {
            stopQuest(true);
        }
    }
}
