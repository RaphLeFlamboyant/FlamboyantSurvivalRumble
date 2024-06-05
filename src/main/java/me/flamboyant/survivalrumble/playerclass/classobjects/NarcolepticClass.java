package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitTask;

public class NarcolepticClass extends APlayerClass implements Listener {
    private static final int validDistance = 35;

    private BukkitTask sleepTask;

    public NarcolepticClass(Player owner) {
        super(owner);

        scoringDescription = "Dormir quand il y a des ennemis proches de ta base.";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.NARCOLEPTIC;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        PlayerBedEnterEvent.getHandlerList().unregister(this);
        PlayerBedLeaveEvent.getHandlerList().unregister(this);

        Bukkit.getScheduler().cancelTask(sleepTask.getTaskId());
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getPlayer() != owner) return;

        sleepTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> playerBedTickHandle(), 20, 20);
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (event.getPlayer() != owner) return;

        Bukkit.getScheduler().cancelTask(sleepTask.getTaskId());
    }

    private void playerBedTickHandle() {
        var ownerTeam = data().getPlayerTeam(owner);

        for (Player player : Common.server.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) continue;
            var playerTeam = data().getPlayerTeam(player);
            if (playerTeam == ownerTeam || playerTeam == null) continue;

            if (TeamHelper.isLocationInHeadQuarter(player.getLocation(), ownerTeam)) {
                GameManager.getInstance().addAddMoney(ownerTeam, 25);
            }
        }
    }
}
