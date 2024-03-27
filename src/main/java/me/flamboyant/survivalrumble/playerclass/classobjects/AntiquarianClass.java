package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.AntiquarianClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashMap;
import java.util.Map;

public class AntiquarianClass extends APlayerClass implements Listener {
    private int pointsByRarity = 25;
    private AntiquarianClassData classData;

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
    public PlayerClassType getClassType() {
        return PlayerClassType.ANTIQUARIAN;
    }

    @Override
    public PlayerClassData buildClassData() { return new AntiquarianClassData(); }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (AntiquarianClassData) data().getPlayerClassData(owner);
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        EntityPickupItemEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!event.getEntity().getUniqueId().equals(owner.getUniqueId())) return;
        Material material = event.getItem().getItemStack().getType();
        if (!scoreByMaterial.containsKey(material)) return;
        if (classData.collectedItems.contains(material)) return;

        int score = scoreByMaterial.get(material);
        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), score);

        classData.collectedItems.add(material);
    }
}
