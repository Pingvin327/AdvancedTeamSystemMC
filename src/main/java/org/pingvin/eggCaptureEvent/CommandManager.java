package org.pingvin.eggCaptureEvent;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandManager {
    CommandSystem commandSystem;

    public CommandManager(TeamSystem teamSystem, EggCaptureEvent plugin) {
        this.commandSystem = new CommandSystem(teamSystem, plugin);
    }

    private class CommandExecutorMain implements CommandExecutor {

        CommandSystem commandSystem;

        public CommandExecutorMain(CommandSystem commandSystem) { this.commandSystem = commandSystem; };

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
            if (args.length == 0) { return false; };

            if (!commandSender.isOp() && commandSystem.teamSystem.playerAccessLevel == 0) { return false; };

            switch (args[0]) {

                case "create":
                    if (args.length != 4) {  return false; }
                    commandSystem.teamSystem.addTeam(args[1], args[2], Boolean.parseBoolean(args[3]));
                    return true;

                case "join":
                    if (args.length != 3) {  return false; }
                    Team team = commandSystem.teamSystem.getTeam(args[1]);
                    if (team == null) { return false; };
                    Player player = commandSystem.server.getPlayer(args[2]);
                    if (player == null) { return false; };
                    team.addPlayer(player);
                    return true;

                case "list":
                    if (args.length == 1) {
                        for (Team team1:commandSystem.teamSystem.teamList) {
                            StringBuilder output = new StringBuilder();
                            output.append(team1.displayName).append(": ");
                            for (UUID playerId:team1.players) {
                                output.append(playerId.toString());
                                output.append(" ");
                            }
                            commandSystem.server.sendPlainMessage(output.toString());
                        }
                    }
                    return true;

                case "save":

                    return commandSystem.teamSystem.saveTeams();

                case "load":

                    return  commandSystem.teamSystem.loadTeams();
            }


            return false;
        }
    }

    public class CommandSystem {
        TeamSystem teamSystem;
        EggCaptureEvent plugin;
        Server server;
        public CommandSystem(TeamSystem teamSystem, EggCaptureEvent plugin){
            this.teamSystem = teamSystem;
            this.plugin = plugin;
            this.server = plugin.getServer();
        }

        public void initialise() {
            plugin.getCommand("eventctl").setExecutor(new CommandExecutorMain(this));
        }

    }

}
