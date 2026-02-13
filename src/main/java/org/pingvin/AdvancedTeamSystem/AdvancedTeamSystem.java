package org.pingvin.AdvancedTeamSystem;

import org.bukkit.plugin.java.JavaPlugin;

public final class AdvancedTeamSystem extends JavaPlugin {
    TeamSystem teamSystem;
    CommandManager commandManager;
    ServerEventHandler eventHandler;

    @Override
    public void onEnable() {
        teamSystem = new TeamSystem(this);
        commandManager = new CommandManager(teamSystem, this);
        commandManager.commandSystem.initialise();
        eventHandler = new ServerEventHandler(this);
        teamSystem.loadTeams();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
