package me.flamboyant.survivalrumble.quests;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class BaseBlocPlaceQuest extends Quest implements Listener {
    HashMap<Material, Integer> blocsToPlace;

    public BaseBlocPlaceQuest(APlayerClass playerClass, Player owner, String questTitle, String questMessage, HashMap<Material, Integer> blocsToPlace, int flatPoints, int perfectPoints) {
        super(playerClass, owner, questTitle, questMessage, flatPoints, perfectPoints);

        this.blocsToPlace = blocsToPlace;
    }

    @Override
    public void showQuestMessage() {
        String corpus = "Place les blocs suivants dans ta base :\n";
        for (Material item : blocsToPlace.keySet()) {
            corpus += blocsToPlace.get(item) + " x " + item;
        }

        String message = ChatUtils.questAnnouncement(questTitle, "" + (flatPoints == 0 ? perfectPoints : flatPoints), corpus);
        owner.sendMessage(message);
    }

    @Override
    public void startQuest() {
        super.startQuest();
        owner.getServer().getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stopQuest() {
        BlockPlaceEvent.getHandlerList().unregister(this);
        super.stopQuest();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer() != owner) return;
        Material blocType = event.getBlock().getType();
        if (!blocsToPlace.keySet().contains(blocType)) return;
        if (!TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), SurvivalRumbleData.getSingleton().playersTeam.get(owner.getUniqueId())))
            return;

        blocsToPlace.put(blocType, blocsToPlace.get(blocType) - 1);

        if (blocsToPlace.get(blocType) == 0)
            blocsToPlace.remove(blocType);

        if (blocsToPlace.size() <= 0) {
            stopQuest();
        }
    }
}
