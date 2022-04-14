package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;
import java.util.stream.Collectors;

public class HookPlayersFromTeamQuestComponent extends HookPlayerQuestComponent {
    private List<String> targetTeam;

    public HookPlayersFromTeamQuestComponent(Player player, int quantity, List<String> targetTeam) {
        super(player, quantity);

        this.targetTeam = targetTeam;
        subQuestMessage = "Attrape " + quantity + " fois un joueur d'une des équipes suivantes avec une canne é péche : ";

        for (String teamName : targetTeam) {
            subQuestMessage += teamName + ", ";
        }
        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2);
    }

    public HookPlayersFromTeamQuestComponent(Player player, int quantity, boolean targetFoes) {
        super(player, quantity);

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String ownerTeam = data.playersTeam.get(player.getUniqueId());
        this.targetTeam = data.teams.stream().filter(t -> targetFoes ? !ownerTeam.equals(t) : ownerTeam.equals(t)).collect(Collectors.toList());
        subQuestMessage = "Attrape " + quantity + " fois un joueur " + (targetFoes ? "ennemi" : "allié") + " avec une canne é péche";
    }

    @Override
    protected boolean checkFishCondition(PlayerFishEvent event) {
        Player hookedPlayer = (Player) event.getCaught();
        return targetTeam.contains(SurvivalRumbleData.getSingleton().playersTeam.get(hookedPlayer.getUniqueId()));
    }
}
