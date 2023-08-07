package me.flamboyant.survivalrumble.quests.pools.impl;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.tasks.*;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassicQuestPool extends AQuestPoolBase {
    // Classe crado à automatiser en data oriented un jour peut etre

    @Override
    protected void buildQuestList() {
        List<AQuestTask> questTasks = new ArrayList<>();
        Quest quest = new Quest("Les basses besognes", 250);
        questTasks.add(new CraftQuestTask(quest, Material.BUCKET, 4));
        questTasks.add(new CraftQuestTask(quest, Material.SHIELD, 4));
        questTasks.add(new BreakBlocQuestTask(quest, Material.STONE, 64 * 4));
        questTasks.add(new GetItemQuestTask(quest, Material.COBBLESTONE, 64 * 4));
        questTasks.add(new BucketFillQuestTask(quest, Material.WATER, 4));
        questTasks.add(new FurnaceExtractQuestTask(quest, Material.COOKED_PORKCHOP, 64));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Préparer la mousson", 150);
        questTasks.add(new KillMobQuestTask(quest, EntityType.SKELETON, 16));
        questTasks.add(new GetItemQuestTask(quest, Material.BONE, 32));
        questTasks.add(new CraftQuestTask(quest, Material.BONE_MEAL, 32 * 3));
        questTasks.add(new BucketFillQuestTask(quest, Material.WATER, 2));
        questTasks.add(new FertilizeQuestTask(quest, 32 * 3, true));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Exil en enfer", 2500);
        questTasks.add(new BiomeVentureQuestTask(quest, Arrays.asList(Biome.NETHER_WASTES, Biome.BASALT_DELTAS, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.SOUL_SAND_VALLEY), 15 * 60));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Refroidir le magma", 250);
        questTasks.add(new AimOnBlockQuestTask(quest, EntityType.SNOWBALL, 16, Material.MAGMA_BLOCK));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Se la jouer Archer", 500);
        questTasks.add(new AimOnEntityQuestTask(quest, EntityType.ARROW, 4, EntityType.PLAYER, false));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Le Pêcheur fou", 100);
        questTasks.add(new HookMobQuestTask(quest, 1, Arrays.asList(EntityType.COW)));
        questTasks.add(new HookMobQuestTask(quest, 1, Arrays.asList(EntityType.SHEEP)));
        questTasks.add(new HookMobQuestTask(quest, 1, Arrays.asList(EntityType.CHICKEN)));
        questTasks.add(new HookMobQuestTask(quest, 1, Arrays.asList(EntityType.PIG)));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Décorer les environs", 150);
        questTasks.add(new CraftQuestTask(quest, Material.FLOWER_POT, 4));
        questTasks.add(new PlacePotQuestTask(quest, Material.POPPY, 4, true));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Les beaux arts", 200);
        questTasks.add(new HangingPlaceQuestTask(quest, EntityType.PAINTING, 6));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Shaun of the Dead", 100);
        questTasks.add(new KillMobQuestTask(quest, EntityType.ZOMBIE, 20));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Se la jouer Espion", 1000);
        questTasks.add(new HQProximityQuestTask(quest, 300, 35, true));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Le goût du risque", 1000);
        questTasks.add(new HQProximityQuestTask(quest, 30, 5, true));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Un pro de la négo", 1000);
        questTasks.add(new VillagerTradeQuestTask(quest, Material.EMERALD, 32, false));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Danger océanique", 2500);
        questTasks.add(new KillMobQuestTask(quest, EntityType.ELDER_GUARDIAN, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Aller à l'essentiel", 250);
        questTasks.add(new BreakBlocQuestTask(quest, Material.DEEPSLATE_DIAMOND_ORE, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Une blague risquée", 400);
        questTasks.add(new HookPlayersFromTeamQuestTask(quest, 1, true));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Le gâchis", 750);
        questTasks.add(new DestroyItemQuestTask(quest, Material.DIAMOND, 9));
        questTasks.add(new DestroyItemQuestTask(quest, Material.BOW, 3));
        questTasks.add(new DestroyItemQuestTask(quest, Material.SHIELD, 3));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Pour passer le temps", 10);
        questTasks.add(new DestroyItemQuestTask(quest, Material.STICK, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Pour passer le temps 2", 10);
        questTasks.add(new DestroyItemQuestTask(quest, Material.DIRT, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Pour passer le temps 3", 10);
        questTasks.add(new DestroyItemQuestTask(quest, Material.WHEAT_SEEDS, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("On a trop de charbon", 150);
        questTasks.add(new FurnaceExtractQuestTask(quest, Material.COPPER_INGOT, 128));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("On a beaucoup trop de charbon", 400);
        questTasks.add(new FurnaceExtractQuestTask(quest, Material.DRIED_KELP, 640));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Avoir l'air con devant un lac", 50);
        questTasks.add(new BucketFillQuestTask(quest, Material.WATER, 120));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Dans la peau d'un noob", 75);
        questTasks.add(new CraftQuestTask(quest, Material.CLOCK, 1));
        quest.setTasks(questTasks);
        questList.add(quest);

        questTasks = new ArrayList<>();
        quest = new Quest("Comme en UHC", 250);
        questTasks.add(new CraftQuestTask(quest, Material.GOLDEN_APPLE, 10));
        quest.setTasks(questTasks);
        questList.add(quest);
    }
}
