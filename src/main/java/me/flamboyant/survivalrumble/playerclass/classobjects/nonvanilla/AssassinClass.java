package me.flamboyant.survivalrumble.playerclass.classobjects.nonvanilla;

import me.flamboyant.survivalrumble.GameManager;
import me.flamboyant.survivalrumble.data.PlayerClassType;
import me.flamboyant.survivalrumble.data.classes.AssassinClassData;
import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.survivalrumble.utils.UsefulConstants;
import me.flamboyant.utils.Common;
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
                + "Au bout de 15 minutes de jeu tout le monde sait que le joueur ciblé est sous contrat. "
                + "Au bout de 60 minutes de jeu le contrat est annulé."
                + "Il est possible d'annuler un contrat en faisant un click droit dessus, mais tu ne peux plus lancer de contrat pendant les prochaines 30 minutes.";
    }

    @Override
    public int getScoreMalus() {
        return -3000;
    }

    @Override
    public void enableClass() {
        super.enableClass();
        classData = (AssassinClassData) data().getPlayerClassData(owner);
        System.out.println("Assassin enabling with target id = " + classData.targetPlayerId);
        if (classData.targetPlayerId != null) {
            launchContract();
        }

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    public void disableClass() {
        super.disableClass();

        cancelContract();

        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
        EntityPortalEnterEvent.getHandlerList().unregister(this);
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

        owner.sendMessage(ChatColors.feedback("Le contrat a été honnoré"));
        targetPlayer = null;
        nextAvailableContrat = LocalTime.now().plusMinutes(1);
        owner.getInventory().remove(currentContractItem);
        currentContractItem = null;
        if (warnPlayersTask != null) Bukkit.getScheduler().cancelTask(warnPlayersTask.getTaskId());
        if (cancelContractTask != null) Bukkit.getScheduler().cancelTask(cancelContractTask.getTaskId());

        GameManager.getInstance().addAddMoney(data().getPlayerTeam(owner), 500);
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
            owner.sendMessage(ChatColors.personalAnnouncement("Le contrat est annulé", "Le contrat sur " + targetPlayer.getDisplayName() + " est annulé. Tu ne peux pas relancer de contrat pendant 30 minutes."));
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
                owner.sendMessage(ChatColors.feedback("Tu dois encore attendre " + diff + " minutes avant de pouvoir relancer un contrat"));
                return;
            }
            String selectedTeam = data().getTeams().stream().filter(t -> !t.equals(data().getPlayerTeam(owner))).collect(Collectors.toList()).get(Common.rng.nextInt(data().getTeams().size() - 1));
            List<Player> playersInTeam = data().getPlayers(selectedTeam);
            classData.targetPlayerId = playersInTeam.get(Common.rng.nextInt(playersInTeam.size())).getUniqueId();
            paper.setAmount(paper.getAmount() - 1);
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

        owner.sendMessage(ChatColors.personalAnnouncement("Nouveau contrat",
                "Un nouveau contrat est établi sur la tête de " + targetPlayer.getDisplayName()));

        warnPlayersTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> warnPlayers(), 20L * 60 * 15);
        cancelContractTask = Bukkit.getScheduler().runTaskLater(Common.plugin, () -> cancelContract(), 20L * 60 * 60);
    }

    private void warnPlayers() {
        Bukkit.broadcastMessage(ChatColors.generalAnnouncement("La tête de " + targetPlayer.getDisplayName() + " est mise à prix !",
                "Le contrat actuel de l'Assassin cible actuellement " + targetPlayer.getDisplayName() + ", il gagnera 500 points si il parvient à le tuer dans les prochaines 45 minutes !"));
        warnPlayersTask = null;
    }

    private void cancelContract() {
        if (targetPlayer != null)
            owner.sendMessage(ChatColors.personalAnnouncement("Le contrat est annulé", "Le contrat sur " + targetPlayer.getDisplayName() + " est annulé."));
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
