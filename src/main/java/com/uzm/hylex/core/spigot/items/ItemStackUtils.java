package com.uzm.hylex.core.spigot.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class ItemStackUtils {
  public static int getAmountOfItem(Material material, Location location) {
    return getAmountOfItem(material, location, 1);
  }

  public static int getAmountOfItem(Material material, Location location, int distance) {
    int amount = 0;
    for (Entity entity : location.getWorld().getEntities()) {
      if (entity instanceof Item) {
        Item item = (Item) entity;
        if (item.getItemStack().getType().equals(material) && item.getLocation().distance(location) <= distance) {
          amount += item.getItemStack().getAmount();
        }
      }
    }
    return amount;
  }
}
