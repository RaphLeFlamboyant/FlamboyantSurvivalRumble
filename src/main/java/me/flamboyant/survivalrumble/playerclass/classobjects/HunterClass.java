package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
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
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HunterClass extends APlayerClass implements Listener {
    private List<EntityType> tier1EntityTypes = Arrays.asList(EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.PANDA, EntityType.BAT, EntityType.BEE, EntityType.CAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FOX, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.MULE, EntityType.OCELOT, EntityType.MUSHROOM_COW, EntityType.PARROT, EntityType.PIG, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SNOWMAN, EntityType.SQUID, EntityType.STRIDER, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.WOLF, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE);
    private List<EntityType> tier2EntityTypes = Arrays.asList(EntityType.WITCH, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);

    public HunterClass(Player owner) {
        super(owner);

        scoringDescription = "Tuer des mobs";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Premier arsenal", null, 15, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_SWORD, 1),
                new CraftQuestComponent(owner, Material.WOODEN_AXE, 1))));
        questList.add(new Quest(this, owner, "Une arme indispensable", null, 35, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.STRING, 2),
                new CraftQuestComponent(owner, Material.CROSSBOW, 1))));
        questList.add(new Quest(this, owner, "S'entrainer au tir", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.TARGET, 4))));

        Random rng = new Random();
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        if (allies.size() > 0) {
            Player target = allies.get(rng.nextInt(allies.size()));
            questList.add(new Quest(this, owner, "J'ai cru que c'était un cochon !", null, 75, 0, Arrays.asList(
                    new KillPlayerQuestComponent(owner, target, 1))));
        }

        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "S'approcher de l'ennemi", null, 80, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 5 * 60, 40, true))));
            questList.add(new Quest(this, owner, "Appliquer la leéon", null, 175, 0, Arrays.asList(
                    new AimOnEntityQuestComponent(owner, EntityType.ARROW, 10, EntityType.PLAYER, false))));
        }
        questList.add(new Quest(this, owner, "Des armes de champions", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_SWORD, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_AXE, 1))));
        questList.add(new Quest(this, owner, "Agrémenter les tirs", null, 80, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.GUNPOWDER, 10),
                new GetItemQuestComponent(owner, Material.SUGAR_CANE, 12),
                new CraftQuestComponent(owner, Material.FIREWORK_STAR, 3),
                new CraftQuestComponent(owner, Material.FIREWORK_ROCKET, 3))));

        if (data().teams.size() > 1) {
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            Player target = ennemies.get(rng.nextInt(ennemies.size()));
            questList.add(new Quest(this, owner, "Traquer l'ennemi", null, 350, 0, Arrays.asList(
                    new FireworkExplodesOnPlayerQuestComponent(owner, target, 2),
                    new KillPlayerQuestComponent(owner, target, 1))));

            List<AQuestComponent> questComponentList = new ArrayList<>();
            for (Player ennemy : ennemies) {
                questComponentList.add(new KillPlayerQuestComponent(owner, ennemy, 1));
            }
            questList.add(new Quest(this, owner, "Folie meurtriére", null, ennemies.size() * 200, 0, questComponentList));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.HUNTER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (event.getEntity().getKiller().getUniqueId() != owner.getUniqueId()) return;
        boolean isTier1 = tier1EntityTypes.contains(event.getEntity().getType());
        if (!isTier1 && !tier2EntityTypes.contains(event.getEntity().getType())) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, isTier1 ? 1 : 2, ScoreType.FLAT);
    }
}
