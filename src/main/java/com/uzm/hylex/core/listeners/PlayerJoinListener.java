package com.uzm.hylex.core.listeners;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.controllers.FakeController;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(PlayerJoinEvent evt) {
    System.out.println(NPCLibrary.listNPCS().size());
    if (!evt.getPlayer().hasPermission("hylex.staff")) {
      if (Core.SOCKET_NAME.contains("mega")) {
        return;
      }
      for (UUID players : HylexPlayer.getInvisible()) {
        if (Bukkit.getPlayer(players) != null) {
          evt.getPlayer().hidePlayer(Bukkit.getPlayer(players));
        }
      }
    }

  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(PlayerQuitEvent evt) {
    FakeController.remove(evt.getPlayer());
  }
}


