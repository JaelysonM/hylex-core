package com.uzm.hylex.core.spigot.features;

import com.uzm.hylex.core.nms.NMS;
import org.bukkit.entity.Player;



public class TabColor {

    private Player player;

    private String footer;
    private String bottom;


    public TabColor(Player player) {
        this.player=player;
        this.bottom= "";
        this.footer="";
    }
    public TabColor(Player player, String footer, String bottom) {
        this.player=player;
        this.footer=footer;
        this.bottom=bottom;

    }

    public TabColor setFooter(String footer) {
       this.footer=footer;
       return this;
    }
    public TabColor setBottom(String bottom) {
        this.bottom=bottom;
        return this;
    }


    public TabColor send() {
        NMS.sendTabColor(player, footer,bottom);
        return this;
    }

    public Player getPlayer() {
        return player;
    }

}
