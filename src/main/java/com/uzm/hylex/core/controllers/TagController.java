package com.uzm.hylex.core.controllers;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TagController {

  private String uuid;
  private String prefix = "§7";
  private String suffix = "";
  private String order = "z";
  private Player player;
  private Team team;

  public TagController(String uuid) {
    this.uuid = uuid;
    this.player = Bukkit.getPlayer(UUID.fromString(uuid));
  }

  public void update() {
    if (player == null)
      return;
    if (this.team == null) {
      Team team = getTeam(this.player.getScoreboard(), this.order);
      team.setPrefix(prefix);
      team.setSuffix(suffix);
      team.addPlayer(player);
      setTeam(team);
      return;
    }

    if (!this.team.getName().equalsIgnoreCase(this.order)) {
      if (!team.getName().contains("mini")) {
        this.team.removePlayer(player);
      }

      setTeam(getTeam(this.player.getScoreboard(), this.order));
    }

    this.team.setPrefix(prefix);
    this.team.setSuffix(suffix);
    if (!this.team.hasPlayer(player)) {
      this.team.addPlayer(player);
    }
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public Team getTeam() {
    return this.team;
  }

  public String getSuffix() {
    return this.suffix;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getUUID() {
    return this.uuid;
  }

  public String getOrder() {
    return this.order;
  }

  public Player getPlayer() {
    return this.player;
  }

  private static final Map<String, TagController> TAGS = new HashMap<>();

  public static TagController create(Player player) {
    TAGS.computeIfAbsent(player.getUniqueId().toString(), result -> new TagController(player.getUniqueId().toString()));
    return get(player);
  }

  public static TagController remove(Player player) {
    return TAGS.remove(player.getUniqueId().toString());
  }

  public static TagController get(Player player) {
    return TAGS.getOrDefault(player.getUniqueId().toString(), null);
  }

  public static List<TagController> getControllers() {
    return ImmutableList.copyOf(TAGS.values());
  }

  public static Team getTeam(Scoreboard board, String name) {
    if (board.getTeam(name) != null) {
      return board.getTeam(name);
    } else {
      return board.registerNewTeam(name);
    }
  }
}
