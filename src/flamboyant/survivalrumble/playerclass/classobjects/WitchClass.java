package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import flamboyant.survivalrumble.utils.ScoreType;
import flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.List;

public class WitchClass extends APlayerClass implements Listener {
    private List<PotionEffectType> positiveEffects = Arrays.asList(PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.HEAL, PotionEffectType.HEALTH_BOOST, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.INVISIBILITY, PotionEffectType.JUMP, PotionEffectType.NIGHT_VISION, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.REGENERATION, PotionEffectType.SLOW_FALLING, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING);
    private List<PotionEffectType> negativeEffects = Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.HARM, PotionEffectType.SLOW);

    public WitchClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.WITCH;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if (event.getAffectedEntities().isEmpty()) return;
        ProjectileSource source = event.getEntity().getShooter();
        if (!(source instanceof Player)) return;
        Player shooter = (Player) source;
        if (!shooter.getUniqueId().equals(owner.getUniqueId())) return;
        int positivity = potionPositivity(event.getPotion());
        if (positivity == 0) return;


        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        int scoreDelta = 0;
        for (LivingEntity ety : event.getAffectedEntities()) {
            if (!data().playersTeam.containsKey(ety.getUniqueId())) continue;

            if (positivity > 0 && data().playersTeam.get(ety.getUniqueId()).equals(ownerTeamName))
                scoreDelta += 5;
            else if (positivity < 0 && !data().playersTeam.get(ety.getUniqueId()).equals(ownerTeamName))
                scoreDelta += 40;
        }

        ScoringHelper.addScore(ownerTeamName, scoreDelta, ScoreType.FLAT);
    }

    private int potionPositivity(ThrownPotion potion) {
        int positivity = 0;

        for (PotionEffect effect : potion.getEffects()) {
            if (positiveEffects.contains(effect.getType())) positivity++;
            else if (negativeEffects.contains(effect.getType())) positivity--;
        }

        return positivity;
    }
}
