package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.gamecontrollers.assault.IAssaultStepListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShieldRemovalPower implements IChampionPower, IAssaultStepListener {
    private Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;
        removeFoesShield();
    }

    @Override
    public void deactivate() {

    }

    private void removeFoesShield() {
        var data = SurvivalRumbleData.getSingleton();
        var ownerTeamName = data.getPlayerTeam(powerOwner);
        var foeTeamName = data.getTeamAssaultTeam(ownerTeamName);
        var foeChampion = data.getTeamChampion(foeTeamName);

        for (var player : data.getPlayers(foeTeamName)) {
            if (player == foeChampion)
                continue;

            player.getInventory().remove(Material.SHIELD);
        }
    }

    @Override
    public void onTeamEliminated() {
        removeFoesShield();
    }
}
