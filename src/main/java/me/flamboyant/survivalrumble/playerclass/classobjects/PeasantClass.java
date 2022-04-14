package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.ScoringHelper;
import me.flamboyant.survivalrumble.utils.ScoringTriggerType;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PeasantClass extends APlayerClass {
    public PeasantClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);

        scoringDescription = "Pose des blocs de paille dans ta base";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Prendre soin de ses bétes", null, 50, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.PIG, 8))));
        questList.add(new Quest(this, owner, "ériger une ferme", null, 50, 0, Arrays.asList(
                new PlaceBlocQuestComponent(owner, Material.OAK_PLANKS, 64, true))));
        questList.add(new Quest(this, owner, "Faire affaires", null, 200, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.POTATO, 1, true))));
        questList.add(new Quest(this, owner, "Accélérer ses récoltes", null, 150, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.SKELETON, 16),
                new GetItemQuestComponent(owner, Material.BONE, 8),
                new CraftQuestComponent(owner, Material.BONE_MEAL, 24),
                new FertilizeQuestComponent(owner, 24, false))));
        questList.add(new Quest(this, owner, "Ramener moultes richesses", null, 500, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.COD_BUCKET, 3, true))));
        questList.add(new Quest(this, owner, "L'outil parfait", null, 200, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.DIAMOND_ORE, 2),
                new CraftQuestComponent(owner, Material.DIAMOND_HOE, 1))));
        questList.add(new Quest(this, owner, "Industrialiser l'activité", null, 500, 0, Arrays.asList(
                new VillagerTradeQuestComponent(owner, Material.WHEAT, 300, true))));
        questList.add(new Quest(this, owner, "Devenir patron et avoir trop de vacances", null, 50, 0, Arrays.asList(
                new BiomeVentureQuestComponent(owner, Biome.OCEAN, 1 * 60))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Passe temps péche", null, 500, 0, Arrays.asList(
                    new HookPlayersFromTeamQuestComponent(owner, 8, true))));
        }
        questList.add(new Quest(this, owner, "Revenir bosser un peu", null, 250, 0, Arrays.asList(
                new FertilizeQuestComponent(owner, 10, true))));
        if (data().teams.size() > 1) {
            questList.add(new Quest(this, owner, "Passe temps tir", null, 500, 0, Arrays.asList(
                    new AimOnEntityQuestComponent(owner, EntityType.ARROW, 8, EntityType.PLAYER, false))));
        }
        questList.add(new Quest(this, owner, "Revenir bosser un peu", null, 250, 0, Arrays.asList(
                new FertilizeQuestComponent(owner, 10, true))));
        if (data().teams.size() > 1) {
            List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
            Random rng = new Random();
            questList.add(new Quest(this, owner, "Espionnage industriel", null, 2000, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 5 * 60, 8, false, otherTeams.get(rng.nextInt(otherTeams.size()))))));
        }
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.PEASANT;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        handleBlockBreak(block);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockBreak(block);
    }

    private void handleBlockBreak(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        int coef = broken ? -1 : 1;
        if (block.getType() != Material.HAY_BLOCK) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(ownerTeamName, (coef * (int) (10 * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY()))), ScoreType.REVERSIBLE);
    }
}
