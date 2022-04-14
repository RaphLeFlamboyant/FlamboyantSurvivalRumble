package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public class FurnaceExtractQuestComponent extends AQuestComponent implements Listener {
    private Material materialResult;
    private int quantity;

    public FurnaceExtractQuestComponent(Player player, Material materialResult, int quantity) {
        super(player);

        this.materialResult = materialResult;
        this.quantity = quantity;

        subQuestMessage = "Fais cuire " + quantity + " x " + materialResult + " dans un four";
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        player.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        FurnaceExtractEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getItemType() != materialResult) return;

        quantity -= event.getItemAmount();

        if (quantity <= 0) {
            stopQuest();
        }
    }
}
