package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.stream.Collectors;

public class PyromaniacClass extends APlayerClass {
    private static final int scoringCoef = 20;
    private static final int malusCoef = -16;

    private HashSet<Material> validBurnableBlocks = new HashSet<>(Arrays.asList(
            Material.OAK_LOG,
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_MANGROVE_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG,
            Material.ACACIA_WOOD,
            Material.BIRCH_WOOD,
            Material.DARK_OAK_WOOD,
            Material.JUNGLE_WOOD,
            Material.MANGROVE_WOOD,
            Material.OAK_WOOD,
            Material.SPRUCE_WOOD,
            Material.STRIPPED_ACACIA_WOOD,
            Material.STRIPPED_BIRCH_WOOD,
            Material.STRIPPED_DARK_OAK_WOOD,
            Material.STRIPPED_JUNGLE_WOOD,
            Material.STRIPPED_MANGROVE_WOOD,
            Material.STRIPPED_OAK_WOOD,
            Material.STRIPPED_SPRUCE_WOOD,
            Material.COAL_BLOCK,
            Material.OAK_PLANKS,
            Material.ACACIA_PLANKS,
            Material.BIRCH_PLANKS,
            Material.DARK_OAK_PLANKS,
            Material.JUNGLE_PLANKS,
            Material.MANGROVE_PLANKS,
            Material.SPRUCE_PLANKS,
            Material.OAK_SLAB,
            Material.ACACIA_SLAB,
            Material.BIRCH_SLAB,
            Material.DARK_OAK_SLAB,
            Material.JUNGLE_SLAB,
            Material.MANGROVE_SLAB,
            Material.SPRUCE_SLAB,
            Material.OAK_FENCE,
            Material.ACACIA_FENCE,
            Material.BIRCH_FENCE,
            Material.DARK_OAK_FENCE,
            Material.JUNGLE_FENCE,
            Material.MANGROVE_FENCE,
            Material.SPRUCE_FENCE,
            Material.OAK_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.MANGROVE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.OAK_STAIRS,
            Material.ACACIA_STAIRS,
            Material.BIRCH_STAIRS,
            Material.DARK_OAK_STAIRS,
            Material.JUNGLE_STAIRS,
            Material.MANGROVE_STAIRS,
            Material.SPRUCE_STAIRS,
            Material.OAK_LEAVES,
            Material.ACACIA_LEAVES,
            Material.BIRCH_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES,
            Material.AZALEA_LEAVES,
            Material.COMPOSTER,
            Material.BEEHIVE,
            Material.TARGET,
            Material.BOOKSHELF,
            Material.LECTERN,
            Material.BEE_NEST,
            Material.BLACK_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
            Material.GREEN_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.LIME_WOOL,
            Material.MAGENTA_WOOL,
            Material.ORANGE_WOOL,
            Material.PINK_WOOL,
            Material.PURPLE_WOOL,
            Material.RED_WOOL,
            Material.WHITE_WOOL,
            Material.YELLOW_WOOL,
            Material.BLACK_CARPET,
            Material.BLUE_CARPET,
            Material.BROWN_CARPET,
            Material.CYAN_CARPET,
            Material.GRAY_CARPET,
            Material.GREEN_CARPET,
            Material.LIGHT_BLUE_CARPET,
            Material.LIGHT_GRAY_CARPET,
            Material.LIME_CARPET,
            Material.MAGENTA_CARPET,
            Material.ORANGE_CARPET,
            Material.PINK_CARPET,
            Material.PURPLE_CARPET,
            Material.RED_CARPET,
            Material.WHITE_CARPET,
            Material.YELLOW_CARPET,
            Material.DRIED_KELP_BLOCK,
            Material.HAY_BLOCK,
            Material.SCAFFOLDING
    ));

    private HashSet<EntityDamageEvent.DamageCause> validDeathCauses = new HashSet<>(Arrays.asList(EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.FIRE));

