package org.pingvin.AdvancedTeamSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerEventHandler implements Listener {

    AdvancedTeamSystem plugin;

    public ServerEventHandler(AdvancedTeamSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.teamSystem.initialisePlayer(player);
    }

}