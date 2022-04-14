package me.flamboyant.survivalrumble.comebackmechanics.impl;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.comebackmechanics.IComebackMechanics;
import me.flamboyant.survivalrumble.comebackmechanics.IComebackMechanicsOwner;
import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CaptureTheFlagMechanics implements Listener, IComebackMechanics {
    private List<String> ongoingActivated = new ArrayList<>();
    private HashMap<UUID, String> playerWhoHasFlag = new HashMap<>();
    private NamespacedKey namespaceKey;
    private IComebackMechanicsOwner owner;

    public CaptureTheFlagMechanics(IComebackMechanicsOwner owner) {
        this.owner = owner;
        namespaceKey = new NamespacedKey(Common.plugin, "captureFlagMechanic");
    }

    public void activate(String bestTeamName) {
        System.out.println("Activating mechanics for team " + bestTeamName);
        if (ongoingActivated.size() == 0) {
            Common.server.getPluginManager().registerEvents(this, Common.plugin);
        }

        ongoingActivated.add(bestTeamName);
        spawnFlag(bestTeamName);
    }

    private void spawnFlag(String teamName) {
        System.out.println("Spawning flag");
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Location loc = data.teamHeadquarterLocation.get(teamName);
        Chunk teamChunk = Common.server.getWorld("world").getChunkAt(loc);
        if (!teamChunk.isLoaded()) {
            System.out.println("Chunk unloaded");
            teamChunk.load();
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                placeFlag(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), teamName);
                teamChunk.unload(true);
            }, 1L);
        }
        else {
            placeFlag(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), teamName);
        }
    }

    private void placeFlag(Location location, String teamName) {
        System.out.println("placeFlag at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        Block block = location.getBlock();
        block.setType(TeamHelper.getTeamBannerMaterial(teamName));
        Banner banner = (Banner) block.getState();
        PersistentDataContainer pdc = banner.getPersistentDataContainer();
        pdc.set(namespaceKey, PersistentDataType.STRING, teamName);
        banner.update();
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Block block = event.getClickedBlock();
        if (block == null) return;
        Location loc = block.getLocation();
        loc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        for (String teamName : ongoingActivated) {
            if (loc.equals(data.teamHeadquarterLocation.get(teamName))) {
                event.setCancelled(true);
                if (TeamHelper.getTeamBannerMaterial(teamName) != block.getType()) return;
                Player player = event.getPlayer();
                if (data.playersTeam.get(player.getUniqueId()).equals(teamName)) return;
                Material material = block.getType();
                block.setType(Material.AIR);
                Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("LE DRAPEAU DE L'ÉQUIPE " + teamName + " A ÉTÉ CAPTURÉ",
                        "Le drapeau de l'équipe " + teamName + " a été capturé par le joueur " + event.getPlayer().getDisplayName()));
                playerWhoHasFlag.put(player.getUniqueId(), teamName);

                Inventory playerInv = player.getInventory();
                if (playerInv.firstEmpty() == -1) {
                    ItemStack item = playerInv.getItem(26);
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                    playerInv.remove(item);
                }
                event.getPlayer().getInventory().addItem(new ItemStack(material));

                return;
            }
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Player player = event.getEntity();
        if (!playerWhoHasFlag.containsKey(player.getUniqueId())) return;

        String teamName = playerWhoHasFlag.get(player.getUniqueId());
        playerWhoHasFlag.remove(player.getUniqueId());

        if (player.getKiller() != null && data.playersTeam.get(player.getKiller().getUniqueId()).equals(teamName)) {
            Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("LE DRAPEAU DE L'ÉQUIPE " + teamName + " A ÉTÉ RESTITUÉ",
                    "Le drapeau de l'équipe " + teamName + " a été restitué et est donc de nouveau dans leur base"));
            spawnFlag(teamName);
        }
        else {
            Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("LE DRAPEAU DE L'ÉQUIPE " + teamName + " A ÉTÉ REPRIS",
                    "Le drapeau de l'équipe " + teamName + " a été capturé par le joueur " + player.getKiller().getDisplayName()));
            playerWhoHasFlag.put(player.getKiller().getUniqueId(), teamName);

            Inventory killerInv = player.getKiller().getInventory();
            if (killerInv.firstEmpty() == -1) {
                ItemStack item = killerInv.getItem(26);
                player.getKiller().getWorld().dropItemNaturally(player.getKiller().getLocation(), item);
                killerInv.remove(item);
            }
            player.getKiller().getInventory().addItem(new ItemStack(TeamHelper.getTeamBannerMaterial(teamName)));
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Block block = event.getBlock();
        Location loc = block.getLocation();
        loc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        for (String teamName : ongoingActivated) {
            if (loc.equals(data.teamHeadquarterLocation.get(teamName))) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    private void onBlockExplode(BlockExplodeEvent event) {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        List<Block> blockList = event.blockList();

        for (Block block : blockList) {
            Location loc = block.getLocation();
            Location baseLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
            for (String teamName : ongoingActivated) {
                if (baseLoc.equals(data.teamHeadquarterLocation.get(teamName))) {
                    Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
                        placeFlag(loc, teamName);
                    }, 1L);
                    return;
                }
            }
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!playerWhoHasFlag.containsKey(playerId)) return;
        if (event.getBlock().getType() != TeamHelper.getTeamBannerMaterial(playerWhoHasFlag.get(playerId))) return;

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        Block block = event.getBlock();
        String teamName = playerWhoHasFlag.get(playerId);
        String catcherTeamName = data.playersTeam.get(playerId);

        playerWhoHasFlag.remove(playerId);

        if (TeamHelper.isLocationInHeadQuarter(event.getBlock().getLocation(), catcherTeamName)) {
            ongoingActivated.remove(teamName);
            if (ongoingActivated.size() == 0) unregisterEvents();
            owner.onMechanicsEnd(teamName);

            Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("L'ÉQUIPE " + catcherTeamName + " A RAMENÉ LE DRAPEAU DE L'ÉQUIPE " + teamName + " À SA BASE",
                    "Le drapeau de l'équipe " + teamName + " a été ramené à la base de l'équipe " + catcherTeamName + " par " + player.getDisplayName()));
            int delta = data.getTotalScore(teamName) - data.getTotalScore(catcherTeamName);
            if (delta > 0) {
                GameManager.getInstance().addScore(catcherTeamName, delta / 2, ScoreType.FLAT);
            }
        }
        else {
            Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("LE DRAPEAU DE L'ÉQUIPE " + teamName + " A ÉTÉ ABANDONNÉ",
                    "Le drapeau de l'équipe " + teamName + " a été abandonné et est donc de nouveau dans leur base"));
            spawnFlag(teamName);
        }
        block.setType(Material.AIR);
    }

    public void unregisterEvents() {
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
    }
}
