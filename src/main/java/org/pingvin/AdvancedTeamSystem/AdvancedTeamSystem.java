package org.pingvin.AdvancedTeamSystem;

import org.bukkit.plugin.java.JavaPlugin;

public final class AdvancedTeamSystem extends JavaPlugin {
    TeamSystem teamSystem;
    static TeamSystem teamSystemStatic = null;
    ATSEventManager eventSender;
    CommandManager commandManager;
    ServerEventHandler eventHandler;

    @Override
    public void onEnable() {
        eventSender = new ATSEventManager();
        teamSystem = new TeamSystem(this, eventSender);
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
