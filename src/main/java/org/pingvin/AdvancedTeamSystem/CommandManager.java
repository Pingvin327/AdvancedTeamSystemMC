package org.pingvin.AdvancedTeamSystem;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandManager {
    CommandSystem commandSystem;

    public CommandManager(TeamSystem teamSystem, AdvancedTeamSystem plugin) {
        this.commandSystem = new CommandSystem(teamSystem, plugin);
    }

    private class CommandExecutorMain implements CommandExecutor {

        CommandSystem commandSystem;

        public CommandExecutorMain(CommandSystem commandSystem) { this.commandSystem = commandSystem; };

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
            if (args.length == 0) { return false; };

            if (!commandSender.isOp() && commandSystem.teamSystem.playerAccessLevel == 0) { return false; };

            try {
                boolean result = false;
                switch (args[0]) {

                    case "create":
                        if (args.length != 4) { return false; };
                        result = commandSystem.teamSystem.addTeam(args[1], args[2], Boolean.parseBoolean(args[3]));
                        if (result) { commandSender.sendMessage("Created team " + args[1]); };
                        return result;

                    case "remove":
                        if (args.length != 2) { return false; };
                        Team teamRemove = commandSystem.teamSystem.getTeam(args[1]);
                        if (teamRemove == null) { return false;};
                        result = commandSystem.teamSystem.removeTeam(teamRemove);
                        if (result) { commandSender.sendMessage("Removed team " + args[1]); };
                        return result;

                    case "join":
                        if (args.length != 3) { return false; };
                        Team teamJoin = commandSystem.teamSystem.getTeam(args[1]);
                        if (teamJoin == null) { return false; };
                        Player playerJoin = commandSystem.server.getPlayer(args[2]);
                        result  = commandSystem.teamSystem.playerTeamJoin(teamJoin, playerJoin);
                        if (result) { commandSender.sendMessage("Joined " + args[2] + " to " + args[1]); }
                        return result;

                    case "leave":
                        if (args.length != 3) { return false; };
                        Team teamLeave = commandSystem.teamSystem.getTeam(args[1]);
                        if (teamLeave == null) { return false; };
                        Player playerLeave = commandSystem.server.getPlayer(args[2]);
                        if (playerLeave == null) { return false; };
                        result = commandSystem.teamSystem.playerTeamLeave(teamLeave, playerLeave);
                        if (result) { commandSender.sendMessage("Removed " + args[2] + " from " + args[1]); };
                        return result;

                    case "list":
                        if (args.length == 1) {
                            for (Team team1 : commandSystem.teamSystem.teamList) {
                                StringBuilder output = new StringBuilder();
                                output.append(team1.displayName).append(": ");
                                for (UUID playerId : commandSystem.teamSystem.getAllPlayersByTeam(team1)) {
                                    output.append(playerId.toString());
                                    output.append(" ");
                                }

                                output.append("(");
                                for (Player playerList:commandSystem.teamSystem.getOnlinePlayersByTeam(team1)) {
                                    output.append(playerList.getName());
                                    output.append(" ");
                                }
                                output.append(")");

                                commandSender.sendMessage(output.toString());
                            }
                        }
                        return true;

                    case "limit":
                        if (args.length != 3) { return false; };
                        Team teamLimitSet = commandSystem.teamSystem.getTeam(args[1]);
                        if (teamLimitSet == null) { return false; };
                        int newPlayerLimit = Integer.parseInt(args[2]);
                        result = commandSystem.teamSystem.setTeamLimit(teamLimitSet, newPlayerLimit);
                        if (result) { commandSender.sendMessage("Set the limit for " + args[1] + " to " + newPlayerLimit); };
                        return result;

                    case "save":

                        return commandSystem.teamSystem.saveTeams();

                    case "load":

                        return commandSystem.teamSystem.loadTeams();
                }
            }
            catch (Exception e) {
                return false;
            }


            return false;
        }
    }

    public class CommandSystem {
        TeamSystem teamSystem;
        AdvancedTeamSystem plugin;
        Server server;
        public CommandSystem(TeamSystem teamSystem, AdvancedTeamSystem plugin){
            this.teamSystem = teamSystem;
            this.plugin = plugin;
            this.server = plugin.getServer();
        }

        public void initialise() {
            plugin.getCommand("teamctl").setExecutor(new CommandExecutorMain(this));
        }

    }

}
