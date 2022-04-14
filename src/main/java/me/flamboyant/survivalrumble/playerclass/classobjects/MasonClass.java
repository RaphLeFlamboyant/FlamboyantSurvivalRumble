package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MasonClass extends APlayerClass {
    public MasonClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "Poser des briques rapporte le double de points";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Une bien jolie matiére", null, 25, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.CLAY, 8))));
        questList.add(new Quest(this, owner, "Comparer les résultats", null, 25, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.BRICK, 1),
                new FurnaceExtractQuestComponent(owner, Material.TERRACOTTA, 1))));
        questList.add(new Quest(this, owner, "Je préfére les briques", null, 100, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.BRICK, 128))));
        questList.add(new Quest(this, owner, "Oups", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.FLOWER_POT, 4))));
        questList.add(new Quest(this, owner, "Bon tant qu'é faire ...", null, 25, 0, Arrays.asList(
                new PlacePotQuestComponent(owner, Material.POPPY, 4, true))));
        questList.add(new Quest(this, owner, "D'autres briques ?", null, 200, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.NETHER_BRICKS, 32))));
        questList.add(new Quest(this, owner, "Tenter un autre style", null, 300, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.STONE_BRICKS, 64, true),
                new PlaceBlocQuestComponent(owner, Material.POLISHED_BLACKSTONE_BRICKS, 32, true))));
        questList.add(new Quest(this, owner, "Chercher un mélange osé", null, 1000, 0, Arrays.asList(
                new BiomeVentureQuestComponent(owner, Arrays.asList(Biome.NETHER_WASTES, Biome.WARPED_FOREST, Biome.CRIMSON_FOREST, Biome.BASALT_DELTAS, Biome.SOUL_SAND_VALLEY), 10 * 60),
                new CraftQuestComponent(owner, Material.RED_NETHER_BRICKS, 8))));
        questList.add(new Quest(this, owner, "Devoir fermer boutique ...", null, 200, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.FURNACE, 8),
                new DestroyItemQuestComponent(owner, Material.CLAY, 32))));

        if (data().teams.size() > 1) {
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            Player target = ennemies.get((new Random()).nextInt(ennemies.size()));
            questList.add(new Quest(this, owner, "S'acharner sur le concurrent", null, 750, 0, Arrays.asList(
                    new KillPlayerQuestComponent(owner, target, 2))));
        }
        questList.add(new Quest(this, owner, "Une toujours aussi jolie matiére", null, 25, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.CLAY, 8))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.MASON;
    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned) {
        if (Arrays.asList(Material.BRICKS, Material.BRICK_STAIRS, Material.BRICK_SLAB, Material.BRICK_WALL).contains(blockData.getMaterial())
                && teamConcerned.equals(data().playersTeam.get(owner.getUniqueId()))) {
            score *= 2;
        }

        return score;
    }
}
