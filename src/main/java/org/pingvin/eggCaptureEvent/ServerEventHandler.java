package org.pingvin.eggCaptureEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerEventHandler implements Listener {

    EggCaptureEvent plugin;

    public ServerEventHandler(EggCaptureEvent plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.teamSystem.initialisePlayer(player);
    }

}