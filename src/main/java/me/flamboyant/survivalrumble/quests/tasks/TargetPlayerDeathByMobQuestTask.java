package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class TargetPlayerDeathByMobQuestTask extends TargetPlayerDeathQuestTask {
    private List<EntityType> expectedKiller;

    public TargetPlayerDeathByMobQuestTask(Quest ownerQuest, Player playerToDie, int quantity, List<EntityType> expectedKiller) {
        super(ownerQuest, playerToDie, quantity);

        this.expectedKiller = expectedKiller;
    }

    @Override
    public String getSubQuestMessage() {
        if (subQuestMessage == null) {
            String subject = playerToDie == this.player ? "Mourrez" : "Le joueur " + playerToDie.getDisplayName() + " doit mourir";
            if (quantity > 1)
                subject += quantity + " fois";
            if (expectedKiller.size() > 1) {
                String mobListString = "";
                for (EntityType entityType : expectedKiller) {
                    mobListString = mobListString + entityType + ", ";
                }

                subQuestMessage = subject + " de la main d'un des monstres suivants : " + mobListString.substring(0, mobListString.length() - 2);
            } else
                subQuestMessage = subject + " de la main d'un " + expectedKiller.get(0);
        }

        return super.getSubQuestMessage();
    }

    @Override
    protected boolean checkDeathCondition(PlayerDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) return false;
        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
        EntityType damager = damageEvent.getDamager().getType();
        if (damageEvent.getDamager() instanceof AbstractArrow) {
            AbstractArrow dmger = (AbstractArrow) damageEvent.getDamager();
            if (dmger.getShooter() instanceof LivingEntity) {
                LivingEntity ety = (LivingEntity) dmger.getShooter();
                damager = ety.getType();
            }
        }
        System.out.println("Damage cause : " + damageEvent.getDamager().getType());
        return expectedKiller.contains(damager);
    }
}
