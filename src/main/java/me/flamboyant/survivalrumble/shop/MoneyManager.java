package me.flamboyant.survivalrumble.shop;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class MoneyManager implements IMoneyManager {
    private HashMap<Player, Integer> playerToMoney = new HashMap<>();

    public boolean trySpendMoney(Player player, int amount) {
        if (playerToMoney.containsKey(player)
                && playerToMoney.get(player) >= amount) {
            playerToMoney.put(player, playerToMoney.get(player) - amount);
            return true;
        }

        return false;
    }

    public void earnMoney(Player player, int amount) {
        if (!playerToMoney.containsKey(player)) {
            playerToMoney.put(player, 0);
        }

        playerToMoney.put(player, playerToMoney.get(player) + amount);
    }
}
