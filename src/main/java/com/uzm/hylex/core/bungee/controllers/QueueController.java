package com.uzm.hylex.core.bungee.controllers;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.services.lan.WebSocket;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QueueController {
  public static List<ServerInfo> QUEUE;

  public static HashMap<String, Long> IS_RESTARTING;

  static {
    QUEUE = Lists.newArrayList();
    IS_RESTARTING = new HashMap<>();
  }


  public static HashMap<String, Long> isRestarting() {
    return IS_RESTARTING;
  }

  public static List<ServerInfo> getQueue() {
    return QUEUE;
  }

  public static void addToQueue(ServerInfo serverInfo) {
    if(!QUEUE.contains(serverInfo) )
      QUEUE.add(serverInfo);
  }
  public static void removeFromQueue(ServerInfo serverInfo) {
    QUEUE.remove(serverInfo);
  }

  public static void run() {
    Bungee.getInstance().getProxy().getScheduler().schedule(Bungee.getInstance(), () -> {
      if (getQueue().size() > 0) {
           if (isRestarting().size() == 0 || isRestarting().values().stream().filter(longV -> longV >= System.currentTimeMillis()).count() >= 1) {
             ServerInfo server = getQueue().get(0);
             removeFromQueue(server);
             isRestarting().put(server.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
             Bungee.getInstance().getLogger().info("§b[Hylex Module: Core] §7Sending a permission to " + server.getName() + " to restarting");
             JSONObject json = new JSONObject();
             json.put("clientName", "core-bedwars-" + server.getName());
             WebSocket.get("core-bungee").getSocket().emit("send-restart-server", json);
           }
      }
    }, 1, 5, TimeUnit.SECONDS);
  }
}
