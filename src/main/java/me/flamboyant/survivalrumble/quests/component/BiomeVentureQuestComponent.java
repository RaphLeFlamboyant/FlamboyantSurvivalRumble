package me.flamboyant.survivalrumble.quests.component;

import me.flamboyant.survivalrumble.quests.Quest;
import me.flamboyant.survivalrumble.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BiomeVentureQuestComponent extends AQuestComponent {
    private List<Biome> biomes;
    private int durationInSeconds;
    private BukkitTask checkPlayerBiome = null;

    public BiomeVentureQuestComponent(Player player, Biome biome, int durationInSeconds) {
        super(player);

        biomes = new ArrayList<>();
        biomes.add(biome);
        this.durationInSeconds = durationInSeconds;

        String suffix = "";
        if (durationInSeconds <= 0) {
            suffix += " pendant ";
            if (durationInSeconds / 60 > 0) {
                suffix += (durationInSeconds / 60) + " minutes";
                durationInSeconds -= (durationInSeconds / 60) * 60;
                if (durationInSeconds > 0)
                    suffix += " et ";
            }
            suffix += durationInSeconds + " secondes";
        }

        subQuestMessage = "Aventure toi dans le biome " + biome + suffix;
    }

    public BiomeVentureQuestComponent(Player player, List<Biome> biomes, int durationInSeconds) {
        super(player);

        this.biomes = biomes;
        this.durationInSeconds = durationInSeconds;

        String suffix = " [";

        for (Biome biome : biomes) {
            suffix += biome + ", ";
        }
        suffix = suffix.substring(0, suffix.length() - 2) + "]";

        if (durationInSeconds > 0) {
            suffix += " pendant ";
            if (durationInSeconds / 60 > 0) {
                suffix += (durationInSeconds / 60) + " minutes";
                durationInSeconds -= (durationInSeconds / 60) * 60;
                if (durationInSeconds > 0)
                    suffix += " et ";
            }
            suffix += durationInSeconds + " secondes";
        }

        subQuestMessage = "Aventure toi dans un des biomes des biomes de cette liste " + suffix;
    }

    @Override
    public void startQuest(Quest owner) {
        super.startQuest(owner);
        checkPlayerBiome = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> checkPlayerBiome(), 20L, 20L);
    }

    @Override
    protected void stopQuest() {
        super.stopQuest();
        Bukkit.getScheduler().cancelTask(checkPlayerBiome.getTaskId());
    }

    private void checkPlayerBiome() {
        Location location = player.getLocation();
        Biome currentBiome = player.getWorld().getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (!biomes.contains(currentBiome)) return;

        if (--durationInSeconds <= 0) {
            stopQuest();
        }
    }
}
