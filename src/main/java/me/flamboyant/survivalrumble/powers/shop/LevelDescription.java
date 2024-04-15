package me.flamboyant.survivalrumble.powers.shop;

import java.util.List;

public class LevelDescription {
    private int price;
    private List<String> description;

    public LevelDescription(int price, List<String> description) {
        this.price = price;
        this.description = description;
        this.description.add(0, "Prix : " + price);
    }

    public int getPrice() {
        return price;
    }

    public List<String> getDescription() {
        return description;
    }
}
