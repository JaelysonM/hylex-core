package com.uzm.hylex.core.spigot.particles;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ParticleEffects {

  public static void sendCubeEffect(Location corner1, Location corner2, double particleDistance, Effect effect) {
    List<Location> result = new ArrayList<>();
    World world = corner1.getWorld();
    double minX = Math.min(corner1.getX(), corner2.getX());
    double minY = Math.min(corner1.getY(), corner2.getY());
    double minZ = Math.min(corner1.getZ(), corner2.getZ());
    double maxX = Math.max(corner1.getX(), corner2.getX());
    double maxY = Math.max(corner1.getY(), corner2.getY());
    double maxZ = Math.max(corner1.getZ(), corner2.getZ());
    for (double x = minX; x <= maxX; x += particleDistance) {
      for (double y = minY; y <= maxY; y += particleDistance) {
        for (double z = minZ; z <= maxZ; z += particleDistance) {
          int components = 0;
          if (x == minX || x == maxX)
            components++;
          if (y == minY || y == maxY)
            components++;
          if (z == minZ || z == maxZ)
            components++;
          if (components >= 2) {
            result.add(new Location(world, x, y, z));
          }
        }
      }

    }
    for (Location loc : result)
      loc.getWorld().spigot().playEffect(loc, effect);
  }

}

