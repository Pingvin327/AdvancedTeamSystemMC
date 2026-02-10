package org.pingvin.eggCaptureEvent;

import org.bukkit.plugin.java.JavaPlugin;

public final class EggCaptureEvent extends JavaPlugin {
    TeamSystem teamSystem;
    CommandManager commandManager;


    @Override
    public void onEnable() {
        teamSystem = new TeamSystem();
        commandManager = new CommandManager(teamSystem, this);
        commandManager.commandSystem.initialise();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
