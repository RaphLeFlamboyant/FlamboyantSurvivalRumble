package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AntiquarianClass extends APlayerClass implements Listener {
    private HashSet<Material> collectedItems = new HashSet<>();

    private Map<Material, Integer> scoreByMaterial = new HashMap<Material, Integer>() {{
        put(Material.HEART_OF_THE_SEA, 250);
        put(Material.WITHER_SKELETON_SKULL, 1000);
        put(Material.SPONGE, 250);
        put(Material.ANCIENT_DEBRIS, 300);
        put(Material.BELL, 50);
        put(Material.CRYING_OBSIDIAN, 150);
        put(Material.ELYTRA, 500);
        put(Material.DRAGON_HEAD, 500);
        put(Material.DRAGON_EGG, 500);
        put(Material.NETHER_STAR, 1000);
        put(Material.LEATHER_HORSE_ARMOR, 50);
        put(Material.IRON_HORSE_ARMOR, 100);
        put(Material.GOLDEN_HORSE_ARMOR, 150);
        put(Material.DIAMOND_HORSE_ARMOR, 200);
        put(Material.MUSIC_DISC_11, 25);
        put(Material.MUSIC_DISC_13, 25);
        put(Material.MUSIC_DISC_BLOCKS, 25);
        put(Material.MUSIC_DISC_CAT, 25);
        put(Material.MUSIC_DISC_CHIRP, 25);
        put(Material.MUSIC_DISC_FAR, 25);
        put(Material.MUSIC_DISC_MALL, 25);
        put(Material.MUSIC_DISC_MELLOHI, 25);
        put(Material.MUSIC_DISC_PIGSTEP, 25);
        put(Material.MUSIC_DISC_STAL, 25);
        put(Material.MUSIC_DISC_STRAD, 25);
        put(Material.MUSIC_DISC_WAIT, 25);
        put(Material.MUSIC_DISC_WARD, 25);
        put(Material.NAME_TAG, 75);
        put(Material.NAUTILUS_SHELL, 50);
        put(Material.SADDLE, 150);
        put(Material.SCUTE, 300);
        put(Material.TRIDENT, 750);
        put(Material.TOTEM_OF_UNDYING, 250);
        put(Material.LEAD, 50);
    }};


    public AntiquarianClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ANTIQUARIAN;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!event.getEntity().getUniqueId().equals(owner.getUniqueId())) return;
        Material material = event.getItem().getItemStack().getType();
        if (!scoreByMaterial.containsKey(material)) return;
        if (collectedItems.contains(material)) return;

        int score = scoreByMaterial.get(material);
        changeScore(data().playersTeam.get(owner.getUniqueId()), score);

        collectedItems.add(material);
    }
}
