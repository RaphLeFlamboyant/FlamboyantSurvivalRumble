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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoorishClass extends APlayerClass {
    private Map<Material, Integer> pointsByOre = new HashMap<Material, Integer>() {{
        put(Material.COAL_ORE, 3);
        put(Material.REDSTONE_ORE, 4);
        put(Material.IRON_ORE, 2);
        put(Material.NETHER_GOLD_ORE, 4);
        put(Material.NETHER_QUARTZ_ORE, 4);
        put(Material.GILDED_BLACKSTONE, 3);
        put(Material.LAPIS_ORE, 7);
        put(Material.GOLD_ORE, 3);
        put(Material.EMERALD_ORE, 25);
        put(Material.DIAMOND_ORE, 20);
    }};

    public BoorishClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "Placer des blocs de minerais entre la couche 70 et la couche " + ScoringHelper.fullScoreMaxY;
    }

    @Override
    protected void buildQuestList() {
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());

        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Apprécier solitude", null, 25, 0, Arrays.asList(
                    new ProximityToPlayersQuestComponent(owner, allies, 50, 2 * 60, true))));
        }
        questList.add(new Quest(this, owner, "Crasse beau", null, 35, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.STONE_SHOVEL, 1),
                new BreakBlocQuestComponent(owner, Material.DIRT, 50),
                new BreakBlocQuestComponent(owner, Material.COAL_ORE, 5))));
        questList.add(new Quest(this, owner, "Pagne beau", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.LEATHER_LEGGINGS, 1))));
        questList.add(new Quest(this, owner, "Boue agréable", null, 100, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.CLAY_BALL, 200))));
        questList.add(new Quest(this, owner, "Construire caverne", null, 250, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.STONE, 128),
                new PlaceBlocQuestComponent(owner, Material.STONE, 128, true),
                new CraftQuestComponent(owner, Material.CAMPFIRE, 1),
                new PlaceBlocQuestComponent(owner, Material.CAMPFIRE, 1, true))));
        questList.add(new Quest(this, owner, "Armure homme", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.IRON_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.IRON_HELMET, 1),
                new CraftQuestComponent(owner, Material.IRON_BOOTS, 1))));
        questList.add(new Quest(this, owner, "Moi pas aimer", null, 150, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.IRON_CHESTPLATE, 1),
                new DestroyItemQuestComponent(owner, Material.IRON_LEGGINGS, 1),
                new DestroyItemQuestComponent(owner, Material.IRON_HELMET, 1),
                new DestroyItemQuestComponent(owner, Material.IRON_BOOTS, 1))));
        questList.add(new Quest(this, owner, "Armure solide", null, 200, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_HELMET, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_BOOTS, 1))));
        questList.add(new Quest(this, owner, "Moi pas aimer", null, 1500, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.DIAMOND_CHESTPLATE, 1),
                new DestroyItemQuestComponent(owner, Material.DIAMOND_LEGGINGS, 1),
                new DestroyItemQuestComponent(owner, Material.DIAMOND_HELMET, 1),
                new DestroyItemQuestComponent(owner, Material.DIAMOND_BOOTS, 1))));
        questList.add(new Quest(this, owner, "Fleurs jolies", null, 25, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.POPPY, 1),
                new GetItemQuestComponent(owner, Material.DANDELION, 1),
                new GetItemQuestComponent(owner, Material.ROSE_BUSH, 1),
                new GetItemQuestComponent(owner, Material.CORNFLOWER, 1))));
        questList.add(new Quest(this, owner, "Finalement moi pas aimer", null, 25, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.POPPY, 1),
                new DestroyItemQuestComponent(owner, Material.DANDELION, 1),
                new DestroyItemQuestComponent(owner, Material.ROSE_BUSH, 1),
                new DestroyItemQuestComponent(owner, Material.CORNFLOWER, 1))));
        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        if (otherTeams.size() > 0) {
            Player target = owner.getServer().getPlayer(TeamHelper.getRandomPlayer(otherTeams, 1).get(0));
            questList.add(new Quest(this, owner, "Besoin me défouler", null, 500, 0, Arrays.asList(
                    new KillPlayerQuestComponent(owner, target, 1))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BOORISH;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockBreak(block);
    }

    private void handleBlockBreak(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        int coef = broken ? -1 : 1;
        if (!pointsByOre.containsKey(block.getType())) return;
        Location location = block.getLocation();
        if (location.getBlockY() <= 70 || location.getBlockY() > ScoringHelper.fullScoreMaxY) return;
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (coef * (int) (pointsByOre.get(block.getType()) * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()))), ScoreType.REVERSIBLE);
    }
}
