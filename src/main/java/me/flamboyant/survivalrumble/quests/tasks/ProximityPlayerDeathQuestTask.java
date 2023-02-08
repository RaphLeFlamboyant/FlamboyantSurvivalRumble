package me.flamboyant.survivalrumble.quests.tasks;

import me.flamboyant.survivalrumble.quests.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class ProximityPlayerDeathQuestTask extends APlayerDeathQuestTask {
    private List<EntityDamageEvent.DamageCause> deathCauses;
    private int distance;
    private int quantity;
    private List<Player> validTarget = null;

    public ProximityPlayerDeathQuestTask(Quest ownerQuest, List<EntityDamageEvent.DamageCause> deathCauses, int distance, int quantity) {
        super(ownerQuest);

        this.deathCauses = deathCauses;
        this.distance = distance;
        this.quantity = quantity;


        subQuestMessage = quantity + "fois, des joueurs doivent mourir é moins de " + distance + " blocs de vous par les moyens suivants : [";
        for (EntityDamageEvent.DamageCause cause : deathCauses) {
            subQuestMessage += cause + ", ";
        }
        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2) + "]";
    }

    public ProximityPlayerDeathQuestTask(Quest ownerQuest, List<EntityDamageEvent.DamageCause> deathCauses, int distance, int quantity, List<Player> validTarget) {
        super(ownerQuest);

        this.deathCauses = deathCauses;
        this.distance = distance;
        this.quantity = quantity;
        this.validTarget = validTarget;

        subQuestMessage = quantity + " fois, les joueurs suivants doivent mourir é moins de " + distance + " blocs de vous :";
        for (Player target : validTarget) {
            subQuestMessage += "\n    -> " + target.getDisplayName();
        }
        subQuestMessage += "\n  Causes du décés pour valier la mission : [";

        for (EntityDamageEvent.DamageCause cause : deathCauses) {
            subQuestMessage += cause + ", ";
        }

        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2) + "]";
    }

    public ProximityPlayerDeathQuestTask(Quest ownerQuest, int distance, int quantity, List<Player> validTarget) {
        super(ownerQuest);

        this.distance = distance;
        this.quantity = quantity;
        this.validTarget = validTarget;

        subQuestMessage = quantity + " fois, les joueurs suivants doivent mourir é moins de " + distance + " blocs de vous :";
        for (Player target : validTarget) {
            subQuestMessage += "\n    -> " + target.getDisplayName();
        }

        subQuestMessage = subQuestMessage.substring(0, subQuestMessage.length() - 2) + "]";
    }

    @Override
    protected void doOnEvent(PlayerDeathEvent event) {
        if (deathCauses.size() > 0 && !deathCauses.contains(event.getEntity().getLastDamageCause().getCause())) return;
        if (validTarget != null && !validTarget.contains(event.getEntity())) return;
        Location deadLocation = event.getEntity().getLocation();
        if (deadLocation.getWorld() != player.getWorld() || distance <= deadLocation.distance(player.getLocation())) return;

        if (--quantity <= 0) {
            stopQuest(true);
        }
    }
}
