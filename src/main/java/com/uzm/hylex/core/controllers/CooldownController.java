package com.uzm.hylex.core.controllers;

import com.uzm.hylex.core.java.util.TimeMethods;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownController {

  private HashMap<String, Long> cooldowns = new HashMap<>();

  public void createCooldown(String key, int seconds) {
    this.cooldowns.put(key, TimeMethods.generateTimeCurrent(TimeUnit.SECONDS, seconds));
  }

  public void deleteCooldown(String key) {
    this.cooldowns.remove(key);
  }

  public long getCooldown(String key) {
    return this.cooldowns.getOrDefault(key, 0L);
  }

  public boolean isInCooldown(String key) {
    return this.getCooldown(key) >= System.currentTimeMillis();
  }

  private static final Map<UUID, CooldownController> CONTROLLER = new HashMap<>();

  public static void removeCooldownController(Player player) {
    CONTROLLER.remove(player.getUniqueId());
  }

  public static CooldownController getCooldownController(Player player) {
    if (!CONTROLLER.containsKey(player.getUniqueId())) {
      CONTROLLER.put(player.getUniqueId(), new CooldownController());
    }

    return CONTROLLER.get(player.getUniqueId());
  }
}
