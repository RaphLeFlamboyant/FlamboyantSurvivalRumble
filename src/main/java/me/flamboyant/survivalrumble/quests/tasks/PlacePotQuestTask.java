package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlacePotQuestTask extends APlayerInteractQuestTask {
    private Material flower;
    private int quantity;

    public PlacePotQuestTask(Quest ownerQuest, Material flower, int quantity, boolean inHQ) {
        super(ownerQuest);

        this.flower = flower;
        this.quantity = quantity;

        subQuestMessage = "Poses " + quantity + " pots de fleur de type " + flower + (inHQ ? " dans la base" : "");
    }

    @Override
    protected boolean doOnEvent(PlayerInteractEvent event) {
        System.out.println(" ----- PlacePotQuestComponent-PlayerInteractEvent by " + event.getPlayer() + " ----- ");
        if (event.getPlayer() != player) return false;
        if (!event.hasItem()) return false;
        System.out.println("Used item : " + event.getItem().getType());
        if (event.getItem().getType() != flower) return false;
        System.out.println("Clicked block : " + event.getClickedBlock().getType());
        if (event.getClickedBlock().getType() != Material.FLOWER_POT) return false;
        System.out.println("useItemInHand : " + event.useItemInHand());
        if (event.useItemInHand() == Event.Result.DENY) return false;

        return --quantity == 0;
    }
}
