package com.uzm.hylex.core.bungee.controllers;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.bungee.configuration.ConfigurationCreator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MotdController {


  public static void load() {
    Configuration config = ConfigurationCreator.find("setup.yml").get();
    motds.addAll(config.getStringList("motds"));
    realMaxPlayers = config.getBoolean("isRealMaxPlayers");
    if (!realMaxPlayers)
      maxPlayers = config.getInt("MaxPlayers");

  }

  public static List<String> motds = Lists.newArrayList();
  public static boolean realMaxPlayers = false;
  public static List<String> pinged = Lists.newArrayList();
  public static int maxPlayers;


  public static void addMotd(String motd) {
    if (motds.contains(motd)) {
      motds.add(motd);
    }
  }

  public static void setRealMaxPlayers(boolean realMaxPlayers) {
    MotdController.realMaxPlayers = realMaxPlayers;
  }

  public static int getMaxPlayers() {
    return maxPlayers;
  }

  public static List<String> getMotds() {
    return motds;
  }

  public static boolean isRealMaxPlayers() {
    return realMaxPlayers;
  }

  public static List<String> getPinged() {
    return pinged;
  }

  public static void addPinged(String name) {
    if (!pinged.contains(name))
      pinged.add(name);
  }

  public static void destroy() {
    pinged.clear();
    pinged = null;
    pinged = new ArrayList<>();
  }

  public static boolean hasPinged(String name) {
    return pinged.contains(name);
  }

  public static String getRandom() {
    return motds.isEmpty() ? "§b§lHYLEX §eVenha jogar conosco." : ChatColor.translateAlternateColorCodes('&', motds.get(ThreadLocalRandom.current().nextInt(motds.size())).replace("<newline>", "\n"));
  }
}
