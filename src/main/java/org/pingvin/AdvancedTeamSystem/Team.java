package org.pingvin.AdvancedTeamSystem;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    TeamSystem teamSystem;
    UUID teamId;
    Boolean isDisplay;
    String displayName;
    String color;
    ArrayList<UUID> players;
    Integer playerLimit;
    UUID leader;

    public Team(TeamSystem teamSystem) {

        this.teamSystem = teamSystem;
        this.teamId = UUID.randomUUID();
        this.isDisplay = false;
        this.displayName = teamId.toString();
        this.color = "white";
        this.players = new ArrayList<>();
        this.playerLimit = Integer.MAX_VALUE;
        this.leader = UUID.randomUUID();

    }

    public void deleteTeam() {
        teamSystem.minecraftTeamManager.removeTeamMC(this);
    }

    public boolean loadFromString(String string) {

        System.out.println(string);

        String[] stringArray = string.split("\\$");

        if (stringArray.length != 7) { return false; };

        this.teamId = UUID.fromString(stringArray[0]);

        this.isDisplay = Boolean.parseBoolean(stringArray[1]);

        this.displayName = stringArray[2];
        this.color = stringArray[3];

        String[] playerIds = stringArray[4].split("&");

        if (isDisplay) {
            teamSystem.minecraftTeamManager.createTeamMC(displayName, color, true, true);
        }

        if (!stringArray[4].equals(" ")) {

            for (String playerIdStr : playerIds) {
                addPlayer(UUID.fromString(playerIdStr));
            }
        }

        setPlayerLimit(Integer.parseInt(stringArray[5]));

        setLeader(UUID.fromString(stringArray[6]));

        return true;
    }

    public String exportToString() {
        StringBuilder output = new StringBuilder();

        output.append(teamId.toString()).append("$");
        output.append(isDisplay.toString()).append("$");
        output.append(displayName).append("$");
        output.append(color).append("$");

        if (players.isEmpty()) { output.append(" "); };
        for (UUID playerId:players) {
            output.append(playerId.toString()).append("&");
        }
        output.append("$");

        output.append(playerLimit.toString());

        output.append("$");

        output.append(leader.toString());

        return output.toString();
    }

    public void playerOnJoinGame(Player player) {
        if (player == null) { return; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerAddMC(this, player); };
    }

    public boolean addPlayer(Player player) {
        if (player == null) { return false; };
        if (players.size() + 1 > playerLimit) { return false; };
        UUID playerId = player.getUniqueId();
        if (players.contains(playerId)) { return false; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerAddMC(this, player); }
        return players.add(playerId);
    }

    public boolean addPlayer(UUID playerId) {
        if (players.contains(playerId)) { return false; };
        if (players.size() + 1 > playerLimit) { return false; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerAddMC(this, playerId); }
        return players.add(playerId);
    }

    public  boolean removePlayer(Player player) {
        if (player == null) { return false; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerRemoveMC(this, player); }
        return players.remove(player.getUniqueId());
    }

    public  boolean removePlayer(UUID player) {
        if (isDisplay) { teamSystem.minecraftTeamManager.playerRemoveMC(this, player); }
        return players.remove(player);
    }

    public boolean setLeader(Player player) {
        if (player == null) { return false; };
        UUID playerId = player.getUniqueId();
        if (!players.contains(playerId)) { return false; };
        leader = playerId;
        return true;
    }

    public boolean setLeader(UUID playerId) {
        if (!players.contains(playerId)) { return false; };
        leader = playerId;
        return true;
    }

    public void setDisplayName(String displayName) {
        if (teamSystem.usedNames.contains(displayName)) { return; };

        teamSystem.minecraftTeamManager.removeTeamMC(this);
        this.displayName = displayName;

        if (isDisplay) {
            teamSystem.minecraftTeamManager.createTeamMC(displayName, color, true, true);
        }
    }

    public void setColor(String color){
        this.color = color;
        teamSystem.minecraftTeamManager.setTeamColorMC(this, color);
    }

    public void setIsDisplay(boolean isDisplay){
        if (this.isDisplay == isDisplay) { return; };

        this.isDisplay = isDisplay;
        if (isDisplay) {
            teamSystem.minecraftTeamManager.createTeamMC(displayName, color, true, true);
        }
        else {
            teamSystem.minecraftTeamManager.removeTeamMC(this);
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public UUID getLeader(){
        return leader;
    }

    public void setPlayerLimit(int playerLimit){
        if (playerLimit <= 0) { return; };
        if (playerLimit >= this.playerLimit) { this.playerLimit = playerLimit; return; };
        if (players.size() - playerLimit <= 0) { this.playerLimit = playerLimit; return; };

        int toRemove = players.size() - playerLimit;

        ArrayList<UUID> playerIdToRemove = new ArrayList<>();

        for (UUID playerId:players) {
            if (toRemove <= 0) { break; }

            if (playerId != leader) {
                playerIdToRemove.add(playerId);
                toRemove -= 1;
            }
        }

        for (UUID playerId:playerIdToRemove) {
            removePlayer(playerId);
        }
        this.playerLimit = playerLimit;
    }
}