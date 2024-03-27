package me.flamboyant.survivalrumble.powers.shop;

import me.flamboyant.survivalrumble.powers.ChampionPowerType;
import org.bukkit.Material;

import java.util.List;

public class ChampionPowerShopItem {
    private ChampionPowerType championPowerType;
    private int currentPowerLevel;
    private int maxPowerLevel;
    private String powerName;
    private Material powerAppearance;
    private List<LevelDescription> levelDescriptions;

    public ChampionPowerShopItem(ChampionPowerType championPowerType,
                                 List<LevelDescription> levelDescriptions,
                                 String powerName,
                                 Material powerAppearance) {
        this.championPowerType = championPowerType;
        this.levelDescriptions = levelDescriptions;
        this.maxPowerLevel = levelDescriptions.size() == 1 ? 1 : levelDescriptions.size() - 1;
        this.powerName = powerName;
        this.powerAppearance = powerAppearance;
    }

    public int getPrice() {
        return levelDescriptions.get(currentPowerLevel).getPrice();
    }

    public List<String> getDescription() {
        return levelDescriptions.get(currentPowerLevel).getDescription();
    }

    public boolean hasReachedMaxLevel() {
        return currentPowerLevel == maxPowerLevel;
    }

    public boolean levelUp() {
        if (hasReachedMaxLevel()) return false;

        currentPowerLevel++;
        return true;
    }

    public int getCurrentPowerLevel() {
        return currentPowerLevel;
    }

    public String getPowerName() {
        return powerName;
    }

    public ChampionPowerType getChampionPowerType() {
        return championPowerType;
    }

    public Material getPowerAppearance() {
        return powerAppearance;
    }
}
