package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.ElectricianClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.*;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ElectricianClass extends APlayerClass {
    // TODO : ajouter une écoute de mouvements par piston pour maj la liste de location (via BlockPistonExtendEvent par ex)
    private HashMap<Material, Integer> scoreByBlockType = new HashMap<Material, Integer>() {{
        put(Material.COAL_BLOCK, 1);
        put(Material.IRON_BLOCK, 5);
        put(Material.GOLD_BLOCK, 5);
        put(Material.REDSTONE_BLOCK, 5);
        put(Material.LAPIS_BLOCK, 15);
        put(Material.EMERALD_BLOCK, 15);
        put(Material.DIAMOND_BLOCK, 30);
        put(Material.NETHERITE_BLOCK, 50);
    }};

    private HashMap<Material, Float> scoreLossByBrokenBlockType = new HashMap<Material, Float>() {{
        put(Material.COAL_BLOCK, 1f);
        put(Material.IRON_BLOCK, 0.9f);
        put(Material.GOLD_BLOCK, 0.8f);
        put(Material.REDSTONE_BLOCK, 1f);
        put(Material.LAPIS_BLOCK, 1f);
        put(Material.EMERALD_BLOCK, 0.5f);
        put(Material.DIAMOND_BLOCK, 0.9f);
        put(Material.NETHERITE_BLOCK, 0.9f);
    }};

    private ElectricianClassData classData;

    private int checkInterval = 5;
    private float total = 0f;

    public ElectricianClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);

        scoringDescription = "Poser, exposés au ciel, des blocs de ressource issus de minerais (fer, redstone, diamant, etc)";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELECTRICIAN;
    }

    @Override
    public PlayerClassData buildClassData() { return new ElectricianClassData(); }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (ElectricianClassData) data().getPlayerClassData(owner);
        Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
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
        String ownerTeam = data().getPlayerTeam(owner);
        if (concernedTeamName == null || !ownerTeam.equals(concernedTeamName)) return;
        if (location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) != location.getBlockY())
            return;

        handleBlockLocation(classData.blockLocationList, block, broken);
    }

    private void handleBlockLocation(HashMap<Location, Float> blockLocationAndScore, Block block, boolean broken) {
        if (!broken) {
            blockLocationAndScore.put(block.getLocation(), 0f);
            return;
        }

        Location loc = block.getLocation();
        for (Location existingLocation : blockLocationAndScore.keySet()) {
            if (existingLocation.equals(loc)) {
                float score = blockLocationAndScore.get(loc);
                blockLocationAndScore.remove(loc);
                int lastTotal = (int) total;
                total -= score * scoreLossByBrokenBlockType.get(block.getType());
                GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), -(lastTotal - (int) total));
                return;
            }
        }
    }

    private void updateScoring() {
        int lastTotal = (int) total;

        for (Location loc : classData.blockLocationList.keySet()) {
            if (loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) == loc.getBlockY()) {
                float score = classData.blockLocationList.get(loc);
                float earned = scoreByBlockType.get(loc.getBlock().getType()) * checkInterval / 60f; // coef is score by minute
                score += earned;
                total += earned;
                classData.blockLocationList.put(loc, score);
            }
        }

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), ((int) total - lastTotal));
    }
}
