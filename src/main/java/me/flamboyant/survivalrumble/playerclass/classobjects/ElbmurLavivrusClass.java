package me.flamboyant.survivalrumble.playerclass.classobjects;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.quests.component.*;
import me.flamboyant.survivalrumble.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElbmurLavivrusClass extends APlayerClass {
    public ElbmurLavivrusClass(Player owner) {
        super(owner);
        this.triggers.add(ScoringTriggerType.BLOCK_BREAK);
        this.triggers.add(ScoringTriggerType.BLOCK_EXPLOSION);
        this.triggers.add(ScoringTriggerType.BLOCK_PLACE);
        this.triggers.add(ScoringTriggerType.BLOCK_BURNED);
        this.triggers.add(ScoringTriggerType.BLOCK_MODIFIER);

        scoringDescription = "leic ua sésopxe esab at ed scolb sel eriurtéD";
    }

    @Override
    protected void buildQuestList() {
        questList.add(new Quest(this, owner, "! ehcir rineveD", null, 1500, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.DIAMOND_ORE, 32))));
        List<String> otherTeams = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList());
        if (otherTeams.size() > 0) {
            List<Player> ennemies = owner.getWorld().getPlayers().stream().filter(p -> otherTeams.contains(data().playersTeam.get(p.getUniqueId()))).collect(Collectors.toList());
            List<AQuestComponent> killQuestComponent = ennemies.stream().map(p -> new KillPlayerQuestComponent(owner, p, 1)).collect(Collectors.toList());
            questList.add(new Quest(this, owner, "serret sel reificaP", null, 1500, 0, killQuestComponent));
            questList.add(new Quest(this, owner, "egéis el rineT", null, 100, 0, Arrays.asList(
                    new HQProximityQuestComponent(owner, 20 * 60, 50, true),
                    new AimOnEntityQuestComponent(owner, EntityType.ARROW, 10, EntityType.PLAYER, false))));
        }
        questList.add(new Quest(this, owner, "tabmoc el rerapérP", null, 100, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_SWORD, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_LEGGINGS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_CHESTPLATE, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_BOOTS, 1),
                new CraftQuestComponent(owner, Material.DIAMOND_HELMET, 1),
                new CraftQuestComponent(owner, Material.BUCKET, 1),
                new CraftQuestComponent(owner, Material.GOLDEN_APPLE, 3),
                new CraftQuestComponent(owner, Material.BOW, 1),
                new CraftQuestComponent(owner, Material.ARROW, 32),
                new GetItemQuestComponent(owner, Material.ENDER_PEARL, 2))));
        questList.add(new Quest(this, owner, "nitsed nos retpeccA", null, 100, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.STONE, 640))));
        questList.add(new Quest(this, owner, "lituo elbasnepsidnI", null, 50, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.DIAMOND_PICKAXE, 1))));
        questList.add(new Quest(this, owner, "xueim ervivruS", null, 40, 0, Arrays.asList(
                new FurnaceExtractQuestComponent(owner, Material.COOKED_BEEF, 64))));
        questList.add(new Quest(this, owner, "trom al ressuopeR", null, 50, 0, Arrays.asList(
                new KillMobQuestComponent(owner, EntityType.ZOMBIE, 16))));
        questList.add(new Quest(this, owner, "elliavuort eréimerP", null, 25, 0, Arrays.asList(
                new BreakBlocQuestComponent(owner, Material.COAL_ORE, 1))));
        questList.add(new Quest(this, owner, "ednegél ed tubéd nU", null, 15, 0, Arrays.asList(
                new CraftQuestComponent(owner, Material.WOODEN_PICKAXE, 1))));
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ELBMURLAVIVRUS;
    }

    @Override
    public Integer onBlockModifierTrigger(Integer score, BlockData blockData, Location blockLocation, String teamConcerned) {
        if (!teamConcerned.equals(data().playersTeam.get(owner.getUniqueId()))) return score;

        return score * -1;
    }

    @Override
    public void onBlockPlaceTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, false);
    }

    @Override
    public void onBlockBreakTrigger(Player playerWhoBreaks, Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onExplosionTrigger(Block block) {
        handleBlockModification(block, true);
    }

    @Override
    public void onBlockBurnedTrigger(Block block) {
        handleBlockModification(block, true);
    }

    private void handleBlockModification(Block block, boolean broken) {
        MaterialHelper mh = new MaterialHelper();
        if (mh.scoringMaterial.containsKey(block.getType()) || block.getType() == Material.WATER || block.getType() == Material.LAVA)
            return;
        Location location = block.getLocation();
        String concernedTeamName = TeamHelper.getTeamHeadquarterName(location);
        String ownerTeamName = data().playersTeam.get(owner.getUniqueId());
        if (concernedTeamName == null || !ownerTeamName.equals(concernedTeamName)) return;
        if (block.getWorld().getHighestBlockYAt(location) > location.getBlockY()) return;

        int coef = broken ? 1 : -1;
        GameManager.getInstance().addScore(ownerTeamName, (coef * 1), ScoreType.REVERSIBLE);
    }
}
