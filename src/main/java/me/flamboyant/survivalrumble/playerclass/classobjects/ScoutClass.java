package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ScoutClass extends APlayerClass {
    private static final float scoreRatioInMalus = 0.05f;
    private static final int minDistDefault = 101;
    private String ownerTeam;
    private int checkInterval = 1;
    private float leftovers = 0f;
    private HashMap<Integer, Float> scoreBySeconds = new HashMap<Integer, Float>() {{
        put(4, 20f);
        put(10, 5f);
        put(20, 2.5f);
        put(30, 1.7f);
        put(50, 1f);
        put(60, 0.6f);
        put(80, 0.4f);
        put(100, 0.2f);
    }};

    public ScoutClass(Player owner) {
        super(owner);

        ownerTeam = data().playersTeam.get(owner.getUniqueId());
        scoringDescription = "Approche toi autant que possible du centre de la base adverse";
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        questList.add(new Quest(this, owner, "L'instrument principal", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.TORCH, 16))));
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "S'habituer é gambader", null, 100, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 150, 5 * 60, true))));
        }
        questList.add(new Quest(this, owner, "S'entrainer en milieu hostile", null, 150, 0, Arrays.asList(
                new BiomeVentureQuestComponent(owner, Arrays.asList(Biome.NETHER_WASTES, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.BASALT_DELTAS, Biome.SOUL_SAND_VALLEY), 5 * 60))));
        questList.add(new Quest(this, owner, "Un équipement discret", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.LEATHER_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.LEATHER_BOOTS, 1),
                new CraftQuestComponent(owner, Material.LEATHER_HELMET, 1),
                new CraftQuestComponent(owner, Material.LEATHER_CHESTPLATE, 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_LEGGINGS, DyeColor.GRAY, "Vert", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_BOOTS, DyeColor.GRAY, "Vert", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_HELMET, DyeColor.GRAY, "Vert", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_CHESTPLATE, DyeColor.GRAY, "Vert", 1))));
        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        List<Player> ennemies = new ArrayList<>();
        if (otherTeams.size() > 0) {
            ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Prés de ses ennemis ...", null, 500, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, ennemies, 30, 5 * 60, false))));
            questList.add(new Quest(this, owner, "Saboter", null, 75, 0, Arrays.asList(
                    new HQExplosionQuestComponent(owner, 1, otherTeams))));
        }
        questList.add(new Quest(this, owner, "Revenir faire un rapport", null, 100, 0, Arrays.asList(
                new HQProximityQuestComponent(owner, 1 * 60, 25, true, ownerTeam))));
        questList.add(new Quest(this, owner, "Pause pipi", null, 10, 0, Arrays.asList(
                new FertilizeQuestComponent(owner, 1, true))));
        questList.add(new Quest(this, owner, "Préparer l'assaut", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.BOW, 1),
                new CraftQuestComponent(owner, Material.ARROW, 16),
                new CraftQuestComponent(owner, Material.SHIELD, 1))));
        if (otherTeams.size() > 0) {
            String targetTeam = otherTeams.get((new Random()).nextInt(otherTeams.size()));
            List<UUID> targetTeamPlayerIds = data().playersByTeam.get(targetTeam);
            List<Player> targetTeamPlayers = ennemies.stream().filter(p -> targetTeamPlayerIds.contains(p.getUniqueId())).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Participer é la guerre", null, 1500, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 10 * 60, 35, true, targetTeam),
                    new ProximityPlayerDeathQuestComponent(owner, 35, 8, targetTeamPlayers))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.SCOUT;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    private void updateScoring() {
        if (!owner.getWorld().getName().equals("world")) return;
        String closestTeamHQ = getCloserValidHeadQuarter();
        if (closestTeamHQ == null) return;

        Boolean isAnyFoeClose = false;
        for (UUID playerId : data().playersByTeam.get(closestTeamHQ)) {
            Player player = Common.server.getPlayer(playerId);
            isAnyFoeClose |= ((player.getWorld() == owner.getWorld()) && player.getLocation().distance(owner.getLocation()) < minDistDefault);
        }

        GameManager.getInstance().addScore(ownerTeam, getScoring((int) owner.getLocation().distance(data().teamHeadquarterLocation.get(closestTeamHQ)), !isAnyFoeClose), ScoreType.FLAT);
    }

    private int getScoring(int distToHqCenter, boolean isMalusApplied) {
        float score = 0f;
        for (Integer dist : scoreBySeconds.keySet()) {
            if (distToHqCenter <= dist) {
                score = scoreBySeconds.get(dist);
                break;
            }
        }

        if (isMalusApplied) score *= scoreRatioInMalus;
        score += leftovers;
        leftovers = score % 1f;

        return (int) score;
    }

    private String getCloserValidHeadQuarter() {
        double minDist = minDistDefault;
        String res = null;
        for (String teamName : data().teamHeadquarterLocation.keySet()) {
            if (ownerTeam.equals(teamName)) continue;

            Location hqLocation = data().teamHeadquarterLocation.get(teamName);
            double dist = hqLocation.distance(owner.getLocation());
            if (dist < minDist) {
                minDist = dist;
                res = teamName;
            }
        }

        return res;
    }
}
