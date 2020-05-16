package com.uzm.hylex.core.bungee.listeners;

import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.bungee.commands.ReportCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class ProxyConnectListener implements Listener {

   @EventHandler
  public void onConnect(PostLoginEvent evt) {
     ProxiedPlayer player = evt.getPlayer();
     if (HylexPlayer.getByPlayer(player) == null) {
       HylexPlayer hp = HylexPlayer.create(player);
       hp.requestLoad("Global_Profile", "BedWarsData");
     }
   }

  @EventHandler
  public void onPostLogin(PlayerDisconnectEvent evt) {
    ProxiedPlayer player = evt.getPlayer();
    if (HylexPlayer.getByPlayer(player) != null) {
      HylexPlayer hp = HylexPlayer.remove(player);
      hp.destroy();
    }

    ReportCommand.DELAY.remove(player.getUniqueId());
  }
}
