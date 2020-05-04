package com.uzm.hylex.core.api;

import org.bukkit.entity.Player;

public enum Group {

  HYLEX("§bHylex", "§b[Hylex] ", "*", "a", true),
  GERENTE("§4Gerente", "§4[Gerente] ", "hylex.group.gerente", "b", true),
  ADMIN("§9Admin", "§9[Admin] ", "hylex.group.admin", "c", true),
  DESENVOLVEDOR("§6Desenvolvedor", "§6[Desenvolvedor] ", "hylex.group.dev", "d", true),
  MODERADOR("§2Moderador", "§2[Moderador]", "hylex.group.mod", "e", true),
  AJUDANTE("§eAjudante", "§e[Ajudante] ", "hylex.group.ajd", "f", true),
  PASSE("§3Passe", "§3[Passe] ", "hylex.group.pass", "g"),
  STREAMER("§5Streamer", "§5[Streamer] ", "hylex.group.streamer", "h", true),
  MINIYT("§cMiniYT", "§c[MiniYT] ", "hylex.group.miniyt", "i", true),
  NORMAL("§7Normal", "§7", "hylex.group.normal", "j");

  private String color;
  private String display;
  private String permission;
  private String order;
  private String name;
  private boolean alwaysVisible;

  Group(String name, String display, String permission, String order, boolean visible) {
    this(name, display, permission, order);
    this.alwaysVisible = visible;
  }

  Group(String name, String display, String permission, String order) {
    this.display = display;
    this.order = order;
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
}
