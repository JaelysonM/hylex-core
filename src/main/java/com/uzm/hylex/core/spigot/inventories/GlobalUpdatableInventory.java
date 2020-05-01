package com.uzm.hylex.core.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GlobalUpdatableInventory extends Menu implements Listener {


  public GlobalUpdatableInventory(String name) {
    this(name, 21);
  }

  public GlobalUpdatableInventory(String name, int rows) {
    super(name, rows);
  }

  public void register(JavaPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
   
  }
  public void open(Player player) {
	  player.openInventory(getInventory());
  }


  public abstract void update();
}