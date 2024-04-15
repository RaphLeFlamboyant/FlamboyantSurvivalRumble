package me.flamboyant.survivalrumble.shop;

import me.flamboyant.survivalrumble.delegates.TryRunOnPlayerCallback;
import org.bukkit.inventory.ItemStack;

public class ShopItemController {
    private ItemStack representation;
    private TryRunOnPlayerCallback tryBuyOne;
    private TryRunOnPlayerCallback tryBuyAll;

    public ItemStack getRepresentation() {
        return representation;
    }

    public void setRepresentation(ItemStack representation) {
        this.representation = representation;
    }

    public TryRunOnPlayerCallback getTryBuyOne() {
        return tryBuyOne;
    }

    public void setTryBuyOne(TryRunOnPlayerCallback tryBuyOne) {
        this.tryBuyOne = tryBuyOne;
    }

    public TryRunOnPlayerCallback getTryBuyAll() {
        return tryBuyAll;
    }

    public void setTryBuyAll(TryRunOnPlayerCallback tryBuyAll) {
        this.tryBuyAll = tryBuyAll;
    }
}
