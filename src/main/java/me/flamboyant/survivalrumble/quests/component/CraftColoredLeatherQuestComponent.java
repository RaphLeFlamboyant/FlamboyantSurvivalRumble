package me.flamboyant.survivalrumble.quests.component;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CraftColoredLeatherQuestComponent extends CraftQuestComponent {
    private DyeColor color;

    public CraftColoredLeatherQuestComponent(Player player, Material material, DyeColor color, String colorName, int quantity) {
        super(player, material, quantity);

        this.color = color;

        subQuestMessage += " de couleur " + colorName;
    }

    @Override
    protected boolean checkOtherConditions(CraftItemEvent event) {
        LeatherArmorMeta meta = (LeatherArmorMeta) event.getInventory().getResult().getItemMeta();

        System.out.println("Color used : " + meta.getColor() + " and expected : " + color.getColor());
        return meta.getColor().equals(color.getColor());
    }

}
