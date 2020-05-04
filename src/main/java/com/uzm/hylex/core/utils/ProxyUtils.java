package com.uzm.hylex.core.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import org.bukkit.entity.Player;

public class ProxyUtils {

  public static void connect(HylexPlayer hp, String serverName) {
    Player player = hp.getPlayer();
    hp.save();
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(serverName);
    player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
  }
}

