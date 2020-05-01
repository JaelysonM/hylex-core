package com.uzm.hylex.core.libraries.holograms.api;

import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.nms.interfaces.IArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * @author Maxter
 */
public class HologramLine {

  private Location location;
  private IArmorStand armor;
  private String line;
  private Hologram hologram;

  public HologramLine(Hologram hologram, Location location, String line) {
    this.line = ChatColor.translateAlternateColorCodes('&',line);
    this.location = location;
    this.armor = null;
    this.hologram = hologram;
  }

  public void spawn() {
    if (this.armor == null) {
      this.armor = NMS.createArmorStand(location, line, this);
    }
  }

  public void despawn() {
    if (this.armor != null) {
      this.armor.killEntity();
      this.armor = null;
    }
  }

  public void setLocation(Location location) {
    if (this.armor != null) {
      this.armor.setLocation(location.getX(), location.getY(), location.getZ());
    }
  }

  public void setLine(String line) {
    if (this.line.equals(ChatColor.translateAlternateColorCodes('&',line))) {
      this.armor.setName(this.line );
      return;
    }
    
    this.line = ChatColor.translateAlternateColorCodes('&',line);
    if (armor == null) {
      if (hologram.isSpawned()) {
        this.spawn();
      }

      return;
    }

    this.armor.setName(this.line);
  }

  public Location getLocation() {
    return this.location;
  }

  public IArmorStand getArmor() {
    return this.armor;
  }

  public String getLine() {
    return this.line;
  }

  public Hologram getHologram() {
    return this.hologram;
  }
}
