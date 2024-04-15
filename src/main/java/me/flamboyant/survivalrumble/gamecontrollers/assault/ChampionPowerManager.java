package me.flamboyant.survivalrumble.gamecontrollers.assault;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.powers.ChampionPowerType;
import me.flamboyant.survivalrumble.powers.impl.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChampionPowerManager {
    private HashMap<Player, List<IChampionPower>> championToPowers = new HashMap<>();

    private ChampionPowerManager() {}

    private static ChampionPowerManager instance;
    public static ChampionPowerManager getInstance() {
        if (instance == null) {
            instance = new ChampionPowerManager();
        }

        return instance;
    }

    public void activateChampionsPowers() {
        var data = SurvivalRumbleData.getSingleton();

        for (var teamName : data.getTeams()) {
            var player = data.getTeamChampion(teamName);
            var powers = new ArrayList<IChampionPower>();

            for (var powerType : ChampionPowerType.values()) {
                var powerLevel = data.getChampionPowerTypeLevel(teamName, powerType);

                if (powerLevel > 0) {
                    var power = powerTypeToPower.get(powerType).createPower();
                    power.activate(player, powerLevel);
                    powers.add(power);
                }
            }

            championToPowers.put(player, powers);
        }
    }

    public void deactivateChampionPowers(Player player) {
        for (var power : championToPowers.get(player)) {
            power.deactivate();
        }

        championToPowers.remove(player);
    }

    public void deactivateAllAndReset() {
        for (var player : championToPowers.keySet()) {
            for (var power : championToPowers.get(player)) {
                power.deactivate();
            }
        }

        championToPowers.clear();
    }

    private HashMap<ChampionPowerType, PowerCreator> powerTypeToPower = new HashMap<>() {{
        put(ChampionPowerType.EQUIPMENT, EquipmentPower::new);
        put(ChampionPowerType.ENDERCHEST_STUFF, EnderChestContentPower::new);
        put(ChampionPowerType.NO_SHIELD, ShieldRemovalPower::new);

        put(ChampionPowerType.EFFECT_STRENGH, StrengthEffectPower::new);
        put(ChampionPowerType.EFFECT_RESISTANCE, ResistanceEffectPower::new);
        put(ChampionPowerType.EFFECT_SPEED, SpeedEffectPower::new);
        put(ChampionPowerType.EFFECT_JUMP, JumpEffectPower::new);
        put(ChampionPowerType.EFFECT_FIRE_RESISTANCE, FireResistanceEffectPower::new);
        put(ChampionPowerType.EFFECT_WATER_BREATHING, WaterBreathingEffectPower::new);

        put(ChampionPowerType.MOB_FRIEND, FriendlyMobsPower::new);
        put(ChampionPowerType.ENEMIES_DETECTION, EnemiesDetectionPower::new);
        put(ChampionPowerType.NO_FALL_DAMAGE, NoFallDamagePower::new);

        put(ChampionPowerType.SPELL_SCREAM, CryOfDeadSpellPower::new);
        put(ChampionPowerType.SPELL_SUMMON, SummoningSpellPower::new);
        put(ChampionPowerType.SPELL_JUMP, HighJumpSpellPower::new);
        put(ChampionPowerType.SPELL_EXPLOSIVE_PUN, ExplodingPunSpellPower::new);
        put(ChampionPowerType.SPELL_JAIL, ObsidianTrapSpellPower::new);
        put(ChampionPowerType.SPELL_ADIOS, AdiosSpellPower::new);
        put(ChampionPowerType.SPELL_SHUFFLE, ShuffleSpellPower::new);

        put(ChampionPowerType.COME_BACK_REVIVE, ReviveComeBackPower::new);
        put(ChampionPowerType.COME_BACK_BALANCE, BalanceComeBackPower::new);
        put(ChampionPowerType.COME_BACK_JUMP_SCARE, JumpScareComeBackPower::new);
    }};

    @FunctionalInterface
    private interface PowerCreator {
        IChampionPower createPower();
    }
}
