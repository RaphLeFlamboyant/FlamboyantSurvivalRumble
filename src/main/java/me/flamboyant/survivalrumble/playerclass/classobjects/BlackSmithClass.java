package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.Common;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;

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
    public PlayerClassType getClassType() {
        return PlayerClassType.BLACKSMITH;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        CraftItemEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(owner.getUniqueId())) return;
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(event.getWhoClicked().getLocation());
        if (!concernedTeamName.equals(data().getPlayerTeam(owner))) return;
        if (!scoreByMaterial.containsKey(event.getInventory().getResult().getType())) return;

        int score = scoreByMaterial.get(event.getInventory().getResult().getType());
        GameManager.getInstance().addAddMoney(concernedTeamName, score);
    }
}
