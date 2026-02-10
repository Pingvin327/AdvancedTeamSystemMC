package org.pingvin.eggCaptureEvent;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Team {
    TeamSystem teamSystem;
    UUID teamId;
    Boolean isDisplay;
    String displayName;
    String color;
    ArrayList<UUID> players;
    UUID leader;

    public Team(TeamSystem teamSystem) {

        this.teamSystem = teamSystem;
        this.teamId = UUID.randomUUID();
        this.isDisplay = false;
        this.displayName = teamId.toString();
        this.color = "white";
        this.players = new ArrayList<>();
        this.leader = UUID.randomUUID();

    }

    public boolean loadFromString(String string) {

        System.out.println(string);

        String[] stringArray = string.split("\\$");

        if (stringArray.length != 6) { return false; };

        this.teamId = UUID.fromString(stringArray[0]);

        this.isDisplay = Boolean.parseBoolean(stringArray[1]);

        this.displayName = stringArray[2];
        this.color = stringArray[3];

        String[] playerIds = stringArray[4].split("&");

        for (String playerIdStr:playerIds) {
            addPlayer(UUID.fromString(playerIdStr));
        }

        setLeader(UUID.fromString(stringArray[5]));

        return true;
    }

    public String exportToString() {
        StringBuilder output = new StringBuilder();

        output.append(teamId.toString()).append("$");
        output.append(isDisplay.toString());
        output.append(displayName).append("$");
        output.append(color).append("$");

        for (UUID playerId:players) {
            output.append(playerId.toString()).append("&");
        }
        output.append("$");

        output.append(leader.toString());

        return output.toString();
    }

    public boolean addPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        if (players.contains(playerId)) { return false; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerAddMC(this, player); }
        return players.add(playerId);
    }

    public boolean addPlayer(UUID playerId) {
        if (players.contains(playerId)) { return false; };
        if (isDisplay) { teamSystem.minecraftTeamManager.playerAddMC(this, playerId); }
        return players.add(playerId);
    }

    public  boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId());
    }

    public boolean setLeader(Player player) {
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
        this.displayName = displayName;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setIsDisplay(boolean isDisplay){
        this.isDisplay = isDisplay;
    }
}