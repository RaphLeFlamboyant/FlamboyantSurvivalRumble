package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.AssassinClassData;
import me.flamboyant.survivalrumble.data.classes.ElectricianClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ChatUtils;
import me.flamboyant.survivalrumble.utils.Common;
import me.flamboyant.survivalrumble.utils.ScoreType;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AssassinClass extends ANonVanillaClass implements Listener {
    private static final int overworldNameLength = UsefulConstants.overworldName.length();
    private static final int netherWorldNameLength = UsefulConstants.netherWorldName.length();
    private static final int endWorldNameLength = UsefulConstants.endWorldName.length();
    private Player targetPlayer;
    private LocalTime nextAvailableContrat;
    private BukkitTask warnPlayersTask;
    private BukkitTask cancelContractTask;
    private Location overworldLocationBeforePortal;
    private Location netherLocationBeforePortal;
    private ItemStack currentContractItem;
    private AssassinClassData classData;

    public AssassinClass(Player owner) {
        super(owner);
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.ASSASSIN;
    }

    @Override
    public PlayerClassData buildClassData() { return new AssassinClassData(); }

    @Override
    protected String getClassDescriptionCorpus() {
        return "Utiliser une feuille de papier te donne un contrat sur un joueur au hasard. "
                + "Tuer le joueur sous contrat te rapporte 500 points. "
                + "Quand un joueur est sous contrat, utiliser une boussole te permet de connaitre sa position. "
                + "Au bout de 15 minutes de jeu tout le monde sait que le joueur cibl?? est sous contrat. "
                + "Au bout de 60 minutes de jeu le contrat est annul??."
                + "Il est possible d'annuler un contrat en faisant un click droit dessus, mais tu ne peux plus lancer de contrat pendant les prochaines 30 minutes.";
    }

    @Override
    public int getScoreMalus() {
        return -3000;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (AssassinClassData) data().playerClassDataList.get(getClassType());
        System.out.println("Assassin enabling with target id = " + classData.targetPlayerId);
        if (classData.targetPlayerId != null) {
            launchContract();
        }

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (event.getPlayer() != owner) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem().getType() == Material.COMPASS) {
            handleCompass(event.getItem());
        }

        if (event.getItem().getType() == Material.PAPER) {
            handleContract(event.getItem());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (event.getPlayer() == owner && currentContractItem != null)
            event.getPlayer().getInventory().setItem(0, currentContractItem);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (event.getEntity() != targetPlayer) return;
        if (event.getEntity().getKiller() != owner) return;

        owner.sendMessage(ChatUtils.feedback("Le contrat a ??t?? honnor??"));
        targetPlayer = null;
        nextAvailableContrat = LocalTime.now().plusMinutes(1);
        owner.getInventory().remove(currentContractItem);
        currentContractItem = null;
        if (warnPlayersTask != null) Bukkit.getScheduler().cancelTask(warnPlayersTask.getTaskId());
        if (cancelContractTask != null) Bukkit.getScheduler().cancelTask(cancelContractTask.getTaskId());

        GameManager.getInstance().addScore(data().playersTeam.get(owner.getUniqueId()), 500, ScoreType.FLAT);
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        if (event.getEntity() != targetPlayer) return;
        if (event.getLocation().getWorld().getName().equals("world"))
            overworldLocationBeforePortal = event.getLocation();
        if (event.getLocation().getWorld().getName().equals("world_nether"))
            netherLocationBeforePortal = event.getLocation();
    }

    private void handleContract(ItemStack paper) {
        boolean isContract = currentContractItem != null && paper.getItemMeta().getDisplayName().equals(currentContractItem.getItemMeta().getDisplayName());
        if (targetPlayer == null && isContract) return;
        if (targetPlayer != null && !isContract) return;

        if (isContract) {
            owner.sendMessage(ChatUtils.personalAnnouncement("Le contrat est annul??", "Le contrat sur " + targetPlayer.getDisplayName() + " est annul??. Tu ne peux pas relancer de contrat pendant 30 minutes."));
            targetPlayer = null;
            classData.targetPlayerId = null;
            nextAvailableContrat = LocalTime.now().plusMinutes(30);
            owner.getInventory().remove(currentContractItem);
            currentContractItem = null;
            if (warnPlayersTask != null) Bukkit.getScheduler().cancelTask(warnPlayersTask.getTaskId());
            if (cancelContractTask != null) Bukkit.getScheduler().cancelTask(cancelContractTask.getTaskId());
        }
        else {
            if (nextAvailableContrat != null && LocalTime.now().compareTo(nextAvailableContrat) < 0) {
                Long diff = ChronoUnit.MINUTES.between(LocalTime.now(), nextAvailableContrat) + 1;
                owner.sendMessage(ChatUtils.feedback("Tu dois encore attendre " + diff + " minutes avant de pouvoir relancer un contrat"));
                return;
            }
            String selectedTeam = data().teams.stream().filter(t -> !t.equals(data().playersTeam.get(owner.getUniqueId()))).collect(Collectors.toList()).get(Common.rng.nextInt(data().teams.size() - 1));
            List<UUID> playersInTeam = data().playersByTeam.get(selectedTeam);
            classData.targetPlayerId = playersInTeam.get(Common.rng.nextInt(playersInTeam.size()));
            owner.getInventory().remove(paper);
            launchContract();
        }
    }

    private void launchContract() {
        ItemStack contract = new ItemStack(Material.PAPER);
        ItemMeta data = contract.getItemMeta();
        targetPlayer = Common.server.getPlayer(classData.targetPlayerId);
        data.setDisplayName("Contrat sur " + targetPlayer.getDisplayName());
        data.setLore(Arrays.asList("Tu dois tuer le joueur " + targetPlayer.getDisplayName() + " pour remporter 500 points."));
        contract.setItemMeta(data);
        owner.getInventory().addItem(contract);
        this.currentContractItem = contract;

        owner.sendMessage(ChatUtils.personalAnnouncement("Nouveau contrat",
                "Un nouveau contrat est ??tabli sur la t??te de " + targetPlayer.getDisplayName()));

        warnPlayersTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> warnPlayers(), 20L * 60 * 15);
        cancelContractTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> cancelContract(), 20L * 60 * 60);
    }

    private void warnPlayers() {
        Bukkit.broadcastMessage(ChatUtils.generalAnnouncement("La t??te de " + targetPlayer.getDisplayName() + " est mise ?? prix !",
                "Le contrat actuel de l'Assassin cible actuellement " + targetPlayer.getDisplayName() + ", il gagnera 500 points si il parvient ?? le tuer dans les prochaines 45 minutes !"));
        warnPlayersTask = null;
    }

    private void cancelContract() {
        owner.sendMessage(ChatUtils.personalAnnouncement("Le contrat est annul??", "Le contrat sur " + targetPlayer.getDisplayName() + " est annul??."));
        targetPlayer = null;
        classData.targetPlayerId = null;
        cancelContractTask = null;
    }

    private void handleCompass(ItemStack compass) {
        if (targetPlayer == null) return;
        System.out.println("handleCompass");
        Location huntedLocation = targetPlayer.getLocation();
        String huntedWorldName = huntedLocation.getWorld().getName();
        int huntedWorldLength = huntedWorldName.length();
        int ownerWorldLength = owner.getWorld().getName().length();

        if (ownerWorldLength != huntedWorldLength) {
            owner.sendMessage("La cible est dans la dimension " + huntedWorldName);

            if (huntedWorldLength != endWorldNameLength) {
                if (ownerWorldLength == overworldNameLength)
                    huntedLocation = overworldLocationBeforePortal;
                if (ownerWorldLength == netherWorldNameLength)
                    huntedLocation = netherLocationBeforePortal;
            }
        }

        if (ownerWorldLength == netherWorldNameLength && (huntedWorldLength == netherWorldNameLength || netherLocationBeforePortal != null)){
            Location lodeStoneLocation = new Location(huntedLocation.getWorld(), huntedLocation.getBlockX(), 0, huntedLocation.getBlockZ());
            lodeStoneLocation.getBlock().setType(Material.LODESTONE);

            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            compassMeta.setLodestone(lodeStoneLocation);
            compassMeta.setLodestoneTracked(true);
            compass.setItemMeta(compassMeta);
        }
        else {
            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            compassMeta.setLodestone(null);
            compassMeta.setLodestoneTracked(false);
            compass.setItemMeta(compassMeta);
            if (huntedLocation != null) owner.setCompassTarget(huntedLocation);
        }
    }
}
