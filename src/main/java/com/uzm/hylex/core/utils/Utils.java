package com.uzm.hylex.core.utils;

import com.uzm.hylex.core.nms.NMS;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.regex.Pattern;

public class Utils {
  private static final Location AT_LOCATION = new Location(null, 0, 0, 0);

  public static String removeNumbers(String string) {
    return string.replaceAll("[0-9]", "");
  }
  private static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_\\-]+$");
  public static boolean validUsername(String username) {
    if (username.length() > 16)
      return false;

    return namePattern.matcher(username).matches();
  }

  public static void faceEntity(Entity entity, Entity at) {
    if (at == null || entity == null || entity.getWorld() != at.getWorld())
      return;
    if (at instanceof LivingEntity) {
      NMS.look(entity, at);
    } else {
      faceLocation(entity, at.getLocation(AT_LOCATION));
    }
  }
  public static void faceLocation(Entity entity, Location to) {
    faceLocation(entity, to, false);
  }

  public static void faceLocation(Entity entity, Location to, boolean headOnly) {
    faceLocation(entity, to, headOnly, true);
  }

  public static void faceLocation(Entity entity, Location to, boolean headOnly, boolean immediate) {
    if (to == null || entity.getWorld() != to.getWorld())
      return;
    NMS.look(entity, to, headOnly, immediate);
  }


  public static float clampYaw(float yaw) {
    while (yaw < -180.0F) {
      yaw += 360.0F;
    }

    while (yaw >= 180.0F) {
      yaw -= 360.0F;
    }
    return yaw;
  }
}
