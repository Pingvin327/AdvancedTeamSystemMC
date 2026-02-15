package org.pingvin.AdvancedTeamSystem;

import org.bukkit.plugin.java.JavaPlugin;

public final class AdvancedTeamSystem extends JavaPlugin {
    TeamSystem teamSystem;
    static TeamSystem teamSystemStatic = null;
    CommandManager commandManager;
    ServerEventHandler eventHandler;

    @Override
    public void onEnable() {
        teamSystem = new TeamSystem(this);
        teamSystemStatic = teamSystem;
        commandManager = new CommandManager(teamSystem, this);
        commandManager.commandSystem.initialise();
        eventHandler = new ServerEventHandler(this);
        teamSystem.loadTeams();
    }

    public static TeamSystem getTeamSystem(){
        return teamSystemStatic;
    }

    @Override
    public void onDisable() {
        teamSystem.saveTeams();
    }
}
