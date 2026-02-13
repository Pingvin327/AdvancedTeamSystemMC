package org.pingvin.AdvancedTeamSystem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;




public class TeamSystem {
    AdvancedTeamSystem plugin;
    MinecraftTeamManager minecraftTeamManager;
    ArrayList<Team> teamList;
    ArrayList<String> usedNames;
    SaveManager saveManager;
    int playerAccessLevel = 0;

    public TeamSystem(AdvancedTeamSystem plugin) {
        this.plugin = plugin;
        minecraftTeamManager = new MinecraftTeamManager(plugin);
        this.teamList = new ArrayList<>();
        this.usedNames = new ArrayList<>();
        this.saveManager = new SaveManager();
    }


    private class SaveManager {

        public boolean loadTeams() {
            File pluginDirectory = new File("plugins/AdvancedTeamSystem");
            pluginDirectory.mkdir();

            File saveTeamFile = new File("plugins/AdvancedTeamSystem/teams-save.txt");

            try (Scanner scanner = new Scanner(saveTeamFile)) {
                while (scanner.hasNextLine()) {
                    String teamString = scanner.nextLine();
                    addTeam(teamString);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean saveTeamsFull() {
            File pluginDirectory = new File("plugins/AdvancedTeamSystem");
            pluginDirectory.mkdir();

            try (FileWriter writer = new FileWriter("plugins/AdvancedTeamSystem/teams-save.txt")) {
                for (Team team:teamList) {
                    writer.write(team.exportToString());
                    writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    public boolean saveTeams() {
        return saveManager.saveTeamsFull();
    }

    public boolean loadTeams() {
        minecraftTeamManager.flushTeams();
        return saveManager.loadTeams();
    }

    public void initialisePlayer(Player player) {
        UUID playerId = player.getUniqueId();

        for (Team team:teamList) {
            if (team.players.contains(playerId)) {
                team.playerOnJoinGame(player);
            }
        }

    }


    public boolean addTeam(String saveTeamString) {
        Team team = new Team(this);
        if (!team.loadFromString(saveTeamString)) { return false; };
        return teamList.add(team);
    }
    public boolean addTeam(String name, String color, boolean isDisplay) {
        Team team = new Team(this);
        if (usedNames.contains(name)) { return false; };
        team.setDisplayName(name);
        team.setColor(color);
        team.setIsDisplay(isDisplay);
        return teamList.add(team);
    }

    public boolean playerTeamLeave(Team team, Player player) {
        return team.removePlayer(player);
    }

    public boolean playerTeamLeave(Team team, UUID player) {
        return team.removePlayer(player);
    }

    public boolean playerTeamJoin(Team team, Player player) {
        if (!team.isDisplay) {
            return team.addPlayer(player);
        }
        else {
            Team teamToLeave = getPlayerDisplayTeam(player);
            if (teamToLeave != null ) { teamToLeave.removePlayer(player); };
            return team.addPlayer(player);
        }
    }

    public boolean playerTeamJoin(Team team, UUID player) {
        if (!team.isDisplay) {
            return team.addPlayer(player);
        }
        else {
            Team teamToLeave = getPlayerDisplayTeam(player);
            if (teamToLeave != null ) { teamToLeave.removePlayer(player); };
            return team.addPlayer(player);
        }
    }


    public boolean removeTeam(String displayName) {
        Team team = getTeam(displayName);
        if (team == null) { return false; };
        team.deleteTeam();
        usedNames.remove(team.displayName);
        teamList.remove(team);
        return true;
    }

    public ArrayList<Player> getTeamPlayers(Team team) {
        ArrayList<Player> players = new ArrayList<>();

        for (UUID player:team.players){
            Entity playerOnline = plugin.getServer().getEntity(player);
            if (plugin.getServer().getEntity(player) != null && playerOnline instanceof Player) {
                players.add((Player) playerOnline);
            }
        }
        return players;
    }

    public Team getPlayerDisplayTeam(Player player) {
        UUID playerId = player.getUniqueId();
        for (Team teamCheck:teamList) {
            if (!teamCheck.isDisplay) { continue; };
            if (teamCheck.players.contains(playerId)) { return teamCheck; };
        }
        return null;
    }

    public Team getPlayerDisplayTeam(UUID playerId) {
        for (Team teamCheck:teamList) {
            if (!teamCheck.isDisplay) { continue; };
            if (teamCheck.players.contains(playerId)) { return teamCheck; };
        }
        return null;
    }


    public Team getTeam(String name) {
        for (Team team:teamList){
            if (team.displayName.equals(name)){
                return team;
            }
        }
        return null;
    }

}
