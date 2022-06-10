package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.stream.Collectors;

public class TrapperClass extends APlayerClass {
    public TrapperClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.DEATH);

        scoringDescription = "Un ennemi doit mourir de piége é moins de 100 blocs de distance de toi";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Les bases", null, 20, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.OAK_SIGN, 4),
                new CraftQuestComponent(owner, Material.WOODEN_SHOVEL, 1))));
        questList.add(new Quest(this, owner, "Premiéres proies", null, 50, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.PIG, 25))));
        questList.add(new Quest(this, owner, "Des outils utiles", null, 75, 0, Arrays.asList(
                new BucketFillQuestComponent(owner, Material.LAVA, 1),
                new GetItemQuestComponent(owner, Material.STRING, 8),
                new CraftQuestComponent(owner, Material.TRIPWIRE_HOOK, 4),
                new BreakBlocQuestComponent(owner, Material.REDSTONE_ORE, 4),
                new CraftQuestComponent(owner, Material.PISTON, 4))));

        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        List<Player> allies = owner.getWorld().getPlayers().stream().filter(p -> p != owner && data().playersTeam.get(p.getUniqueId()).equals(ownerTeam)).collect(Collectors.toList());
        if (allies.size() > 0) {
            questList.add(new Quest(this, owner, "Un piége placé trop prés", null, 25, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, Arrays.asList(EntityDamageEvent.DamageCause.FALL, EntityDamageEvent.DamageCause.LAVA), 25, 1, allies))));
        }

        if (data().teams.size() > 1) {
            ArrayList<String> foesTeamList = new ArrayList<>(data().teams);
            foesTeamList.remove(ownerTeam);
            Random rng = new Random();
            String targetTeam = foesTeamList.get(rng.nextInt(foesTeamList.size()));
            questList.add(new Quest(this, owner, "Observer l'environnement", null, 200, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 10 * 60, 50, true, targetTeam))));
            questList.add(new Quest(this, owner, "Placer les piéges", null, 150, 0, Arrays.asList(
                    new PlaceBlocQuestComponent(owner, Material.PISTON, 4, false),
                    new PlaceBlocQuestComponent(owner, Material.OAK_SIGN, 4, false),
                    new PlaceBlocQuestComponent(owner, Material.REDSTONE, 16, false),
                    new PlaceBlocQuestComponent(owner, Material.TRIPWIRE, 8, false),
                    new PlaceBlocQuestComponent(owner, Material.TRIPWIRE_HOOK, 4, false))));
            questList.add(new Quest(this, owner, "Attirer l'ennemi dans le piége", null, 150, 0, Arrays.asList(
                    new HookPlayersFromTeamQuestComponent(owner, 2, Arrays.asList(targetTeam)))));
            List<UUID> targetTeamPlayerIds = data().playersByTeam.get(targetTeam);
            List<Player> targetTeamPlayers = targetTeamPlayerIds.stream().map(pid -> owner.getServer().getPlayer(pid)).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "Terminer le travail é la main", null, 800, 0, Arrays.asList(
                    new ProximityPlayerDeathQuestComponent(owner, 25, 2, targetTeamPlayers))));
        }
        questList.add(new Quest(this, owner, "Une retraite bien méritée", null, 500, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.COBBLESTONE, 128, true),
                new PlaceBlocQuestComponent(owner, Material.OAK_LOG, 40, true),
                new PlaceBlocQuestComponent(owner, Material.OAK_STAIRS, 38, true),
                new PlaceBlocQuestComponent(owner, Material.GLASS_PANE, 16, true),
                new PlaceBlocQuestComponent(owner, Material.POTTED_POPPY, 1, true),
                new PlaceBlocQuestComponent(owner, Material.POTTED_DANDELION, 1, true))));
        questList.add(new Quest(this, owner, "Faire du tourisme", null, 1000, 0, Arrays.asList(
                new BiomeVentureQuestComponent(owner,
                        Arrays.asList(Biome.JUNGLE, Biome.SPARSE_JUNGLE, Biome.BAMBOO_JUNGLE, Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.WOODED_BADLANDS, Biome.MUSHROOM_FIELDS, Biome.ICE_SPIKES, Biome.THE_END),
                        10 * 60))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.TRAPPER;
    }

    @Override
    public void onPlayerDeathTrigger(Player killed, Player killer) {
        EntityDamageEvent.DamageCause deathCause = killed.getLastDamageCause().getCause();
        // killer means no trap
        if (killer != null) return;
        // Mob & co is not a valid death, we don't want trapper to earn free points
        if (deathCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        // the trapper is too far away
        if (owner.getWorld() != killed.getWorld()
                || owner.getLocation().distance(killed.getLocation()) > 100) return;
        // If an ally comes to put lava bucket on ennemy for example
        String ownerTeam = data().playersTeam.get(owner.getUniqueId());
        for (UUID playerId : data().playersByTeam.get(ownerTeam)) {
            Player player = owner.getServer().getPlayer(playerId);
            if (playerId != owner.getUniqueId() && player.getWorld() == killed.getWorld() && player.getLocation().distance(killed.getLocation()) < 48)
                return;
        }
        // the dead is in the trapper team
        if (data().playersTeam.get(killed.getUniqueId()).equals(ownerTeam)) return;

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), 400, ScoreType.FLAT);
    }
}