    public PyromaniacClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);

        scoringDescription = "Bréler des blocs dans la base adverse";
    }

    @Override
    protected void buildQuestList() {
        List<EntityDamageEvent.DamageCause> pyroCause = Arrays.asList(EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.LAVA);
        Random rng = new Random();
        questList.add(new Quest(this, owner, "Une substance dangereuse", null, 15, 0, Arrays.asList(
                new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, Arrays.asList(EntityDamageEvent.DamageCause.SUFFOCATION)))));
        questList.add(new Quest(this, owner, "Détruire l'élément toxique", null, 50, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.WATER_BUCKET, 1))));
        questList.add(new Quest(this, owner, "Les outils du salut", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.FLINT_AND_STEEL, 1),
                new CraftQuestComponent(owner, Material.FIRE_CHARGE, 3),
                new BucketFillQuestComponent(owner, Material.LAVA, 1))));
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        if (data().players.size() > 1) {
            List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
            if (allies.size() > 0) {
                questList.add(new Quest(this, owner, "Sauver les étres vivants", null, 150, 0, Arrays.asList(
                        new KillMobQuestComponent(owner, EntityType.ZOMBIE, 1),
                        new KillMobQuestComponent(owner, EntityType.CREEPER, 1),
                        new TargetPlayerSpecificDeathQuestComponent(owner,
                                allies.get(rng.nextInt(allies.size())),
                                1, pyroCause))));
            }
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            List<UUID> enemiesIDfromSpecificTeam = data().playersByTeam.get(otherTeams.get(rng.nextInt(otherTeams.size())));
            questList.add(new Quest(this, owner, "Transmettre la bonne parole", null, 500 * enemiesIDfromSpecificTeam.size(), 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner,
                            pyroCause,
                            16,
                            enemiesIDfromSpecificTeam.size(),
                            enemiesIDfromSpecificTeam.stream().map(pid -> owner.getServer().getPlayer(pid)).collect(Collectors.toList())))));
            questList.add(new Quest(this, owner, "Multiplier les options", null, 100, 0, Arrays.asList(
                    new CraftQuestComponent(owner, Material.CROSSBOW, 1),
                    new CraftQuestComponent(owner, Material.FIREWORK_ROCKET, 16))));
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Les protéger malgré eux", null, 500, 0, Arrays.asList(
                    new FireworkExplodesOnPlayerQuestComponent(owner, ennemies.get(rng.nextInt(ennemies.size())), 3))));
            questList.add(new Quest(this, owner, "Un fontaine ? Mais vous étes fous !!", null, 75, 0, Arrays.asList(
                    new KillMobQuestComponent(owner, EntityType.VILLAGER, 3))));
            questList.add(new Quest(this, owner, "La terre sainte", null, 50, 0, Arrays.asList(
                    new BiomeVentureQuestComponent(owner, Biome.NETHER_WASTES, 5 * 60))));
            questList.add(new Quest(this, owner, "Une légére obsession", null, 200, 0, Arrays.asList(
                    new BucketFillQuestComponent(owner, Material.LAVA, 10))));

            List<AQuestComponent> components = new ArrayList<>();
            for (Player ennemy : ennemies) {
                components.add(new TargetPlayerSpecificDeathQuestComponent(owner, ennemy, 1, pyroCause));
            }
            for (Player ally : allies) {
                components.add(new TargetPlayerSpecificDeathQuestComponent(owner, ally, 1, pyroCause));
            }
            questList.add(new Quest(this, owner, "Les sauver TOUS", null, 300 * ennemies.size() + 50 * allies.size(), 0, components));
            questList.add(new Quest(this, owner, "S'acharner sur les impies", null, 10, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, pyroCause, 16, 20, ennemies))));
            questList.add(new Quest(this, owner, "Mériter le salut", null, 3000, 0, Arrays.asList(
                    new TargetPlayerSpecificDeathQuestComponent(owner, owner, 1, pyroCause))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PYROMANIAC;
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        if (!validBurnableBlocks.contains(block.getType())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (int) (scoringCoef * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        if (!validBurnableBlocks.contains(block.getType())) return;
        if (!data().playersTeam.get(playerWhoBreaks.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, (int) (malusCoef * ScoringHelper.scoreAltitudeCoefficient(block.getLocation().getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        EntityDamageEvent.DamageCause deathCause = killed.getLastDamageCause().getCause();
        // Mob & co is not a valid death, we don't want trapper to earn free points
        if (!validDeathCauses.contains(deathCause)) return;
        // the trapper is too far away
        if (owner.getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 100) return;
        // the dead is in the trapper team
        if (data().playersTeam.get(killed.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), 50, ScoreType.FLAT);
    }
}
