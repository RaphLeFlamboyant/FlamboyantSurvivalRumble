package me.flamboyant.survivalrumble.delegates;

import org.bukkit.block.Block;

@FunctionalInterface
public interface BlockCallback {
    void runOnBlock(Block block);
}
