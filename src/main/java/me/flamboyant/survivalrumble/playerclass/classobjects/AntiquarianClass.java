package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.BaseBlocPlaceQuest;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AntiquarianClass extends APlayerClass implements Listener {
    private int pointsByRarity = 25;

    private HashSet<Material> collectedItems = new HashSet<>();
    private Map<Material, Integer> scoreByMaterial = new HashMap<Material, Integer>() {{
        put(Material.HEART_OF_THE_SEA, pointsByRarity * 10);
        put(Material.WITHER_SKELETON_SKULL, pointsByRarity * 40);
        put(Material.SPONGE, pointsByRarity * 10);
        put(Material.ANCIENT_DEBRIS, pointsByRarity * 12);
        put(Material.BELL, pointsByRarity * 2);
        put(Material.CRYING_OBSIDIAN, pointsByRarity * 6);
        put(Material.ELYTRA, pointsByRarity * 20);
        put(Material.DRAGON_HEAD, pointsByRarity * 20);
        put(Material.DRAGON_EGG, pointsByRarity * 20);
        put(Material.NETHER_STAR, pointsByRarity * 40);
        put(Material.LEATHER_HORSE_ARMOR, pointsByRarity * 2);
        put(Material.IRON_HORSE_ARMOR, pointsByRarity * 4);
        put(Material.GOLDEN_HORSE_ARMOR, pointsByRarity * 6);
        put(Material.DIAMOND_HORSE_ARMOR, pointsByRarity * 8);
        put(Material.MUSIC_DISC_11, pointsByRarity * 2);
        put(Material.MUSIC_DISC_13, pointsByRarity * 2);
        put(Material.MUSIC_DISC_BLOCKS, pointsByRarity * 2);
        put(Material.MUSIC_DISC_CAT, pointsByRarity * 2);
        put(Material.MUSIC_DISC_CHIRP, pointsByRarity * 2);
        put(Material.MUSIC_DISC_FAR, pointsByRarity * 2);
        put(Material.MUSIC_DISC_MALL, pointsByRarity * 2);
        put(Material.MUSIC_DISC_MELLOHI, pointsByRarity * 2);
        put(Material.MUSIC_DISC_PIGSTEP, pointsByRarity * 2);
        put(Material.MUSIC_DISC_STAL, pointsByRarity * 2);
        put(Material.MUSIC_DISC_STRAD, pointsByRarity * 2);
        put(Material.MUSIC_DISC_WAIT, pointsByRarity * 2);
        put(Material.MUSIC_DISC_WARD, pointsByRarity * 2);
        put(Material.NAME_TAG, pointsByRarity * 3);
        put(Material.NAUTILUS_SHELL, pointsByRarity * 2);
        put(Material.SADDLE, pointsByRarity * 4);
        put(Material.SCUTE, pointsByRarity * 12);
        put(Material.TRIDENT, pointsByRarity * 30);
        put(Material.TOTEM_OF_UNDYING, pointsByRarity * 15);
        put(Material.LEAD, pointsByRarity * 2);
    }};


    public AntiquarianClass(Player owner) {
        super(owner);

        scoringDescription = "Obtenir des objets rares";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Traumatisme du combat", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_SWORD, 1),
                new TargetPlayerDeathByMobQuestComponent(owner, owner, 1, Arrays.asList(EntityType.ZOMBIE)))));
        questList.add(new Quest(this, owner, "Premiére propriété", null, 10, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_SHOVEL, 1))));
        questList.add(new Quest(this, owner, "L'amour de l'ancien", null, 30, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.BONE, 1))));
        questList.add(new Quest(this, owner, "Se salir les mains", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.STONE_PICKAXE, 1),
                new BreakBlocQuestComponent(owner, Material.GRANITE, 64))));
        questList.add(new Quest(this, owner, "Aimer le beau", null, 20, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.POLISHED_GRANITE, 64))));
        HashMap<Material, Integer> blocsToPlace = new HashMap<>();
        blocsToPlace.put(Material.POLISHED_GRANITE, 64);
        questList.add(new BaseBlocPlaceQuest(this, owner, "Une maison de luxe", "Place 64 blocs de Granite dans ta base", blocsToPlace, 76, 0));
        questList.add(new Quest(this, owner, "Découvrir la rareté", null, 200, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.DIAMOND, 9),
                new CraftQuestComponent(owner, Material.DIAMOND_BLOCK, 1))));
        questList.add(new Quest(this, owner, "Le moderne c'est naze", null, 200, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.DIAMOND_BLOCK, 1),
                new CraftQuestComponent(owner, Material.PAINTING, 10))));
        questList.add(new Quest(this, owner, "Se la péter", null, 25, 0, Arrays.asList(
                new HangingPlaceQuestComponent(owner, EntityType.PAINTING, 10))));
        questList.add(new Quest(this, owner, "Les ficelles du métier", null, 250, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.STICK, 640),
                new VillagerTradeQuestComponent(owner, Material.STICK, 640, true))));
        questList.add(new Quest(this, owner, "Obtenir l'impossible", null, 1000, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.EXPERIENCE_BOTTLE, 1, false))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ANTIQUARIAN;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!event.getEntity().getUniqueId().equals(owner.getUniqueId())) return;
        Material material = event.getItem().getItemStack().getType();
        if (!scoreByMaterial.containsKey(material)) return;
        if (collectedItems.contains(material)) return;

        int score = scoreByMaterial.get(material);
        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), score, ScoreType.FLAT);

        collectedItems.add(material);
    }
}
