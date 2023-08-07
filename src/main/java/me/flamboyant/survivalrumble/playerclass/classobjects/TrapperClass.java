package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class TrapperClass extends APlayerClass {
    public TrapperClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Un ennemi doit mourir de piége é moins de 100 blocs de distance de toi";
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.TRAPPER;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        EntityDamageEvent.DamageCause deathCause = killed.getLastDamageCause().getCause();
        // killer means no trap
        if (killer != null) return;
        // Mob & co is not a valid death, we don't want trapper to earn free points
        if (deathCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        // the trapper is too far away
        if (owner.getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 100) return;
        // If an ally comes to put lava bucket on ennemy for example
        String ownerTeam = data().getPlayerTeam(owner);
        for (Player player : data().getPlayers(ownerTeam)) {
            if (player != owner && player.getWorld() == killed.getWorld() && player.getLocation().distance(killed.getLocation()) < 48)
                return;
        }
        // the dead is in the trapper team
        if (data().getPlayerTeam(killed).equals(ownerTeam)) return;

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), 400);
    }
}
