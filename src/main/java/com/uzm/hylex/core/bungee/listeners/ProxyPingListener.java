package com.uzm.hylex.core.bungee.listeners;

import com.uzm.hylex.core.bungee.controllers.MotdController;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class ProxyPingListener implements Listener {

  @EventHandler(priority = 64)
  public void onProxyPing(ProxyPingEvent event) {
    ServerPing response = event.getResponse();
    if (response != null) {
      ServerPing.Players players = response.getPlayers();
      int onlinePlayers = players.getOnline();
      int maxPlayers = players.getMax();


      if (MotdController.isRealMaxPlayers())
        maxPlayers = MotdController.getMaxPlayers();
      else
        maxPlayers = onlinePlayers + 1;
      response.setDescriptionComponent(
        (BaseComponent) new TextComponent(MotdController.getRandom().replace("<online>", String.valueOf(onlinePlayers)).replace("<max>", String.valueOf(maxPlayers))));

      String address = event.getConnection().getAddress().getAddress().getHostAddress();
      if (MotdController.hasPinged(address)) {
        response.setFavicon("");
      } else {
        MotdController.addPinged(address);
      }

      players.setMax(maxPlayers);
      players.setOnline(onlinePlayers);

    }
  }
}
