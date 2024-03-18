package me.flamboyant.survivalrumble.powers;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ChampionPowerHelper {
    public static List<ChampionPower> buildChampionPowerList() {
        return Arrays.asList(
                // Pour changer : Remplir depuis le code généré dans le Excel
                new ChampionPower(ChampionPowerType.EQUIPMENT, Arrays.asList(new LevelDescription(10, Arrays.asList("Equipement Cuir et Pierre")), new LevelDescription(20, Arrays.asList("Equipement Cuir et Pierre", "-> Equipement Or")), new LevelDescription(50, Arrays.asList("Equipement Or", "-> Equipement Fer")), new LevelDescription(100, Arrays.asList("Equipement Fer", "-> Equipement Diamant")), new LevelDescription(200, Arrays.asList("Equipement Diamant", "-> Equipement Netherite")), new LevelDescription(200, Arrays.asList("Equipement Netherite", "-> Protection 1")), new LevelDescription(300, Arrays.asList("Protection 1", "-> Protection 2")), new LevelDescription(400, Arrays.asList("Protection 2", "-> Protection 3")), new LevelDescription(500, Arrays.asList("Protection 3", "->Protection 4")), new LevelDescription(200, Arrays.asList("Protection 4", "-> Full Enchant")), new LevelDescription(0, Arrays.asList("Full Enchant"))), "Equipement", Material.LEATHER_CHESTPLATE),
                new ChampionPower(ChampionPowerType.BOW, Arrays.asList(new LevelDescription(50, Arrays.asList("Arc + 2 stacks de Fleches")), new LevelDescription(100, Arrays.asList("Arc + 2 stacks de Fleches", "->Power 1")), new LevelDescription(300, Arrays.asList("Power 1", "-> Power 3 + 1 stack de Flèches")), new LevelDescription(200, Arrays.asList("Power 3 + 1 stack de Flèches", "-> Power 4")), new LevelDescription(500, Arrays.asList("Power 4", "-> Full Enchant")), new LevelDescription(0, Arrays.asList("Full Enchant"))), "Archerie", Material.BOW),
                new ChampionPower(ChampionPowerType.ENDERCHEST_STUFF, Arrays.asList(new LevelDescription(500, Arrays.asList("Recoit les items de", "son Ender Chest"))), "Enderchest Drop", Material.ENDER_CHEST),
                new ChampionPower(ChampionPowerType.NO_SHIELD, Arrays.asList(new LevelDescription(2000, Arrays.asList("Les assaillants perdent", "leur bouclier"))), "No Shield", Material.SHIELD),
                new ChampionPower(ChampionPowerType.EFFECT_STRENGH, Arrays.asList(new LevelDescription(500, Arrays.asList("Effet Force 1")), new LevelDescription(2000, Arrays.asList("Effet Force 1", "-> Effet Force 2")), new LevelDescription(0, Arrays.asList("Effet Force 2"))), "Effet Force", Material.POTION),
                new ChampionPower(ChampionPowerType.EFFECT_RESISTANCE, Arrays.asList(new LevelDescription(500, Arrays.asList("Effet Résistance 1")), new LevelDescription(2000, Arrays.asList("Effet Résistance 1", "->Effet Résistance 2")), new LevelDescription(0, Arrays.asList("Effet Résistance 2"))), "Effet Resistance", Material.POTION),
                new ChampionPower(ChampionPowerType.EFFECT_SPEED, Arrays.asList(new LevelDescription(500, Arrays.asList("Effet Speed 1")), new LevelDescription(1000, Arrays.asList("Effet Speed 1", "->Effet Speed 2")), new LevelDescription(0, Arrays.asList("Effet Speed 2"))), "Effet Vitesse", Material.POTION),
                new ChampionPower(ChampionPowerType.EFFECT_JUMP, Arrays.asList(new LevelDescription(500, Arrays.asList("Effet Jump 1")), new LevelDescription(1000, Arrays.asList("Effet Jump 1", "->Effet Jump 2")), new LevelDescription(0, Arrays.asList("Effet Jump 2"))), "Effet Saut", Material.POTION),
                new ChampionPower(ChampionPowerType.EFFECT_FIRE_RESISTANCE, Arrays.asList(new LevelDescription(2000, Arrays.asList("Effet Résistance au Feu"))), "Effet Resistance au Feu", Material.BLAZE_POWDER),
                new ChampionPower(ChampionPowerType.EFFECT_WATER_BREATHING, Arrays.asList(new LevelDescription(500, Arrays.asList("Effet Respiration"))), "Effet Respiration", Material.CONDUIT),
                new ChampionPower(ChampionPowerType.MOB_FRIEND, Arrays.asList(new LevelDescription(500, Arrays.asList("Les mobs n'attaquent pas", "le capitaine"))), "Ami des Mobs", Material.CREEPER_HEAD),
                new ChampionPower(ChampionPowerType.ENNEMY_DETECTION, Arrays.asList(new LevelDescription(2000, Arrays.asList("Les assaillants à moins", "de 30 blocs sont en", "surbrillance"))), "Detection des assaillants", Material.TARGET),
                new ChampionPower(ChampionPowerType.NO_FALL_DAMAGE, Arrays.asList(new LevelDescription(2000, Arrays.asList("Pas de dégât de chute"))), "Pas de chute", Material.FEATHER),
                new ChampionPower(ChampionPowerType.SPELL_SCREAM, Arrays.asList(new LevelDescription(1000, Arrays.asList("Envoi les ennemis proches", "dans les airs"))), "Sort : Cri d'Effroi", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_SUMMON, Arrays.asList(new LevelDescription(1000, Arrays.asList("Invoque des mobs près", "des assaillants"))), "Sort : Invocation", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_JUMP, Arrays.asList(new LevelDescription(1000, Arrays.asList("Fait un grand saut"))), "Sort : Saut", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_EXPLOSIVE_PUN, Arrays.asList(new LevelDescription(2000, Arrays.asList("Le prochain bloc posé", "par un assaillant explose"))), "Sort : Farce Explosive", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_JAIL, Arrays.asList(new LevelDescription(2000, Arrays.asList("Enferme un assaillant dasn", "une prison d'obsidienne"))), "Sort : Prison", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_ADIOS, Arrays.asList(new LevelDescription(2000, Arrays.asList("Fait disparaitre tous les", "blocs sous les pieds du", "capitaine jusqu'à la couche 0"))), "Sort : Adios!", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.SPELL_SHUFFLE, Arrays.asList(new LevelDescription(1000, Arrays.asList("Intervertit la position", "des assaillants"))), "Sort : Shuffle", Material.WRITABLE_BOOK),
                new ChampionPower(ChampionPowerType.COME_BACK_REVIVE, Arrays.asList(new LevelDescription(2000, Arrays.asList("A 0 cœurs récupère", "3 cœurs + 2 d'abso", "un assaillant allié perd", "tout son équipement"))), "Sauvetage", Material.TOTEM_OF_UNDYING),
                new ChampionPower(ChampionPowerType.COME_BACK_BALANCE, Arrays.asList(new LevelDescription(4000, Arrays.asList("A 5 cœurs les assaillants", "subissent un poison jusqu'à", "5 cœurs max"))), "Equilibre", Material.DAMAGED_ANVIL),
                new ChampionPower(ChampionPowerType.COME_BACK_JUMP_SCARE, Arrays.asList(new LevelDescription(2000, Arrays.asList("A 2 cœurs teleporte", "le capitaine au point le", "plus haut de sa position"))), "Trouille", Material.JACK_O_LANTERN)

                );
    }
}
