package com.uzm.hylex.core.bungee.api;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Group {
  HYLEX("§bHylex", "§b[Hylex] ", "*", 100),
  DESENVOLVEDOR("§6Dev", "§6[Dev] ", "group.dev", 30),
  GERENTE("§4Gerente", "§4[Gerente] ", "group.gerente", 25),
  ADMIN("§9Admin", "§9[Admin] ", "group.admin", 20),
  MODERADOR("§2Moderador", "§2[Moderador] ", "group.mod", 10),
  AJUDANTE("§eAjudante", "§e[Ajudante] ", "group.ajd", 7),
  PASSE("§3Passe", "§3[Passe] ", "group.pass", 30),
  STREAMER("§5Streamer", "§5[Streamer] ", "group.streamer", 25),
  YOUTUBER("§cYoutuber", "§c[Youtuber] ", "group.youtuber", 20),
  MINIYT("§cMiniYT", "§c[MiniYT] ", "group.miniyt", 15),
  EMERALD("§aEmerald", "§a[Emerald] ", "group.emerald", 10),
  DIAMOND("§bDiamond", "§b[Diamond] ", "group.diamond", 7),
  GOLD("§eGold", "§e[Gold] ", "group.gold", 5),
  NORMAL("§7Normal", "§7", "group.normal", 3);

  private String color;
  private String display;
  private String permission;
  private int partySize;
  private String name;

  Group(String name, String display, String permission, int partySize) {
    this.display = display;
    this.partySize = partySize;
    this.permission = permission;
    this.name = name;
    this.color = this.name.substring(0, 2);
  }

  public String getColor() {
    return this.color;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getPermission() {
    return this.permission;
  }

  public int getPartySize() {
    return this.partySize;
  }

  public String getName() {
    return this.name;
  }

  public static String getColored(String player) {
    ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(player);
    if (pp != null) {
      return getPlayerGroup(pp).getColor() + pp.getName();
    }

    return "§7" + player;
  }

  public static String getColored(ProxiedPlayer player) {
    return getPlayerGroup(player).getColor() + player.getName();
  }

  public static Group getPlayerGroup(ProxiedPlayer player) {
    for (Group group : Group.values()) {
      if (player.hasPermission(group.getPermission())) {
        return group;
      }
    }

    return NORMAL;
  }
}
