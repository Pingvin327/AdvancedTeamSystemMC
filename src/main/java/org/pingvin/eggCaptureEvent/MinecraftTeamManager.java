package org.pingvin.eggCaptureEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class MinecraftTeamManager {

    EggCaptureEvent plugin;
    Scoreboard scoreboard;

    public MinecraftTeamManager(EggCaptureEvent plugin) {
        this.plugin = plugin;
        this.scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
    }


    public void flushTeams() {
        for (org.bukkit.scoreboard.Team team: scoreboard.getTeams()){
            team.unregister();
        }
    }

    public void createTeamMC(String name, String color, boolean ffireEnabled, boolean canSeeInv) {

        org.bukkit.scoreboard.Team team = scoreboard.getTeam(name);

        if (team != null) { team.unregister(); };

        team = scoreboard.registerNewTeam(name);

        try {
            team.setColor(getColorFromString(color));
            team.setAllowFriendlyFire(ffireEnabled);
            team.setCanSeeFriendlyInvisibles(canSeeInv);
            team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        }
        catch (NullPointerException ignored) {}
    }

    public void removeTeamMC(Team team) {
        org.bukkit.scoreboard.Team teamDel = scoreboard.getTeam(team.displayName);
        if (teamDel != null) { teamDel.unregister(); };
    }

    public void playerAddMC(Team team, Player player) {
        org.bukkit.scoreboard.Team teamMc = scoreboard.getTeam(team.displayName);
        if (teamMc == null) { return; };
        teamMc.addEntity(player);
    }
    public void playerAddMC(Team team, UUID player) {
        org.bukkit.scoreboard.Team teamMc = scoreboard.getTeam(team.displayName);
        if (teamMc == null) { return; };
        Player player1 = plugin.getServer().getPlayer(player);
        if (player1 != null) {
            teamMc.addEntity(player1);
        }
    }

    public void playerRemoveMC(Team team, Player player) {
        org.bukkit.scoreboard.Team teamMc = scoreboard.getTeam(team.displayName);
        if (teamMc == null) { return; };
        teamMc.removeEntity(player);
    }

    public void playerRemoveMC(Team team, UUID player) {
        org.bukkit.scoreboard.Team teamMc = scoreboard.getTeam(team.displayName);
        if (teamMc == null) { return; };
        Player player1 = plugin.getServer().getPlayer(player);
        if (player1 != null) {
            teamMc.removeEntity(player1);
        }
    }

    public void setTeamColorMC(Team team, String color) {
        org.bukkit.scoreboard.Team teamMc = scoreboard.getTeam(team.displayName);
        if (teamMc == null) { return; };
        teamMc.setColor(getColorFromString(color));

    }

    public ChatColor getColorFromString(String colorStr) {
        return switch (colorStr) {
            case "aqua" -> ChatColor.AQUA;
            case "black" -> ChatColor.BLACK;
            case "blue" -> ChatColor.BLUE;
            case "bold" -> ChatColor.BOLD;
            case "dark_aqua" -> ChatColor.DARK_AQUA;
            case "dark_blue" -> ChatColor.DARK_BLUE;
            case "dark_gray" -> ChatColor.DARK_GRAY;
            case "dark_green" -> ChatColor.DARK_GREEN;
            case "dark_purple" -> ChatColor.DARK_PURPLE;
            case "dark_red" -> ChatColor.DARK_RED;
            case "gold" -> ChatColor.GOLD;
            case "gray" -> ChatColor.GRAY;
            case "green" -> ChatColor.GREEN;
            case "italic" -> ChatColor.ITALIC;
            case "light_purple" -> ChatColor.LIGHT_PURPLE;
            case "magic" -> ChatColor.MAGIC;
            case "red" -> ChatColor.RED;
            case "reset" -> ChatColor.RESET;
            case "strikethrough" -> ChatColor.STRIKETHROUGH;
            case "underline" -> ChatColor.UNDERLINE;
            case "white" -> ChatColor.WHITE;
            case "yellow" -> ChatColor.YELLOW;
            case null, default -> ChatColor.GOLD;
        };
    }


}
