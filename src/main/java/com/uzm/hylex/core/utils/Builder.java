package com.uzm.hylex.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Builder extends Thread {
    boolean preview;
    Location loc;
    int sizeX, sizeY, sizeZ;
 
    public Builder( Location loc, boolean preview, int sizeX, int sizeY, int sizeZ) {
        this.preview = preview;
        this.loc = loc;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }
 
    @Override
    public void run() {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    for (int z = 0; z < sizeZ; z++) {
                        Bukkit.getServer().getWorld(loc.clone().getWorld().getName())
                        .getBlockAt(loc.clone().add(x, y, z)).setTypeId(0); //*** This lin/*** This line
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    }
                }
            }

    }

}