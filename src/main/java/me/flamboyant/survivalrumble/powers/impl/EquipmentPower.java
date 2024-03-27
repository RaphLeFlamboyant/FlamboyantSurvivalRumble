package me.flamboyant.survivalrumble.powers.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class EquipmentPower implements IChampionPower {

    @Override
    public void activate(Player powerOwner, int powerLevel) {
        List<Material> materials;

        powerOwner.getInventory().addItem(new ItemStack(Material.SHIELD));

        switch (powerLevel) {
            case 1 :
                materials = Arrays.asList(Material.LEATHER_CHESTPLATE, Material.LEATHER_BOOTS,
                        Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS, Material.STONE_SWORD, Material.STONE_AXE,
                        Material.STONE_PICKAXE, Material.STONE_SHOVEL);

                for (Material material : materials) {
                    ItemStack item = new ItemStack(material);
                    powerOwner.getInventory().addItem(item);
                }
                break;
            case 2:
                materials = Arrays.asList(Material.GOLDEN_CHESTPLATE, Material.GOLDEN_BOOTS,
                        Material.GOLDEN_HELMET, Material.GOLDEN_LEGGINGS, Material.GOLDEN_SWORD, Material.GOLDEN_AXE,
                        Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL);

                for (Material material : materials) {
                    ItemStack item = new ItemStack(material);
                    powerOwner.getInventory().addItem(item);
                }
                break;
            case 3:
                materials = Arrays.asList(Material.IRON_CHESTPLATE, Material.IRON_BOOTS,
                        Material.IRON_HELMET, Material.IRON_LEGGINGS, Material.IRON_SWORD, Material.IRON_AXE,
                        Material.IRON_PICKAXE, Material.IRON_SHOVEL);

                for (Material material : materials) {
                    ItemStack item = new ItemStack(material);
                    powerOwner.getInventory().addItem(item);
                }
                break;
            case 4:
                materials = Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.DIAMOND_BOOTS,
                        Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS, Material.DIAMOND_SWORD, Material.DIAMOND_AXE,
                        Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL);

                for (Material material : materials) {
                    ItemStack item = new ItemStack(material);
                    powerOwner.getInventory().addItem(item);
                }
                break;
            case 5:
                materials = Arrays.asList(Material.NETHERITE_CHESTPLATE, Material.NETHERITE_BOOTS,
                        Material.NETHERITE_HELMET, Material.NETHERITE_LEGGINGS, Material.NETHERITE_SWORD, Material.NETHERITE_AXE,
                        Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL);

                for (Material material : materials) {
                    ItemStack item = new ItemStack(material);
                    powerOwner.getInventory().addItem(item);
                }
                break;
            case 6,7,8,9,10:
                var enchantmentLevel = powerLevel - 5;
                var item = new ItemStack(Material.NETHERITE_CHESTPLATE);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Math.min(4, enchantmentLevel));
                item.addEnchantment(Enchantment.THORNS, Math.max(0, enchantmentLevel - 3));
                powerOwner.getInventory().addItem(item);

                item = new ItemStack(Material.NETHERITE_BOOTS);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Math.min(4, enchantmentLevel));
                item.addEnchantment(Enchantment.PROTECTION_FALL, Math.min(4, enchantmentLevel));
                item.addEnchantment(Enchantment.DEPTH_STRIDER, Math.max(0, enchantmentLevel - 4) * 3);
                item.addEnchantment(Enchantment.SOUL_SPEED, Math.max(0, enchantmentLevel - 4) * 3);
                powerOwner.getInventory().addItem(item);

                item = new ItemStack(Material.NETHERITE_HELMET);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Math.min(4, enchantmentLevel));
                item.addEnchantment(Enchantment.OXYGEN, Math.max(0, enchantmentLevel - 4) * 3);
                powerOwner.getInventory().addItem(item);

                item = new ItemStack(Material.NETHERITE_LEGGINGS);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Math.min(4, enchantmentLevel));
                item.addEnchantment(Enchantment.SWIFT_SNEAK, Math.max(0, enchantmentLevel - 4) * 3);
                powerOwner.getInventory().addItem(item);

                item = new ItemStack(Material.NETHERITE_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, enchantmentLevel);
                item.addEnchantment(Enchantment.SWEEPING_EDGE, Math.max(0, enchantmentLevel - 2));
                item.addEnchantment(Enchantment.KNOCKBACK, Math.max(0, enchantmentLevel - 4) * 2);

                item = new ItemStack(Material.NETHERITE_AXE);
                item.addEnchantment(Enchantment.DAMAGE_ALL, enchantmentLevel);
                item.addEnchantment(Enchantment.DIG_SPEED, enchantmentLevel);

                item = new ItemStack(Material.NETHERITE_PICKAXE);
                item.addEnchantment(Enchantment.DIG_SPEED, enchantmentLevel);
                item = new ItemStack(Material.NETHERITE_SHOVEL);
                item.addEnchantment(Enchantment.DIG_SPEED, enchantmentLevel);
                break;
        }
    }

    @Override
    public void deactivate() {

    }
}
