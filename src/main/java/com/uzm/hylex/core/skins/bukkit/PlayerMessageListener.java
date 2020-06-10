package com.uzm.hylex.core.skins.bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PlayerMessageListener implements PluginMessageListener {

  private Core core;

  public PlayerMessageListener(Core core) {
   this.core=core;
  }

  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] data) {
    if (channel.equals("hylex-core")) {
      ByteArrayDataInput in = ByteStreams.newDataInput(data);
      String subChannel = in.readUTF();
      if (subChannel.equals("SkinUpdate")) {
        core.getFactory().applySkin(player, core.getSkinStorage().createProperty(in.readUTF(), in.readUTF(), in.readUTF()));
        core.getFactory().updateSkin(player);
        System.out.println("chegou");
      }
    }
  }
}
