package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AimOnEntityQuestTask extends AAimQuestTask {
    private EntityType target;
    private boolean friendlyFire;

    public AimOnEntityQuestTask(Quest ownerQuest, EntityType entityTypeToUse, int quantity, EntityType target, boolean friendlyFire) {
        super(ownerQuest, entityTypeToUse, quantity);
        this.target = target;
        this.friendlyFire = friendlyFire;

        subQuestMessage = "Touche " + quantity + " fois un " + target + " é l'aide de " + entityTypeToUse;
    }

    protected boolean doOnEvent(ProjectileHitEvent event) {
        if (event.getHitEntity() == null || event.getHitEntity().getType() != target) return false;
        if (event.getHitEntity().getType() == EntityType.PLAYER) {
            Player target = (Player) event.getHitEntity();
            SurvivalRumbleData data = SurvivalRumbleData.getSingleton();
            String playerTeam = data.playersTeam.get(player.getUniqueId());
            if (!friendlyFire && data.playersTeam.get(target.getUniqueId()).equals(playerTeam)) return false;
        }

        return true;
    }

}
