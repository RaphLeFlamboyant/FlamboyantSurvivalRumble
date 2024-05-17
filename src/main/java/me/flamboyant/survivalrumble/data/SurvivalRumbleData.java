package me.flamboyant.survivalrumble.data;

import me.flamboyant.survivalrumble.data.classes.PlayerClassData;
import me.flamboyant.survivalrumble.playerclass.classobjects.APlayerClass;
import me.flamboyant.survivalrumble.powers.ChampionPowerType;
import me.flamboyant.survivalrumble.utils.ChatColors;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;

public class SurvivalRumbleData {
    public int minutesBeforeEnd = 240;
    protected List<String> teams = new ArrayList<>();
    protected HashMap<UUID, String> playersTeam = new HashMap<>();
    protected Map<String, List<UUID>> playersByTeam = new HashMap<>();
    protected HashMap<String, Integer> teamMoney = new HashMap<>();
    protected HashMap<String, Location> teamHeadquarterLocation = new HashMap<>();
    protected HashMap<String, HashMap<ChampionPowerType, Integer>> teamToChampionPowerLevels = new HashMap<>();
    
    protected HashMap<UUID, PlayerClassType> playersClass = new HashMap<>();
    protected HashMap<PlayerClassType, PlayerClassData> playerClassDataList = new HashMap<>();
    
    protected HashMap<String, UUID> teamChampion = new HashMap<>();

    private static SurvivalRumbleData instance;
    protected SurvivalRumbleData() {
    }

    public static SurvivalRumbleData getSingleton() {
        if (instance == null) {
            instance = new SurvivalRumbleData();
        }

        return instance;
    }

    public void setPlayerClass(Player player, APlayerClass playerClass) {
        playersClass.put(player.getUniqueId(), playerClass.getClassType());
        playerClassDataList.put(playerClass.getClassType(), playerClass.buildClassData());
    }

    public PlayerClassData getPlayerClassData(Player player) {
        return playerClassDataList.get(playersClass.get(player.getUniqueId()));
    }

    public boolean addTeam(String teamName) {
        if (!teams.contains(teamName)) {
            teams.add(teamName);
            playersByTeam.put(teamName, new ArrayList<>());
            teamMoney.put(teamName, 0);
            teamHeadquarterLocation.put(teamName, null);

            HashMap<ChampionPowerType, Integer> championPowerTypeToLevel = new HashMap<>();
            for (ChampionPowerType championPowerType: ChampionPowerType.values()) {
                championPowerTypeToLevel.put(championPowerType, 0);
            }
            teamToChampionPowerLevels.put(teamName, championPowerTypeToLevel);
            return true;
        }

        return false;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeamChampion(String teamName, Player player) {
        teamChampion.put(teamName, player.getUniqueId());
    }

    public Player getTeamChampion(String teamName) {
        return Common.server.getPlayer(teamChampion.get(teamName));
    }

    public int getChampionPowerTypeLevel(String teamName, ChampionPowerType championPowerType) {
        return teamToChampionPowerLevels.get(teamName).get(championPowerType);
    }

    public void setChampionPowerTypeLevel(String teamName, ChampionPowerType championPowerType, int level) {
        teamToChampionPowerLevels.get(teamName).put(championPowerType, level);
    }

    public String getTeamTargetTeam(String attackingTeamName) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).equals(attackingTeamName)) {
                return teams.get((i + 1) % teams.size());
            }
        }

        return null;
    }

    public String getTeamAssaultTeam(String defendingTeamName) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            if (teams.get(i).equals(defendingTeamName)) {
                return teams.get((i == 0 ? teams.size() - 1 : i - 1) % teams.size());
            }
        }

        return null;
    }

    public List<Player> getAttackingPlayers(Player defendingChampion) {
        var teamName = getPlayerTeam(defendingChampion);
        var assaultTeamName = getTeamAssaultTeam(teamName);
        var assaultTeamChampion = getTeamChampion(assaultTeamName);

        return playersByTeam.get(assaultTeamName)
                .stream()
                .map(i -> Common.server.getPlayer(i))
                .filter(p -> p != assaultTeamChampion)
                .collect(Collectors.toList());
    }

    public void removeTeam(String teamName) {
        teams.remove(teamName);
        playersByTeam.remove(teamName);
        teamMoney.remove(teamName);
        teamHeadquarterLocation.remove(teamName);
        teamChampion.remove(teamName);

        for (UUID playerId : playersTeam.keySet().stream().toList()) {
            if (playersTeam.get(playerId).equals(teamName)) {
                playersTeam.remove(playerId);
                PlayerClassType classType = playersClass.get(playerId);
                playersClass.remove(playerId);
                playerClassDataList.remove(classType);
            }
        }
    }

    public void addPlayerTeam(Player player, String teamName) {
        if (playersTeam.containsKey(player.getUniqueId())) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le joueur " + player.getDisplayName() + " est déjà dans l'équipe " + playersTeam.get(player.getUniqueId())));
            Bukkit.getLogger().warning("Tempted to add player " + player.getDisplayName() + " to the team " + teamName + " but is already in the team " + playersTeam.get(player.getUniqueId()));
            return;
        }

        playersTeam.put(player.getUniqueId(), teamName);
        playersByTeam.get(teamName).add(player.getUniqueId());
    }

    public String getPlayerTeam(Player player) {
        if (!playersTeam.containsKey(player.getUniqueId())) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le joueur " + player.getDisplayName() + " n'est dans aucune team"));
            Bukkit.getLogger().warning("Tempted to get player " + player.getDisplayName() + " name but he has none");
            return null;
        }

        return playersTeam.get(player.getUniqueId());
    }

    public List<Player> getPlayers(String teamName) {
        if (!teams.contains(teamName)) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("La team " + teamName + " n'existe pas"));
            Bukkit.getLogger().warning("Tempted to get team " + teamName + " but it doesn't exist");
            return null;
        }

        return playersByTeam.get(teamName).stream().map(i -> Common.server.getPlayer(i)).collect(Collectors.toList());
    }

    public int getMoney(String teamName) {
        if (!teams.contains(teamName)) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le compte de la team " + teamName + " n'existe pas"));
            Bukkit.getLogger().warning("Tempted to get the money of the team " + teamName + " but it doesn't exist");
            return -1;
        }

        return teamMoney.get(teamName);
    }

    public void addMoney(String teamName, int amount) {
        if (!teams.contains(teamName)) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le compte de la team " + teamName + " n'existe pas"));
            Bukkit.getLogger().warning("Tempted to add to the money of the team " + teamName + " but it doesn't exist");
            return;
        }

        teamMoney.put(teamName, amount + teamMoney.get(teamName));
    }

    public Location getHeadquarterLocation(String teamName) {
        if (!teams.contains(teamName)) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le HQ de la team " + teamName + " n'existe pas"));
            Bukkit.getLogger().warning("Tempted to get team " + teamName + " headquarter but it doesn't exist");
            return null;
        }

        return teamHeadquarterLocation.get(teamName).clone();
    }

    public void setHeadquarterLocation(String teamName, Location location) {
        if (!teams.contains(teamName)) {
            Bukkit.broadcastMessage(ChatColors.debugMessage("Le HQ de la team " + teamName + " n'existe pas"));
            Bukkit.getLogger().warning("Tempted to set team " + teamName + " headquarter location, but it doesn't exist");
            return;
        }

        teamHeadquarterLocation.put(teamName, location);
    }
}
