package com.uzm.hylex.core.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.services.lan.WebSocket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.Iterator;

public class ProxyUtils {

  public static void connect(HylexPlayer hp, String serverName) {
    Player player = hp.getPlayer();
    hp.save();
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(serverName);

    player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
  }
  public static void connect(HylexPlayer hp, String serverName, String mini) {
    Player player = hp.getPlayer();
    hp.save();
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(serverName);

    player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    if (serverName.contains("mega")) {
      JSONObject json = new JSONObject();
      json.put("miniName", mini);
      json.put("nickname", hp.getPlayer().getName());
      WebSocket.get("core-" + Core.SOCKET_NAME).getSocket().emit("save-mini", json);

    }

  }
  public static void sendUpdateSettings(HylexPlayer hp) {
    Player player = hp.getPlayer();
    if (hp.isAccountLoaded()) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      JSONObject json = new JSONObject();
      json.put("party", hp.getLobbiesContainer().canSendParty());
      json.put("tell", hp.getLobbiesContainer().canSendTell());
      json.put("report", hp.getLobbiesContainer().canSendReport());
      out.writeUTF("UpdateSettings");
      out.writeUTF(json.toJSONString());

     player.sendPluginMessage(Core.getInstance(), "hylex-core", out.toByteArray());

    }



  }
}

