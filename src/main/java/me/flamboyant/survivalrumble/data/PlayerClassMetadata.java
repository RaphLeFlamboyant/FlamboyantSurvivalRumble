package me.flamboyant.survivalrumble.data;

import org.bukkit.Material;
import org.bukkit.Sound;

public class PlayerClassMetadata {
    private PlayerClassType playerClassType;
    private PlayerClassCategory playerClassCategory;
    private Material playerClassRepresentation;
    private String displayName;
    private Sound sound;
    private String title;
    private String subtitle;

    public PlayerClassMetadata(PlayerClassType playerClassType,
                               PlayerClassCategory playerClassCategory,
                               Material playerClassRepresentation,
                               String displayName,
                               Sound sound) {
        this.playerClassType = playerClassType;
        this.playerClassCategory = playerClassCategory;
        this.playerClassRepresentation = playerClassRepresentation;
        this.displayName = displayName;
        this.sound = sound;
    }

    public PlayerClassMetadata(PlayerClassType playerClassType,
                               PlayerClassCategory playerClassCategory,
                               Material playerClassRepresentation,
                               String displayName,
                               Sound sound,
                               String title,
                               String subtitle) {
        this.playerClassType = playerClassType;
        this.playerClassCategory = playerClassCategory;
        this.playerClassRepresentation = playerClassRepresentation;
        this.displayName = displayName;
        this.sound = sound;
        this.title = title;
        this.subtitle = subtitle;
    }

    public PlayerClassType getPlayerClassType() {
        return playerClassType;
    }

    public PlayerClassCategory getPlayerClassCategory() {
        return playerClassCategory;
    }

    public Material getPlayerClassRepresentation() {
        return playerClassRepresentation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Sound getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
