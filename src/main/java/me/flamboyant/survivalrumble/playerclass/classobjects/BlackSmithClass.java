package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BlackSmithClass extends APlayerClass implements Listener {
    private int leatherBasePoints = 2;
    private int ironBasePoints = 2;
    private int goldBasePoints = 3;
    private int diamondBasePoints = 6;

    private Map<Material, Integer> scoreByMaterial = new HashMap<Material, Integer>() {{
        put(Material.LEATHER_CHESTPLATE, 8 * leatherBasePoints);
        put(Material.LEATHER_BOOTS, 4 * leatherBasePoints);
        put(Material.LEATHER_HELMET, 5 * leatherBasePoints);
        put(Material.LEATHER_LEGGINGS, 7 * leatherBasePoints);
        put(Material.IRON_CHESTPLATE, 8 * ironBasePoints);
        put(Material.IRON_BOOTS, 4 * ironBasePoints);
        put(Material.IRON_HELMET, 5 * ironBasePoints);
        put(Material.IRON_LEGGINGS, 7 * ironBasePoints);
        put(Material.IRON_SWORD, 2 * ironBasePoints);
        put(Material.IRON_PICKAXE, 3 * ironBasePoints);
        put(Material.IRON_AXE, 3 * ironBasePoints);
        put(Material.IRON_SHOVEL, 1 * ironBasePoints);
        put(Material.IRON_HOE, 2 * ironBasePoints);
        put(Material.SHIELD, 1 * ironBasePoints);
        put(Material.GOLDEN_CHESTPLATE, 8 * goldBasePoints);
        put(Material.GOLDEN_BOOTS, 4 * goldBasePoints);
        put(Material.GOLDEN_HELMET, 5 * goldBasePoints);
        put(Material.GOLDEN_LEGGINGS, 7 * goldBasePoints);
        put(Material.GOLDEN_SWORD, 2 * goldBasePoints);
        put(Material.GOLDEN_PICKAXE, 3 * goldBasePoints);
        put(Material.GOLDEN_AXE, 3 * goldBasePoints);
        put(Material.GOLDEN_SHOVEL, 1 * goldBasePoints);
        put(Material.GOLDEN_HOE, 2 * goldBasePoints);
        put(Material.DIAMOND_CHESTPLATE, 8 * diamondBasePoints);
        put(Material.DIAMOND_BOOTS, 4 * diamondBasePoints);
        put(Material.DIAMOND_HELMET, 5 * diamondBasePoints);
        put(Material.DIAMOND_LEGGINGS, 7 * diamondBasePoints);
        put(Material.DIAMOND_SWORD, 2 * diamondBasePoints);
        put(Material.DIAMOND_PICKAXE, 3 * diamondBasePoints);
        put(Material.DIAMOND_AXE, 3 * diamondBasePoints);
        put(Material.DIAMOND_SHOVEL, 1 * diamondBasePoints);
        put(Material.DIAMOND_HOE, 2 * diamondBasePoints);
    }};

    public BlackSmithClass(Player owner) {
        super(owner);

        scoringDescription = "Fabriquer des piéces d'équipement";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Faire le gros dur", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.STONE_SWORD, 4))));
        questList.add(new Quest(this, owner, "établir la forge", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.COBBLESTONE_WALL, 64),
                new PlaceBlocQuestComponent(owner, Material.COBBLESTONE_WALL, 64, true),
                new CraftQuestComponent(owner, Material.COBBLESTONE_SLAB, 64),
                new PlaceBlocQuestComponent(owner, Material.COBBLESTONE_SLAB, 64, true),
                new CraftQuestComponent(owner, Material.FURNACE, 8),
                new PlaceBlocQuestComponent(owner, Material.FURNACE, 8, true),
                new CraftQuestComponent(owner, Material.CRAFTING_TABLE, 1),
                new PlaceBlocQuestComponent(owner, Material.CRAFTING_TABLE, 1, true))));
        questList.add(new Quest(this, owner, "Collection secréte", null, 50, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.POPPY, 1),
                new GetItemQuestComponent(owner, Material.DANDELION, 1),
                new GetItemQuestComponent(owner, Material.ROSE_BUSH, 1),
                new GetItemQuestComponent(owner, Material.CORNFLOWER, 1))));
        questList.add(new Quest(this, owner, "Fondre le métal", null, 300, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.IRON_INGOT, 100))));
        questList.add(new Quest(this, owner, "Améliorer la forge", null, 200, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.BLAST_FURNACE, 8),
                new PlaceBlocQuestComponent(owner, Material.BLAST_FURNACE, 8, true),
                new CraftQuestComponent(owner, Material.ANVIL, 1),
                new PlaceBlocQuestComponent(owner, Material.ANVIL, 1, true),
                new CraftQuestComponent(owner, Material.SMITHING_TABLE, 1),
                new PlaceBlocQuestComponent(owner, Material.SMITHING_TABLE, 1, true))));
        questList.add(new Quest(this, owner, "Se sentir chez soi", null, 75, 0, Arrays.asList(
                new PlacePotQuestComponent(owner, Material.POPPY, 1, true),
                new PlacePotQuestComponent(owner, Material.DANDELION, 1, true),
                new PlacePotQuestComponent(owner, Material.CORNFLOWER, 1, true))));
        questList.add(new Quest(this, owner, "Embellir les lieux", null, 40, 0, Arrays.asList(
                new FertilizeQuestComponent(owner, 16, true))));
        questList.add(new Quest(this, owner, "Assumer ses fétiches", null, 75, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_BARS, 64),
                new PlaceBlocQuestComponent(owner, Material.IRON_BARS, 64, true),
                new CraftQuestComponent(owner, Material.CHAIN, 16),
                new PlaceBlocQuestComponent(owner, Material.CHAIN, 16, true))));
        questList.add(new Quest(this, owner, "Changer de style", null, 250, 0, Arrays.asList(
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_LEGGINGS, DyeColor.PINK, "PINK", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_BOOTS, DyeColor.PINK, "PINK", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_CHESTPLATE, DyeColor.PINK, "PINK", 1),
                new CraftColoredLeatherQuestComponent(owner, Material.LEATHER_HELMET, DyeColor.PINK, "PINK", 1))));
        questList.add(new Quest(this, owner, "Féter nos différences", null, 500, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.FIREWORK_ROCKET, 64))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BLACKSMITH;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(owner.getUniqueId())) return;
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(event.getWhoClicked().getLocation());
        if (!concernedTeamName.equals(data().playersTeam.get(owner.getUniqueId()))) return;
        if (!scoreByMaterial.containsKey(event.getInventory().getResult().getType())) return;

        int score = scoreByMaterial.get(event.getInventory().getResult().getType());
        GameManager.getInstance().addScore(concernedTeamName, score, ScoreType.FLAT);
    }
}
