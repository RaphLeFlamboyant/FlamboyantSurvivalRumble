package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class MasonClass extends APlayerClass
{
    public MasonClass(Player owner)
    {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.MASON;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {

    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned)
    {
        if (Arrays.asList(Material.BRICKS, Material.BRICK_STAIRS, Material.BRICK_SLAB, Material.BRICK_WALL).contains(blockData.getMaterial())
                && teamConcerned.equals(data().playersTeam.get(owner.getUniqueId()))){
            score *= 2;
        }

        return score;
    }
}
