package me.flamboyant.survivalrumble.shop;

public class ShopItem {
    private String itemName;
    private int unitaryPrice;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(int unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }
}
