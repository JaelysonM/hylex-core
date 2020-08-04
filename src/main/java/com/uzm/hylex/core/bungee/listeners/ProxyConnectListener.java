package com.uzm.hylex.core.bungee.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.bungee.commands.ReportCommand;
import com.uzm.hylex.core.bungee.controllers.FakeController;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;


public class ProxyConnectListener implements Listener {

   @EventHandler(priority = EventPriority.HIGHEST)
  public void onConnect(PostLoginEvent evt) {
     ProxiedPlayer player = evt.getPlayer();
     if (HylexPlayer.getByPlayer(player) == null) {
       HylexPlayer hp = HylexPlayer.create(player);
       hp.requestLoad("Global_Profile");
     }
   }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPostLogin(PlayerDisconnectEvent evt) {
    ProxiedPlayer player = evt.getPlayer();
   HylexPlayer hp = HylexPlayer.getByPlayer(player);
    if (hp != null) {
      hp.destroy();
      HylexPlayer.remove(player);
    }

    ReportCommand.DELAY.remove(player.getUniqueId());
  }

  @EventHandler(priority = (byte) 128)
  public void onServerConnected(ServerConnectedEvent evt) {
    ProxiedPlayer player = evt.getPlayer();
    if (FakeController.isFake(player.getName())) {
      // Enviar dados desse jogador que está utilizando Fake para o servidor processar.
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("FAKE");
      out.writeUTF(player.getName());
      out.writeUTF(FakeController.getFake(player.getName()));
      evt.getServer().sendData("hylex-core", out.toByteArray());
    }
  }
}
