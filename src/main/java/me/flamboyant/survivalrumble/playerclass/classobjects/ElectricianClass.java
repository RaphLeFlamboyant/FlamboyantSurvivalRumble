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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;

public class ElectricianClass extends APlayerClass implements Listener {
    // TODO : ajouter une écoute de mouvements par piston pour maj la liste de location (via BlockPistonExtendEvent par ex)
    private HashMap<Material, Integer> scoreByBlockType = new HashMap<>() {{
        put(Material.COAL_BLOCK, 1);
        put(Material.IRON_BLOCK, 5);
        put(Material.GOLD_BLOCK, 5);
        put(Material.REDSTONE_BLOCK, 5);
        put(Material.LAPIS_BLOCK, 15);
        put(Material.EMERALD_BLOCK, 15);
        put(Material.DIAMOND_BLOCK, 30);
        put(Material.NETHERITE_BLOCK, 50);
    }};

    private ElectricianClassData classData;

    private int checkInterval = 5;
    private float leftovers;

    public ElectricianClass(Player owner) {
        super(owner);

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
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockBurnEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleBlockBreak(event.getBlock());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        handleBlockBreak(event.getBlock());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        handleBlockBreak(event.getBlock());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (var block : event.blockList()) {
            handleBlockBreak(block);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        handleBlockPlace(event.getBlock());
    }

    private boolean shouldWeCareAboutThisBlock(Block block) {
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeam = data().getPlayerTeam(owner);
        if (concernedTeamName == null || !ownerTeam.equals(concernedTeamName)) return false;
        if (location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) != location.getBlockY())
            return false;

        return true;
    }

    private void handleBlockBreak(Block block) {
        if (!shouldWeCareAboutThisBlock(block)) return;

        classData.blockLocationList.remove(block.getLocation());
    }

    private void handleBlockPlace(Block block) {
        if (!shouldWeCareAboutThisBlock(block)) return;
        var blockLocation = block.getLocation();

        for (var l : classData.blockLocationList) {
            if (l.getBlockX() == blockLocation.getBlockX() && l.getBlockZ() == blockLocation.getBlockZ()) {
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> classData.blockLocationList.remove(l), 1);
            }
        }

        if (scoreByBlockType.containsKey(block.getType())) {
            classData.blockLocationList.add(block.getLocation());
        }

    }

    private void updateScoring() {
        var amount = 0f;

        for (Location loc : classData.blockLocationList) {
            if (loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) == loc.getBlockY()) {
                amount += scoreByBlockType.get(loc.getBlock().getType()) * checkInterval / 60f;
            }
        }

        amount += leftovers;
        leftovers = amount % 1f;

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), ((int) amount));
    }
}
