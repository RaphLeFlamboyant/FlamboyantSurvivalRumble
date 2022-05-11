package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.ElectricianClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ElectricianClass extends APlayerClass {
    // TODO : ajouter une écoute de mouvements par piston pour maj la liste de location (via BlockPistonExtendEvent par ex)
    private HashMap<Material, Integer> scoreByBlockType = new HashMap<Material, Integer>() {{
        put(Material.COAL_BLOCK, 1);
        put(Material.IRON_BLOCK, 5);
        put(Material.GOLD_BLOCK, 5);
        put(Material.REDSTONE_BLOCK, 5);
        put(Material.LAPIS_BLOCK, 15);
        put(Material.EMERALD_BLOCK, 15);
        put(Material.DIAMOND_BLOCK, 30);
        put(Material.NETHERITE_BLOCK, 50);
    }};

    private HashMap<Material, Float> scoreLossByBrokenBlockType = new HashMap<Material, Float>() {{
        put(Material.COAL_BLOCK, 1f);
        put(Material.IRON_BLOCK, 0.9f);
        put(Material.GOLD_BLOCK, 0.8f);
        put(Material.REDSTONE_BLOCK, 1f);
        put(Material.LAPIS_BLOCK, 1f);
        put(Material.EMERALD_BLOCK, 0.5f);
        put(Material.DIAMOND_BLOCK, 0.9f);
        put(Material.NETHERITE_BLOCK, 0.9f);
    }};

    private ElectricianClassData classData;

    private int checkInterval = 5;
    private float total = 0f;

    public ElectricianClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);

        scoringDescription = "Poser, exposés au ciel, des blocs de ressource issus de minerais (fer, redstone, diamant, etc)";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Innovation technique", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.LEVER, 16))));
        questList.add(new Quest(this, owner, "Récupérer de la matiére", null, 50, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.SPIDER, 8),
                new BreakBlocQuestComponent(owner, Material.REDSTONE_ORE, 16))));
        questList.add(new Quest(this, owner, "De nouveaux instruments", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.PISTON, 16),
                new CraftQuestComponent(owner, Material.OAK_TRAPDOOR, 16))));

        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        List<Player> ennemies = new ArrayList<>();
        if (otherTeams.size() > 0) {
            ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Ingénieuse mort", null, 250, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, Arrays.asList(EntityDamageEvent.DamageCause.FALL, EntityDamageEvent.DamageCause.SUFFOCATION), 25, 1, ennemies))));
        }
        questList.add(new Quest(this, owner, "D'autres matériaux", null, 30, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.GLOWSTONE, 16),
                new BreakBlocQuestComponent(owner, Material.NETHER_QUARTZ_ORE, 32),
                new BreakBlocQuestComponent(owner, Material.NETHER_GOLD_ORE, 32))));
        questList.add(new Quest(this, owner, "Un systéme d'alerte", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.REDSTONE_LAMP, 8),
                new CraftQuestComponent(owner, Material.OBSERVER, 8),
                new CraftQuestComponent(owner, Material.NOTE_BLOCK, 8),
                new CraftQuestComponent(owner, Material.TRIPWIRE_HOOK, 8))));

        if (otherTeams.size() > 0) {
            int averageFoesByTeamNumber = ennemies.size() / (data().teams.size() - 1);
            questList.add(new Quest(this, owner, "Faire échouer leur invasion", null, averageFoesByTeamNumber * 250, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner,
                            Arrays.asList(EntityDamageEvent.DamageCause.FALL,
                                    EntityDamageEvent.DamageCause.PROJECTILE,
                                    EntityDamageEvent.DamageCause.THORNS,
                                    EntityDamageEvent.DamageCause.SUICIDE,
                                    EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                                    EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
                                    EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                                    EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                                    EntityDamageEvent.DamageCause.LAVA),
                            50,
                            averageFoesByTeamNumber,
                            ennemies))));
        }
        questList.add(new Quest(this, owner, "De nouveaux jouets", null, 150, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.RAIL, 128),
                new CraftQuestComponent(owner, Material.DETECTOR_RAIL, 4),
                new CraftQuestComponent(owner, Material.POWERED_RAIL, 16),
                new CraftQuestComponent(owner, Material.ACTIVATOR_RAIL, 4),
                new CraftQuestComponent(owner, Material.MINECART, 2))));
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Oups j'ai créé le TGV", null, 50, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 100, 2 * 60, true))));
        }
        questList.add(new Quest(this, owner, "Bon c'est nul en fait", null, 500, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.LEVER, 16),
                new DestroyItemQuestComponent(owner, Material.PISTON, 16),
                new DestroyItemQuestComponent(owner, Material.OAK_TRAPDOOR, 16),
                new DestroyItemQuestComponent(owner, Material.REDSTONE_LAMP, 8),
                new DestroyItemQuestComponent(owner, Material.OBSERVER, 8),
                new DestroyItemQuestComponent(owner, Material.NOTE_BLOCK, 8),
                new DestroyItemQuestComponent(owner, Material.TRIPWIRE_HOOK, 8),
                new DestroyItemQuestComponent(owner, Material.RAIL, 128),
                new DestroyItemQuestComponent(owner, Material.DETECTOR_RAIL, 4),
                new DestroyItemQuestComponent(owner, Material.POWERED_RAIL, 16),
                new DestroyItemQuestComponent(owner, Material.ACTIVATOR_RAIL, 4),
                new DestroyItemQuestComponent(owner, Material.MINECART, 2))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELECTRICIAN;
    }

    @Override
    public PlayerClassData buildClassData() { return new ElectricianClassData(); }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (ElectricianClassData) data().playerClassDataList.get(getClassType());
        Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> updateScoring(), 0l, checkInterval * 20l);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;
        if (location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) != location.getBlockY())
            return;

        handleBlockLocation(classData.blockLocationList, block, broken);
    }

    private void handleBlockLocation(HashMap<Location, Float> blockLocationAndScore, Block block, boolean broken) {
        if (!broken) {
            blockLocationAndScore.put(block.getLocation(), 0f);
            return;
        }

        Location loc = block.getLocation();
        for (Location existingLocation : blockLocationAndScore.keySet()) {
            if (existingLocation.equals(loc)) {
                float score = blockLocationAndScore.get(loc);
                blockLocationAndScore.remove(loc);
                int lastTotal = (int) total;
                total -= score * scoreLossByBrokenBlockType.get(block.getType());
                GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), -(lastTotal - (int) total), ScoreType.FLAT);
                return;
            }
        }
    }

    private void updateScoring() {
        int lastTotal = (int) total;

        for (Location loc : classData.blockLocationList.keySet()) {
            if (loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) == loc.getBlockY()) {
                float score = classData.blockLocationList.get(loc);
                float earned = scoreByBlockType.get(loc.getBlock().getType()) * checkInterval / 60f; // coef is score by minute
                score += earned;
                total += earned;
                classData.blockLocationList.put(loc, score);
            }
        }

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), ((int) total - lastTotal), ScoreType.FLAT);
    }
}
