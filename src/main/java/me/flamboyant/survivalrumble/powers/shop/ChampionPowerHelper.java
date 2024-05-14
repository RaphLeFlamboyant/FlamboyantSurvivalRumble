package me.flamboyant.survivalrumble.powers.shop;

import me.flamboyant.survivalrumble.powers.ChampionPowerType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionPowerHelper {
    public static List<ChampionPowerShopItem> buildChampionPowerList() {
        return Arrays.asList(
                // Pour changer : Remplir depuis le code généré dans le Excel
                new ChampionPowerShopItem(ChampionPowerType.EQUIPMENT, Arrays.asList(new LevelDescription(10, new ArrayList<>(Arrays.asList("Equipement Cuir et Pierre"))), new LevelDescription(20, new ArrayList<>(Arrays.asList("Equipement Cuir et Pierre", "-> Equipement Or"))), new LevelDescription(50, new ArrayList<>(Arrays.asList("Equipement Or", "-> Equipement Fer"))), new LevelDescription(100, new ArrayList<>(Arrays.asList("Equipement Fer", "-> Equipement Diamant"))), new LevelDescription(200, new ArrayList<>(Arrays.asList("Equipement Diamant", "-> Equipement Netherite"))), new LevelDescription(200, new ArrayList<>(Arrays.asList("Equipement Netherite", "-> Protection 1"))), new LevelDescription(300, new ArrayList<>(Arrays.asList("Protection 1", "-> Protection 2"))), new LevelDescription(400, new ArrayList<>(Arrays.asList("Protection 2", "-> Protection 3"))), new LevelDescription(500, new ArrayList<>(Arrays.asList("Protection 3", "->Protection 4"))), new LevelDescription(200, new ArrayList<>(Arrays.asList("Protection 4", "-> Full Enchant"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Full Enchant")))), "Equipement", Material.LEATHER_CHESTPLATE),
                new ChampionPowerShopItem(ChampionPowerType.BOW, Arrays.asList(new LevelDescription(50, new ArrayList<>(Arrays.asList("Arc + 2 stacks de Fleches"))), new LevelDescription(100, new ArrayList<>(Arrays.asList("Arc + 2 stacks de Fleches", "->Power 1"))), new LevelDescription(300, new ArrayList<>(Arrays.asList("Power 1", "-> Power 3 + 1 stack de Flèches"))), new LevelDescription(200, new ArrayList<>(Arrays.asList("Power 3 + 1 stack de Flèches", "-> Power 4"))), new LevelDescription(500, new ArrayList<>(Arrays.asList("Power 4", "-> Full Enchant"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Full Enchant")))), "Archerie", Material.BOW),
                new ChampionPowerShopItem(ChampionPowerType.ENDERCHEST_STUFF, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Recoit les items de", "son Ender Chest")))), "Enderchest Drop", Material.ENDER_CHEST),
                new ChampionPowerShopItem(ChampionPowerType.NO_SHIELD, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Les assaillants perdent", "leur bouclier")))), "No Shield", Material.SHIELD),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_STRENGH, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Effet Force 1"))), new LevelDescription(2000, new ArrayList<>(Arrays.asList("Effet Force 1", "-> Effet Force 2"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Effet Force 2")))), "Effet Force", Material.POTION),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_RESISTANCE, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Effet Résistance 1"))), new LevelDescription(2000, new ArrayList<>(Arrays.asList("Effet Résistance 1", "->Effet Résistance 2"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Effet Résistance 2")))), "Effet Resistance", Material.POTION),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_SPEED, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Effet Speed 1"))), new LevelDescription(1000, new ArrayList<>(Arrays.asList("Effet Speed 1", "->Effet Speed 2"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Effet Speed 2")))), "Effet Vitesse", Material.POTION),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_JUMP, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Effet Jump 1"))), new LevelDescription(1000, new ArrayList<>(Arrays.asList("Effet Jump 1", "->Effet Jump 2"))), new LevelDescription(0, new ArrayList<>(Arrays.asList("Effet Jump 2")))), "Effet Saut", Material.POTION),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_FIRE_RESISTANCE, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Effet Résistance au Feu")))), "Effet Resistance au Feu", Material.BLAZE_POWDER),
                new ChampionPowerShopItem(ChampionPowerType.EFFECT_WATER_BREATHING, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Effet Respiration")))), "Effet Respiration", Material.CONDUIT),
                new ChampionPowerShopItem(ChampionPowerType.MOB_FRIEND, Arrays.asList(new LevelDescription(500, new ArrayList<>(Arrays.asList("Les mobs n'attaquent pas", "le capitaine")))), "Ami des Mobs", Material.CREEPER_HEAD),
                new ChampionPowerShopItem(ChampionPowerType.ENEMIES_DETECTION, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Les assaillants à moins", "de 30 blocs sont en", "surbrillance")))), "Detection des assaillants", Material.TARGET),
                new ChampionPowerShopItem(ChampionPowerType.NO_FALL_DAMAGE, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Pas de dégât de chute")))), "Pas de chute", Material.FEATHER),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_SCREAM, Arrays.asList(new LevelDescription(1000, new ArrayList<>(Arrays.asList("Envoi les ennemis proches", "dans les airs")))), "Sort : Cri d'Effroi", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_SUMMON, Arrays.asList(new LevelDescription(1000, new ArrayList<>(Arrays.asList("Invoque des mobs près", "des assaillants")))), "Sort : Invocation", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_JUMP, Arrays.asList(new LevelDescription(1000, new ArrayList<>(Arrays.asList("Fait un grand saut")))), "Sort : Saut", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_EXPLOSIVE_PUN, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Le prochain bloc posé", "par un assaillant explose")))), "Sort : Farce Explosive", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_JAIL, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Enferme tous les  assaillants", "dans une prison d'obsidienne")))), "Sort : Prison", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_ADIOS, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("Fait disparaitre tous les", "blocs sous les pieds du", "capitaine jusqu'à la couche 0")))), "Sort : Adios!", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.SPELL_SHUFFLE, Arrays.asList(new LevelDescription(1000, new ArrayList<>(Arrays.asList("Intervertit la position", "des assaillants")))), "Sort : Shuffle", Material.WRITABLE_BOOK),
                new ChampionPowerShopItem(ChampionPowerType.COME_BACK_REVIVE, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("A 0 cœurs récupère", "3 cœurs + 2 d'abso", "un assaillant allié perd", "tout son équipement")))), "Sauvetage", Material.TOTEM_OF_UNDYING),
                new ChampionPowerShopItem(ChampionPowerType.COME_BACK_BALANCE, Arrays.asList(new LevelDescription(4000, new ArrayList<>(Arrays.asList("A 5 cœurs les assaillants", "subissent un poison jusqu'à", "5 cœurs max")))), "Equilibre", Material.DAMAGED_ANVIL),
                new ChampionPowerShopItem(ChampionPowerType.COME_BACK_JUMP_SCARE, Arrays.asList(new LevelDescription(2000, new ArrayList<>(Arrays.asList("A 2 cœurs teleporte", "le capitaine au point le", "plus haut de sa position")))), "Trouille", Material.JACK_O_LANTERN)

                );
    }
}
