package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MasonClass extends APlayerClass {
    public MasonClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "Poser des briques rapporte le double de points";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.MASON;
    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned) {
        if (Arrays.asList(Material.BRICKS, Material.BRICK_STAIRS, Material.BRICK_SLAB, Material.BRICK_WALL).contains(blockData.getMaterial())
                && teamConcerned.equals(data().getPlayerTeam(owner))) {
            score *= 2;
        }

        return score;
    }
}
