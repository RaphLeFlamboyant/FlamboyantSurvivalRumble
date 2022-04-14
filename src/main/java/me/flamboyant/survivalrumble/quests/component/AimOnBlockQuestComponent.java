package me.flamboyant.survivalrumble.quests.component;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AimOnBlockQuestComponent extends AAimQuestComponent {
    private Material target;

    public AimOnBlockQuestComponent(Player player, EntityType entityTypeToUse, int quantity, Material target) {
        super(player, entityTypeToUse, quantity);

        this.target = target;

        subQuestMessage = "Touche " + quantity + " fois un " + target + " é l'aide de " + entityTypeToUse;
    }

    protected boolean doOnEvent(ProjectileHitEvent event) {
        return event.getHitBlock() != null && event.getHitBlock().getType() == target;
    }
}
