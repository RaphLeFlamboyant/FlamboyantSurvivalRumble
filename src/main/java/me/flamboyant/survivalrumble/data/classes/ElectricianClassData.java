package me.flamboyant.survivalrumble.data.classes;

import org.bukkit.Location;

import java.util.HashMap;

public class ElectricianClassData extends PlayerClassData {
    public HashMap<Location, Float> blockLocationList = new HashMap<>();
}
