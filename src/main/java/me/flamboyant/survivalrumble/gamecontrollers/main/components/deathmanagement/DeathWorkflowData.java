package me.flamboyant.survivalrumble.gamecontrollers.main.components.deathmanagement;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeathWorkflowData {
    public Player deadPlayer;
    public Location deathLocation;
    public Location respawnLocation;
    public List<ItemStack> keptItems;
    public RespawnMode respawnMode;
}
