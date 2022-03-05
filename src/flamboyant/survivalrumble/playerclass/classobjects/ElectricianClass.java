package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import flamboyant.survivalrumble.utils.ScoringTriggerType;
import flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElectricianClass extends APlayerClass {
    // TODO : ajouter une écoute de mouvements par piston pour maj la liste de location (via BlockPistonExtendEvent par ex)
    private List<Location> tier1BlockLocationList = new ArrayList<>();
    private List<Location> tier2BlockLocationList = new ArrayList<>();
    private List<Location> tier3BlockLocationList = new ArrayList<>();
    private List<Location> tier4BlockLocationList = new ArrayList<>();

    public ElectricianClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELECTRICIAN;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> updateScoring(), 0l, 200l);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;
        if (location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) != location.getBlockY())
            return;

        if (Arrays.asList(Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.REDSTONE_BLOCK).contains(block.getType())) {
            handleBlockLocation(tier1BlockLocationList, block, broken);
        } else if (Arrays.asList(Material.EMERALD_BLOCK, Material.LAPIS_BLOCK).contains(block.getType())) {
            handleBlockLocation(tier2BlockLocationList, block, broken);
        } else if (Arrays.asList(Material.DIAMOND_BLOCK).contains(block.getType())) {
            handleBlockLocation(tier3BlockLocationList, block, broken);
        } else if (Arrays.asList(Material.NETHERITE_BLOCK).contains(block.getType())) {
            handleBlockLocation(tier4BlockLocationList, block, broken);
        }
    }

    private void handleBlockLocation(List<Location> blockLocationList, Block block, boolean broken) {
        if (!broken) {
            blockLocationList.add(block.getLocation());
            return;
        }

        Location loc = block.getLocation();
        for (Location existingLocation : blockLocationList) {
            if (existingLocation.equals(loc)) {
                blockLocationList.remove(existingLocation);
                return;
            }
        }
    }

    private void updateScoring() {
        double scoreDelta = numberOfCorrectLocation(tier1BlockLocationList)
                + numberOfCorrectLocation(tier2BlockLocationList) * 2.0
                + numberOfCorrectLocation(tier3BlockLocationList) * 3.0
                + numberOfCorrectLocation(tier4BlockLocationList) * 10.0;
        int scoreToCountNow = (int) scoreDelta;

        ScoringHelper.addScore(data().playersTeam.get(owner.getUniqueId()), scoreToCountNow, ScoreType.FLAT);
    }

    private int numberOfCorrectLocation(List<Location> blockLocationList) {
        int res = 0;

        for (Location loc : blockLocationList) {
            if (loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) == loc.getBlockY())
                res++;
        }

        return res;
    }
}
