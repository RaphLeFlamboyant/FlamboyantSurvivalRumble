package flamboyant.survivalrumble.playerclass.classobjects;

import flamboyant.survivalrumble.data.PlayerClassType;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class HunterClass extends APlayerClass implements Listener {
    private List<EntityType> tier1EntityTypes = Arrays.asList(EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.PANDA, EntityType.BAT, EntityType.BEE, EntityType.CAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FOX, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.MULE, EntityType.OCELOT, EntityType.MUSHROOM_COW, EntityType.PARROT, EntityType.PIG, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SNOWMAN, EntityType.SQUID, EntityType.STRIDER, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.WOLF, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE);
    private List<EntityType> tier2EntityTypes = Arrays.asList(EntityType.WITCH, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);

    public HunterClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.HUNTER;
    }

    @Override
    public void gameStarted(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller().getUniqueId() != owner.getUniqueId()) return;
        boolean isTier1 = tier1EntityTypes.contains(event.getEntity().getType());
        if (!isTier1 && !tier2EntityTypes.contains(event.getEntity().getType())) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        changeScore(ownerTeamName, isTier1 ? 1 : 2);
    }
}
