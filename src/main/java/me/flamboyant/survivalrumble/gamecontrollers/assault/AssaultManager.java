package me.flamboyant.survivalrumble.gamecontrollers.assault;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.ChatHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class AssaultManager implements Listener {
    private HashMap<Player, Location> playerLastLocation = new HashMap<>();
    private HashMap<String, Location> assaultSpawnByTeamTarget = new HashMap<>();
    private BukkitTask locationDefenderTask;

    private static AssaultManager instance;
    protected AssaultManager() {

    }

    public static AssaultManager getInstance() {
        if (instance == null) {
            instance = new AssaultManager();
        }

        return instance;
    }

    public void start() {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        for (String teamName : data.getTeams()) {
            Player champion = data.getTeamChampion(teamName);
            Location teamHq = data.getHeadquarterLocation(teamName);

            champion.setGameMode(GameMode.SPECTATOR);
            champion.teleport(teamHq);
            playerLastLocation.put(champion, teamHq);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, champion), 0);

            String targetTeamName = data.getTeamTargetTeam(teamName);
            Location targetTeamHq = data.getHeadquarterLocation(targetTeamName);
            Location spawnPoint = targetTeamHq.add(55, 0, 0);
            spawnPoint = new Location(spawnPoint.getWorld(), spawnPoint.getX(), spawnPoint.getWorld().getHighestBlockYAt(spawnPoint), spawnPoint.getZ());
            assaultSpawnByTeamTarget.put(targetTeamName, spawnPoint);
            for (Player player : data.getPlayers(teamName)) {
                if (player == champion) continue;

                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(spawnPoint);
                playerLastLocation.put(player, spawnPoint);
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, player), 0);
            }
        }

        locationDefenderTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> locationDefender(), 0, 20);

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(locationDefenderTask.getTaskId());
        //PlayerDeathListener.getInstance().stop();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player deadPlayer = (Player) event.getEntity();
        if (event.getFinalDamage() < deadPlayer.getHealth()) return;

        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        String teamName = data.getPlayerTeam(deadPlayer);

        if (deadPlayer != data.getTeamChampion(teamName)) {
            String targetTeamName = data.getTeamTargetTeam(teamName);
            Location spawnPoint = assaultSpawnByTeamTarget.get(targetTeamName);

            resetPlayerState(deadPlayer);
            playerLastLocation.put(deadPlayer, spawnPoint);
            deadPlayer.setGameMode(GameMode.SPECTATOR);
            deadPlayer.teleport(spawnPoint);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, deadPlayer), 0);
        }
        else {
            if (data.getTeams().size() == 2) {
                data.removeTeam(teamName);
                Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + teamName + " est éliminée !"));
                String winTeamName = data.getTeams().get(0);
                Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + winTeamName + " a gagné !"));

                Location spawnPoint = data.getHeadquarterLocation(winTeamName);
                for (Player player : Common.server.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(spawnPoint);
                }

                stop();
            }

            for (Player player : data.getPlayers(teamName)) {
                player.setGameMode(GameMode.SPECTATOR);
            }

            Bukkit.broadcastMessage(ChatHelper.importantMessage("L'équipe " + teamName + " est éliminée !"));

            String targetTeam = data.getTeamTargetTeam(teamName);
            Location spawnPoint = assaultSpawnByTeamTarget.get(targetTeam);

            data.removeTeam(teamName);

            String assaultTeam = data.getTeamAssaultTeam(teamName);
            for (Player player : data.getPlayers(assaultTeam)) {
                if (player == data.getTeamChampion(teamName))
                    continue;

                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(spawnPoint);
                playerLastLocation.put(player, spawnPoint);
                Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(10, player), 0);
            }
        }
    }

    private void locationDefender() {
        SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
        for (String teamName : data.getTeams()) {
            Player champion = data.getTeamChampion(teamName);
            if (TeamHelper.isLocationInHeadQuarter(champion.getLocation(), teamName)) {
                playerLastLocation.put(champion, champion.getLocation());
            } else {
                champion.teleport(playerLastLocation.get(champion));
            }

            for (Player player : data.getPlayers(teamName)) {
                if (player == champion) continue;

                if (player.getGameMode() == GameMode.SPECTATOR && TeamHelper.isLocationInHeadQuarter(player.getLocation(), teamName)) {
                    player.teleport(playerLastLocation.get(player));
                }
                else {
                    playerLastLocation.put(player, player.getLocation());
                }
            }
        }
    }

    private void countdownPlayer(Integer count, Player player) {
        if (count-- > 0) {
            int finalCount = count;
            player.sendTitle(count.toString(), "", 0, 19, 0);
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> countdownPlayer(finalCount, player), 20);
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
    }

    private void resetPlayerState(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(3);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
