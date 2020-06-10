package com.uzm.hylex.core.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.spigot.features.ActionBar;
import com.uzm.hylex.core.utils.ProxyUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServerQueueController {

  private static HashMap<String, ServerQueueController> QUEUES = Maps.newHashMap();

  private List<Player> QUEUED_PLAYERS;

  private String name;

  private String address;


  private String server;

  private int maxPlayers;

  private String priorityPermission;

  public ServerQueueController(String name) {
    this.name = name;
    this.QUEUED_PLAYERS = Lists.newArrayList();
  }


  public boolean add(Player player) {
    if (!getQueue().contains(player)) {
      if (player.hasPermission(getPriorityPermission())) {
        int indexQueue = getQueue().stream().filter(pls -> !pls.hasPermission(getPriorityPermission())).map(pls -> getQueue().indexOf(pls)).min(Integer::compare).orElse(0);
        new ActionBar(player).setMessage("§bVocê furou a fila em entrou em §f#" + (indexQueue + 1) + " de §f" + getQueue().size()).send();
        getQueue().add(indexQueue, player);
        return true;
      } else {
        this.getQueue().add(player);
        return true;
      }
    } else {
      return false;
    }
  }

  public void setPriorityPermission(String priorityPermission) {
    this.priorityPermission = priorityPermission;
  }


  public String getPriorityPermission() {
    return priorityPermission;
  }

  public String getAddress() {
    return address;
  }


  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getServer() {
    return server;
  }

  public String getName() {
    return name;
  }

  public List<Player> getQueue() {
    return QUEUED_PLAYERS;
  }

  public void setQueue(List<Player> q) {
    QUEUED_PLAYERS = q;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public static void create(String name) {
    QUEUES.computeIfAbsent(name, map -> new ServerQueueController(name));
  }

  public static ServerQueueController get(String name) {
    return QUEUES.getOrDefault(name, null);
  }


  public static void task() {

    new BukkitRunnable() {
      @Override
      public void run() {
        getQUEUES().values().forEach(q -> {
          q.setQueue(q.getQueue().stream().filter(OfflinePlayer::isOnline).collect(Collectors.toList()));

          if (!q.getQueue().isEmpty()) {
            q.getQueue().forEach(result -> {
              new ActionBar(result).setMessage("§eVocê está em §f" + (q.getQueue().indexOf(result) + 1) + "º lugar §ena fila.").send();
            });

            boolean connected = false;
            int players = 0;
            try {
              Socket socket = new Socket(q.getAddress().split(":")[0], Integer.parseInt(q.getAddress().split(":")[1]));
              connected = socket.isConnected();
              DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
              DataInputStream dis = new DataInputStream(socket.getInputStream());
              dos.write(0xFE);
              String data = String.valueOf(dis.read());
              String[] newData = data.split("\u0000");

              players = Integer.parseInt(newData[4]);
              socket.close();

            } catch (Exception ignored) {}
            if (connected) {
              if (players <= q.getMaxPlayers()) {
                if (HylexPlayer.getByPlayer(q.getQueue().get(0)) != null) {
                  ProxyUtils.connect(HylexPlayer.getByPlayer(q.getQueue().get(0)), q.getServer());
                  q.getQueue().get(0).sendMessage("§eSendo enviado para " + q.getServer() + "...");
                  q.getQueue().remove(q.getQueue().get(0));
                } else {
                  q.getQueue().remove(q.getQueue().get(0));
                }
              }

            }

          }
        });
      }
    }.runTaskTimerAsynchronously(Core.getInstance(), 0, 20);

  }


  public static HashMap<String, ServerQueueController> getQUEUES() {
    return QUEUES;
  }
}
