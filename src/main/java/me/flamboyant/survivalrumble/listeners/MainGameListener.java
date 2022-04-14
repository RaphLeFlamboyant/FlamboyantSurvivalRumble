package me.flamboyant.survivalrumble.listeners;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainGameListener implements Listener {
    private JavaPlugin plugin;
    private Server server;

    private Map<UUID, Integer> playerCompassingTeamIndex = new HashMap<>();

    public MainGameListener(JavaPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    private SurvivalRumbleData data() {
        return SurvivalRumbleData.getSingleton();
    }

    public void initListener() {
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            Location loc = data().teamHeadquarterLocation.get(data().playersTeam.get(event.getPlayer().getUniqueId()));
            event.setRespawnLocation(loc);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.DEATH)) {
                System.out.println("TOTO TEST");
                playerClass.onPlayerDeathTrigger(event.getEntity(), event.getEntity().getKiller());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BREAK)) {
            playerClass.onBlockBreakTrigger(event.getPlayer(), event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        BlockData blockData = event.getBlock().getBlockData();
        handleBlockDestruction(blockData, blockLocation);
        data().saveData();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_BURNED)) {
            playerClass.onBlockBurnedTrigger(event.getBlock());
        }

        Location blockLocation = event.getBlock().getLocation();
        BlockData blockData = event.getBlock().getBlockData();
        handleBlockDestruction(blockData, blockLocation);
        data().saveData();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_EXPLOSION)) {
            playerClass.onExplosionTrigger(event.getBlock());
        }

        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            BlockData blockData = block.getBlockData();
            handleBlockDestruction(blockData, blockLocation);
        }

        data().saveData();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            Location blockLocation = block.getLocation();
            BlockData blockData = block.getBlockData();
            handleBlockDestruction(blockData, blockLocation);
        }

        data().saveData();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_PLACE)) {
            playerClass.onBlockPlaceTrigger(event.getPlayer(), event.getBlock());
        }

        Location location = event.getBlock().getLocation();
        String concernedTeam = TeamHelper.getTeamHeadquarterName(location);

        if (concernedTeam == null)
            return;

        MaterialHelper mh = new MaterialHelper();
        BlockData blockData = event.getBlock().getBlockData();
        if (mh.scoringMaterial.containsKey(blockData.getMaterial())) {
            int scoreChange = mh.scoringMaterial.get(blockData.getMaterial());

            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_MODIFIER)) {
                scoreChange = playerClass.onBlockModifierTrigger(scoreChange, blockData, location, concernedTeam);
            }

            int changes = (int) (scoreChange * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()));
            GameManager.getInstance().addScore(concernedTeam, changes, ScoreType.REVERSIBLE);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.COMPASS) {
            int index = 0;
            if (!playerCompassingTeamIndex.containsKey(player.getUniqueId()))
                playerCompassingTeamIndex.put(player.getUniqueId(), 0);
            else {
                index = playerCompassingTeamIndex.get(player.getUniqueId());
                index = (index + 1) % data().teams.size();
                playerCompassingTeamIndex.put(player.getUniqueId(), index);
            }

            Location hq = data().teamHeadquarterLocation.get(data().playersTeam.get(player.getUniqueId()));
            player.setCompassTarget(hq);
            String teamName = data().teams.get(index);
            player.sendMessage(ChatUtils.feedback("Ton compas pointe désormais la base de la team " + TeamHelper.getTeamColor(teamName) + teamName));
        }
    }

    private void handleBlockDestruction(BlockData blockData, Location blockLocation) {
        String concernedTeam = TeamHelper.getTeamHeadquarterName(blockLocation);

        if (concernedTeam == null)
            return;

        MaterialHelper mh = new MaterialHelper();
        if (mh.scoringMaterial.containsKey(blockData.getMaterial())) {
            int scoreChange = mh.scoringMaterial.get(blockData.getMaterial());

            if (blockData instanceof Slab) {
                Slab slab = (Slab) blockData;
                if (slab.getType() == Slab.Type.DOUBLE)
                    scoreChange *= 2;
            }

            for (APlayerClass playerClass : PlayerClassMechanicsHelper.getSingleton().connectedClasses.get(ScoringTriggerType.BLOCK_MODIFIER)) {
                scoreChange = playerClass.onBlockModifierTrigger(scoreChange, blockData, blockLocation, concernedTeam);
            }

            int changes = -(int) (scoreChange * ScoringHelper.scoreAltitudeCoefficient(blockLocation.getBlockY()));
            GameManager.getInstance().addScore(concernedTeam, changes, ScoreType.REVERSIBLE);
        }
    }

    /*
	public void scheduleRandomMeetup(JavaPlugin plugin, Server server)
	{
		if (data().pvpIntensity == 0)
			return;

		int remaningTime = data().minutesBeforeEnd;
		int timePart = remaningTime / data().pvpIntensity;
		Random rng = new Random();

		for (int i = 0; i < data().pvpIntensity; i++)
		{
			int meetupTime = i * timePart + timePart * 1 / 20 + rng.nextInt(timePart * 9 / 10);

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				Bukkit.broadcastMessage("é4Oh non ! Un Meetup sauvage est apparu ! ");
				World world = server.getWorld("world");
				Location zeroLocation = new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
				OfflinePlayer[] players = server.getOfflinePlayers();

				for (OfflinePlayer offPlayer : players)
				{
					Player player = offPlayer.getPlayer();
					if (player == null)
						continue;
					player.teleport(zeroLocation);
				}
			}, meetupTime * 60 * 20L);

			data().meetupTimer.add(meetupTime);
		}

		data().saveData();
	}
	     */
}
