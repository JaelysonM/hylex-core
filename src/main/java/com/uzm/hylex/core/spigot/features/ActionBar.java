package com.uzm.hylex.core.spigot.features;

import com.uzm.hylex.core.nms.NMS;
import org.bukkit.entity.Player;

public class ActionBar {

  private String message;

  private Player player;

  public ActionBar(Player player) {
    this.player = player;
  }

  public ActionBar(Player player, String message) {
    this.player = player;
    this.message = message;
  }

  public ActionBar setMessage(String message) {
    this.message = message;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Player getPlayer() {
    return player;
  }

  public ActionBar send() {
    NMS.sendActionBar(player, message);
    destroy();
    return this;
  }

  public void destroy() {
    this.message = null;
    this.player = null;
  }


}
