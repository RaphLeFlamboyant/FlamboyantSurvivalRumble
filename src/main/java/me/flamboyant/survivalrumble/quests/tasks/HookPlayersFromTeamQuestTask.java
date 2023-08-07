package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;
import java.util.stream.Collectors;

public class HookPlayersFromTeamQuestTask extends HookPlayerQuestTask {
    private List<String> targetTeam;

    public HookPlayersFromTeamQuestTask(Quest ownerQuest, int quantity, List<String> targetTeam) {
        super(ownerQuest, quantity);

        this.targetTeam = targetTeam;
        subQuestMessage = "Attrape " + quantity + " fois un joueur d'une des équipes suivantes avec une canne é péche : ";

        for (String teamName : targetTeam) {
            subQuestMessage += teamName + ", ";
        }
        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2);
    }

    public HookPlayersFromTeamQuestTask(Quest ownerQuest, int quantity, boolean targetFoes) {
        super(ownerQuest, quantity);

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String ownerTeam = data.getPlayerTeam(player);
        this.targetTeam = data.getTeams().stream().filter(t -> targetFoes ? !ownerTeam.equals(t) : ownerTeam.equals(t)).collect(Collectors.toList());
        subQuestMessage = "Attrape " + quantity + " fois un joueur " + (targetFoes ? "ennemi" : "allié") + " avec une canne é péche";
    }

    @Override
    protected boolean checkFishCondition(PlayerFishEvent event) {
        Player hookedPlayer = (Player) event.getCaught();
        return targetTeam.contains(SurvivalRumbleData.getSingleton().getPlayerTeam(hookedPlayer));
    }
}
