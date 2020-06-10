package com.uzm.hylex.core.listeners.bungeeguard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerRestListeners implements Listener {

  /**
   * @author NickUC
   */


  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
    String message = e.getMessage().toLowerCase();
    if (message.contains("bungeeguard") && (message.contains("plugman") || message.contains("system"))) {
      e.setCancelled(true);
      e.getPlayer().sendMessage("§cVocê não pode mexer no controlador de tokens aqui.");
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerKick(PlayerKickEvent e) {
    if (e.getReason().contains("You logged in from another location"))
      e.setCancelled(true);
  }


  @EventHandler(priority = EventPriority.HIGH)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
    Player player = Bukkit.getPlayerExact(e.getName());
    if (player != null)
      e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cDesculpe, mas um jogador com o mesmo nome já está online.");
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(PlayerLoginEvent e) {
    Player player = Bukkit.getPlayerExact(e.getPlayer().getName());
    if (player != null)
      e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cDesculpe, mas um jogador com o mesmo nome já está online.");
  }
}
