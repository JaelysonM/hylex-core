package com.uzm.hylex.core.nms.interfaces;

import com.uzm.hylex.core.libraries.holograms.api.HologramLine;
import org.bukkit.entity.ArmorStand;

/**
 * @author Maxter
 */
public interface IArmorStand {
  
  int getId();

  void setName(String name);

  void setLocation(double x, double y, double z);

  boolean isDead();

  void killEntity();

  ArmorStand getEntity();

  HologramLine getLine();
}