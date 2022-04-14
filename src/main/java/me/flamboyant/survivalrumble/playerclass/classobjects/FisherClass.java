package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
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
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Arrays;

public class FisherClass extends APlayerClass implements Listener {
    public FisherClass(Player owner) {
        super(owner);

        scoringDescription = "Pécher des poissons dans ta base";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Comparer les mets", null, 15, 0, Arrays.asList(
                new GetItemQuestComponent(owner, Material.PORKCHOP, 2),
                new GetItemQuestComponent(owner, Material.COD, 2))));
        questList.add(new Quest(this, owner, "Le choix du coeur", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.FISHING_ROD, 1))));
        questList.add(new Quest(this, owner, "Une déco douteuse", null, 50, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.BARREL, 16, true))));
        questList.add(new Quest(this, owner, "Détourner l'instrument", null, 50, 0, Arrays.asList(
                new HookMobQuestComponent(owner, 10, Arrays.asList(EntityType.COW, EntityType.PIG, EntityType.CHICKEN, EntityType.SHEEP)))));
        questList.add(new Quest(this, owner, "Apprendre le commerce", null, 100, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.EMERALD, 10, false))));
        questList.add(new Quest(this, owner, "Prendre des risques", null, 50, 0, Arrays.asList(
                new HookMobQuestComponent(owner, 10, Arrays.asList(EntityType.CREEPER)))));
        questList.add(new Quest(this, owner, "Un échange en or", null, 500, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.FISHING_ROD, 1, false))));
        questList.add(new Quest(this, owner, "Se préparer au temps rudes", null, 100, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.COOKED_COD, 64))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Si ils veulent vraiment venir ...", null, 200, 0, Arrays.asList(
                    new HookPlayersFromTeamQuestComponent(owner, 10, true))));
        }
        questList.add(new Quest(this, owner, "Un défi mortel", null, 1000, 0, Arrays.asList(
                new HookMobQuestComponent(owner, 25, Arrays.asList(EntityType.GHAST)),
                new HookMobQuestComponent(owner, 25, Arrays.asList(EntityType.ZOMBIFIED_PIGLIN)))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.FISHER;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer() != owner) return;
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH && event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;
        Location location = event.getPlayer().getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || !data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(concernedTeamName, (int) (5 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.PERFECT);
    }
}
