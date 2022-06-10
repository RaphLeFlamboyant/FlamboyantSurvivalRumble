package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.BreakBlocQuestComponent;
import me.flamboyant.survivalrumble.quests.component.CraftQuestComponent;
import me.flamboyant.survivalrumble.quests.component.DestroyItemQuestComponent;
import me.flamboyant.survivalrumble.quests.component.HQExplosionQuestComponent;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BunglerClass extends APlayerClass {
    private static final int scoreCoef = 5;
    private static final int malusCoef = -4;

    public BunglerClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);

        scoringDescription = "Détruire des blocs de construction dans une base adverse";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "Une arme de destruction", null, 10, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_PICKAXE, 1))));
        questList.add(new Quest(this, owner, "Casser", null, 50, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.STONE, 64))));
        questList.add(new Quest(this, owner, "étendre l'arsenal", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.IRON_PICKAXE, 1),
                new CraftQuestComponent(owner, Material.FLINT_AND_STEEL, 1))));
        questList.add(new Quest(this, owner, "Détruire", null, 50, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.ANDESITE, 64))));
        questList.add(new Quest(this, owner, "Casser encore", null, 200, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.DIAMOND_ORE, 16))));
        questList.add(new Quest(this, owner, "Détruire encore", null, 200, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.GOLD_ORE, 16))));
        questList.add(new Quest(this, owner, "Encore plus d'armes", null, 25, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_PICKAXE, 1),
                new CraftQuestComponent(owner, Material.TNT, 8))));
        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        if (otherTeams.size() > 0) {
            questList.add(new Quest(this, owner, "Casser ailleurs", null, 500, 0, Arrays.asList(
                    new HQExplosionQuestComponent(owner, 8, otherTeams))));
        }
        questList.add(new Quest(this, owner, "énormément casser", null, 500, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.STONE, 640))));
        questList.add(new Quest(this, owner, "Détruire le précieux", null, 500, 0, Arrays.asList(
                new DestroyItemQuestComponent(owner, Material.DIAMOND_BLOCK, 1))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.BUNGLER;
    }

    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        if (playerWhoBreaks != owner) return;

        if (!MaterialHelper.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), (int) (scoreCoef * ScoringHelper.scoreAltitudeCoefficient(location.getBlockY())), ScoreType.FLAT);
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        if (!MaterialHelper.scoringMaterial.containsKey(block.getBlockData().getMaterial())) return;
        if (!data().playersTeam.get(playerWhoBreaks.getUniqueId()).equals(data().playersTeam.get(owner.getUniqueId()))) return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        if (concernedTeamName == null || data().playersTeam.get(owner.getUniqueId()).equals(concernedTeamName)) return;

        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        GameManager.getInstance().addScore(ownerTeamName, (int) (malusCoef * ScoringHelper.scoreAltitudeCoefficient(block.getLocation().getBlockY())), ScoreType.FLAT);
    }
}
