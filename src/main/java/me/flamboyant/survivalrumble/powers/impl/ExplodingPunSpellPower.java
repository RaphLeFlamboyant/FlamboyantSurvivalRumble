package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.survivalrumble.data.SurvivalRumbleData;
import me.flamboyant.survivalrumble.utils.TeamHelper;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ExplodingPunSpellPower extends ASpellPower implements Listener {
    private boolean isTriggered;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isTriggered) return;
        if (event.getPlayer() == powerOwner) return;
        var data = SurvivalRumbleData.getSingleton();
        var blockLocation = event.getBlock().getLocation();
        if (!TeamHelper.isLocationInHeadQuarter(blockLocation, data.getPlayerTeam(powerOwner))) return;

        blockLocation.getWorld().createExplosion(blockLocation, 1.5f, true);
        isTriggered = false;
    }

    @Override
    protected void onActivate() {

    }

    @Override
    protected void onDeactivate() {
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    @Override
    protected ItemStack getSpellItem() {
        return ItemHelper.generateItem(Material.TNT,
                1,
                "Farce Explosive",
                Arrays.asList("Le prochain bloc posé", "par un assaillant explose"),
                true,
                Enchantment.ARROW_FIRE,
                true,
                true);
    }

    @Override
    protected boolean applySpellEffect() {
        isTriggered = true;

        return true;
    }

    @Override
    protected int getCooldown() {
        return 20 * 60 * 2;
    }
}
