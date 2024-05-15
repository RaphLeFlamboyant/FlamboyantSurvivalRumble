package me.flamboyant.survivalrumble.powers.impl;

import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ASpellPower implements IChampionPower, Listener {
    protected Player powerOwner;

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        this.powerOwner = powerOwner;

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
        powerOwner.getInventory().addItem(getSpellItem());

        onActivate();
    }

    @Override
    public void deactivate() {
        powerOwner.setCooldown(getSpellItem().getType(), 0);
        PlayerInteractEvent.getHandlerList().unregister(this);
        onDeactivate();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != powerOwner) return;
        if (!ItemHelper.isExactlySameItemKind(event.getItem(), getSpellItem())) return;
        event.setCancelled(true);
        if (powerOwner.hasCooldown(getSpellItem().getType())) return;

        if (applySpellEffect())
            powerOwner.setCooldown(getSpellItem().getType(), getCooldown());
        applySpellEffect();
    }

    protected void onActivate() {

    }

    protected void onDeactivate() {

    }

    protected abstract ItemStack getSpellItem();
    protected abstract boolean applySpellEffect();
    protected abstract int getCooldown();
}
