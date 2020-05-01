package com.uzm.hylex.core.spigot.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {


    private String locationString;
    private Location location;

    public LocationSerializer(Location location) {
        this.location = location;
    }

    public LocationSerializer(String locationString) {
        this.locationString = locationString;
    }

    public Location unserialize() {
        String[] sp = locationString.split(" : ");
        Location loc = new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]), Double.parseDouble(sp[3]));
        loc.setPitch(Float.parseFloat(sp[4]));
        loc.setYaw(Float.parseFloat(sp[5]));
        return loc;
    }
    public String serialize() {
        return location.getWorld().getName() + " : " + location.getX() + " : " + location.getY() + " : " + location.getZ() + " : " + location.getPitch() + " : " + location.getYaw();
    }
}
