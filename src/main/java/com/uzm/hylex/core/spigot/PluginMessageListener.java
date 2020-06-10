package com.uzm.hylex.core.spigot;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.utils.ProxyUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

  @Override
  public void onPluginMessageReceived(String channel, Player receiver, byte[] data) {
    if (channel.equals("hylex-core")) {
      ByteArrayDataInput in = ByteStreams.newDataInput(data);

      String subChannel = in.readUTF();
      if (subChannel.equals("SendPartyMember")) {
        Player target = Bukkit.getPlayerExact(in.readUTF());
        String serverName = in.readUTF();
        String mini = in.readUTF();
        String type = in.readUTF();

        if (target != null) {
          HylexPlayer hp = HylexPlayer.getByPlayer(target);
          if (hp != null) {
            target.sendMessage("§8Sendo enviado para " +mini + "...");
            if (type.equalsIgnoreCase("OUTSIDE"))
              ProxyUtils.connect(hp, serverName, mini);
          }
        }
      }

    }
  }
}
