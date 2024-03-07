package me.flamboyant.survivalrumble.powers;

import org.bukkit.Material;

import java.util.List;

public class ChampionPower {
    private ChampionPowerType championPowerType;
    private int currentPowerLevel;
    private int maxPowerLevel;
    private String powerName;
    private Material powerAppearance;
    private List<LevelDescription> levelDescriptions;

    public ChampionPower(ChampionPowerType championPowerType,
                         List<LevelDescription> levelDescriptions,
                         int maxPowerLevel,
                         String powerName,
                         Material powerAppearance) {
        this.championPowerType = championPowerType;
        this.levelDescriptions = levelDescriptions;
        this.maxPowerLevel = maxPowerLevel;
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
