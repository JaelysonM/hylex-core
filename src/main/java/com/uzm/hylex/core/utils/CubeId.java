package com.uzm.hylex.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;

public class CubeId {
  private String world;
  private int xmax, xmin, ymax, ymin, zmax, zmin;
  private Location location1;
  private Location location2;

  public CubeId(Location l1, Location l2) {
    this.world = l1.getWorld().getName();
    this.xmax = Math.max(l1.getBlockX(), l2.getBlockX());
    this.xmin = Math.min(l1.getBlockX(), l2.getBlockX());
    this.ymax = Math.max(l1.getBlockY(), l2.getBlockY());
    this.ymin = Math.min(l1.getBlockY(), l2.getBlockY());
    this.zmax = Math.max(l1.getBlockZ(), l2.getBlockZ());
    this.zmin = Math.min(l1.getBlockZ(), l2.getBlockZ());

    this.location1 = new Location(l1.getWorld(), xmax, ymax, zmax);
    this.location2 = new Location(l1.getWorld(), xmin, ymin, zmin);
  }

  public CubeId(String serializedCube) {
    String[] split = serializedCube.split("; ");
    this.world = split[0];
    this.xmax = parseInt(split[1]);
    this.xmin = parseInt(split[2]);
    this.ymax = parseInt(split[3]);
    this.ymin = parseInt(split[4]);
    this.zmax = parseInt(split[5]);
    this.zmin = parseInt(split[6]);

    this.location1 = new Location(Bukkit.getWorld( split[0]), xmax, ymax, zmax);
    this.location2 = new Location(Bukkit.getWorld( split[0]), xmin, ymin, zmin);
  }

  public Location getLocation1() {
    return location1;
  }

  public Location getLocation2() {
    return location2;
  }

  public CubeIterator iterator() {
    return new CubeIterator(this);
  }

  public Location getRandomLocation() {
    int x = ThreadLocalRandom.current().nextInt(xmax - xmin) + 1;
    int y = ThreadLocalRandom.current().nextInt(xmax - xmin) + 1;
    int z = ThreadLocalRandom.current().nextInt(xmax - xmin) + 1;
    return new Location(Bukkit.getWorld(world), xmin + x, ymin + y, zmin + z);
  }

  public Location getCenterLocation() {
    double x = xmin + ((xmax + 1) - xmin) / 2.0, z = zmin + ((zmax + 1) - zmin) / 2.0;
    World world = Bukkit.getWorld(this.world);
    int y = this.ymax - 10;
    return new Location(world, x, y, z);
  }

  public boolean contains(Location loc) {
    return loc != null && loc.getWorld().getName().equals(world) && loc.getBlockX() >= xmin && loc.getBlockX() <= xmax && loc.getBlockY() >= ymin && loc.getBlockY() <= ymax
      && loc.getBlockZ() >= zmin && loc.getBlockZ() <= zmax;
  }

  public String getWorld() {
    return world;
  }

  public int getXmin() {
    return xmin;
  }

  public int getXmax() {
    return xmax;
  }

  @Override
  public String toString() {
    return world + "; " + xmax + "; " + xmin + "; " + ymax + "; " + ymin + "; " + zmax + "; " + zmin;
  }

  public void setXmin(int xmin) {
    this.xmin = xmin;
  }

  public int getYmax() {
    return ymax;
  }

  public int getYmin() {
    return ymin;
  }

  public int getZmax() {
    return zmax;
  }

  public int getZmin() {
    return zmin;
  }

  public class CubeIterator implements Iterator<Block> {
    String world;
    CubeId cubeId;
    int baseX, baseY, baseZ, sizeX, sizeY, sizeZ, x, y, z;

    public CubeIterator(CubeId cubeId) {
      x = y = z = 0;
      baseX = getXmin();
      baseY = getYmin();
      baseZ = getZmin();
      this.cubeId = cubeId;
      this.world = cubeId.getWorld();
      sizeX = Math.abs(getXmax() - getXmin()) + 1;
      sizeY = Math.abs(getYmax() - getYmin()) + 1;
      sizeZ = Math.abs(getZmax() - getZmin()) + 1;
    }

    public boolean hasNext() {
      return x < sizeX && y < sizeY && z < sizeZ;
    }

    public Block next() {
      Block block = Bukkit.getWorld(world).getBlockAt(baseX + x, baseY + y, baseZ + z);
      if (++x >= sizeX) {
        x = 0;
        if (++y >= sizeY) {
          y = 0;
          ++z;
        }
      }

      return block;
    }

    public void remove() {
      // Do anything
    }
  }
}
