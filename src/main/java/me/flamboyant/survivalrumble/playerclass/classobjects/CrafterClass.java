package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.HangingPlaceQuestComponent;
import me.flamboyant.survivalrumble.quests.component.KillMobQuestComponent;
import me.flamboyant.survivalrumble.quests.component.KillPlayerQuestComponent;
import me.flamboyant.survivalrumble.quests.component.VillagerTradeQuestComponent;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CrafterClass extends APlayerClass implements Listener {
    private HashSet<Material> carftedItems = new HashSet<>();

    public CrafterClass(Player owner) {
        super(owner);

        scoringDescription = "Crafter des items que tu n'as pas encore crafté";
    }

    @Override
    protected void buildQuestList() {
        Random rng = new Random();
        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        if (otherTeams.size() > 0) {
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            Player target = ennemies.get(rng.nextInt(ennemies.size()));
            questList.add(new Quest(this, owner, "Un ennemi secret", null, 200, 0, Arrays.asList(
                    new KillPlayerQuestComponent(owner, target, 1))));
        }
        questList.add(new Quest(this, owner, "Un commerce difficile", null, 200, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.CHISELED_STONE_BRICKS, 32, false))));
        questList.add(new Quest(this, owner, "Décorer les lieux", null, 100, 0, Arrays.asList(
                new HangingPlaceQuestComponent(owner, EntityType.PAINTING, 8))));
        questList.add(new Quest(this, owner, "N'avoir plus que éa é faire", null, 200, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.GHAST, 5))));
        questList.add(new Quest(this, owner, "Sur un malentendu ...", null, 3000, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.WITHER, 5))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.CRAFTER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(owner.getUniqueId())) return;
        if (carftedItems.contains(event.getInventory().getResult().getType())) return;
        Location location = event.getWhoClicked().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (!concernedTeamName.equals(data().playersTeam.get(owner.getUniqueId()))) return;

        GameManager.getInstance().addScore(concernedTeamName, (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
        carftedItems.add(event.getInventory().getResult().getType());
    }
}
