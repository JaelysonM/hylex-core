package com.uzm.hylex.core.api;

import org.bukkit.entity.Player;

public enum Group {

  MASTER("master", "§6Master", "§6[Master] ", "*", "a", true),
  DESENVOLVEDOR("developer", "§6Dev", "§6[Dev] ", "group.dev", "b", true),
  GERENTE("manager", "§4Gerente", "§4[Gerente] ", "group.gerente", "c", true),
  ADMIN("administrator", "§9Admin", "§9[Admin] ", "group.admin", "d", true),
  MODERADOR("moderator", "§2Moderador", "§2[Moderador] ", "group.mod", "e", true),
  AJUDANTE("helper", "§eAjudante", "§e[Ajudante] ", "group.ajd", "f", true),
  PASSE("pass", "§3Passe", "§3[Passe] ", "group.pass", "g"),
  STREAMER("streamer", "§5Streamer", "§5[Streamer] ", "streamer", "h", true),
  YOUTUBER("youtuber", "§cYoutuber", "§c[Youtuber] ", "group.youtuber", "i", true),
  MINIYT("miniyt", "§cMiniYT", "§c[MiniYT] ", "group.miniyt", "j", true),
  EMERALD("emerald", "§aEmerald", "§a[Emerald] ", "group.emerald", "k"),
  DIAMOND("diamond", "§bDiamond", "§b[Diamond] ", "group.diamond", "l"),
  GOLD("gold", "§eGold", "§e[Gold] ", "group.gold", "m"),
  NORMAL("default", "§7Normal", "§7", "group.normal", "n");

  private String lpName;
  private String color;
  private String display;
  private String permission;
  private String order;
  private String name;
  private boolean alwaysVisible;

  Group(String lpName, String name, String display, String permission, String order, boolean visible) {
    this(lpName, name, display, permission, order);
    this.alwaysVisible = visible;
  }

  Group(String lpName, String name, String display, String permission, String order) {
    this.lpName = lpName;
    this.display = display;
    this.order = order;
    this.permission = permission;
    this.name = name;
    this.color = this.name.substring(0, 2);
  }

  public String getLpName() {
    return this.lpName;
  }

  public String getColor() {
    return this.color;
  }

  public String getDisplay() {
    return this.display;
  }

  public boolean isAlwaysVisible() {
    return this.alwaysVisible;
  }

  public String getPermission() {
    return this.permission;
  }

  public String getOrder() {
    return this.order;
  }

  public String getName() {
    return this.name;
  }

  public static String getColored(Player player) {
    String color = "§7";

    HylexPlayer hp = HylexPlayer.getByPlayer(player);
    if (hp != null) {
      color = hp.getGroup().getColor();
    }

    return color + player.getName();
  }

  public static Group getPlayerGroup(Player player) {
    for (Group group : Group.values()) {
      if (player.hasPermission(group.getPermission())) {
        return group;
      }
    }

    return NORMAL;
  }

  public static Group getGroup(String name) {
    for (Group group : values()) {
      if (group.name().equalsIgnoreCase(name)) {
        return group;
      }
    }

    return null;
  }
}
