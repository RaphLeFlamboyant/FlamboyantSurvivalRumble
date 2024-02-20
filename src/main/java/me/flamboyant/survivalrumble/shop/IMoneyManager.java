package me.flamboyant.survivalrumble.shop;

import org.bukkit.entity.Player;

public interface IMoneyManager {
    boolean trySpendMoney(Player player, int amount);

    void earnMoney(Player player, int amount);
}
