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

    public void createTeamMC(String name, String color, boolean ffireEnabled, boolean canSeeInv) {

        org.bukkit.scoreboard.Team team = scoreboard.getTeam(name);

        if (team != null) { team.unregister(); };

        team = scoreboard.registerNewTeam(name);

        try {
            team.setColor(ChatColor.getByChar(color));
            team.setAllowFriendlyFire(ffireEnabled);
            team.setCanSeeFriendlyInvisibles(canSeeInv);
            team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        }
        catch (NullPointerException ignored) {}

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

}
