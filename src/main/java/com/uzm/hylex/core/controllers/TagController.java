package com.uzm.hylex.core.controllers;

import com.google.common.collect.Maps;
import com.uzm.hylex.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class TagController {

    public static HashMap<String, TagController> tags = Maps.newHashMap();

    private String uuid;
    private String prefix = "§7";
    private String suffix = "";
    private String order = "z";
    private Player player;
    private Team team;


    public TagController(String uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(UUID.fromString(uuid));
    }


    public void setPrefix(String p) {
        this.prefix = p;
    }

    public void setSuffix(String s) {
        this.suffix = s;
    }

    public void setOrder(String s) {

        this.order = s;
    }

    public String getSuffix() {return suffix;}

    public String getPrefix() {
        return prefix;
    }

    public String getUUID() {
        return uuid;
    }

    public String getOrder() {
        return order;
    }

    public Player getPlayer() {
        return player;

    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team=team;
    }

    public static void create(Player p) {
        tags.computeIfAbsent(p.getUniqueId().toString(), result -> new TagController(p.getUniqueId().toString()));
    }

    public static void delete(Player p) {
        tags.remove(p.getUniqueId().toString());

    }


    public static TagController get(Player p) {
        return tags.getOrDefault(p.getUniqueId().toString(), null);
    }


    public static void task() {
        new BukkitRunnable() {

            public void run() {
                getDatas().forEach(TagController::update);
            }
        }.runTaskTimerAsynchronously(Core.getInstance(), 20L, 60L);
    }


    public static ArrayList<TagController> getDatas() {
        return new ArrayList<>(tags.values());
    }

    public static Team getTeam(Scoreboard board, String name) {
        if (board.getTeam(name) != null) {
            return board.getTeam(name);
        } else {
            return board.registerNewTeam(name);
        }
    }


    public void update() {
         if (getTeam() == null) {
             Team team = getTeam(player.getScoreboard(), order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15) : player.getUniqueId().toString()));
             team.setPrefix(prefix);
             team.setSuffix(suffix);
             team.addPlayer(player);
             setTeam(team);
             return;
         }

        if (!getTeam().getName().equalsIgnoreCase(order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15) : player.getUniqueId().toString()))) {
            getTeam().unregister();
            Team team = getTeam(player.getScoreboard(), order + (player.getUniqueId().toString().length() > 15 ? player.getUniqueId().toString().substring(0, 15) : player.getUniqueId().toString()));
            setTeam(team);
        }

        getTeam().setPrefix(prefix);
        getTeam().setSuffix(suffix);

            if (!getTeam().hasPlayer(player))
            getTeam().addPlayer(player);


    }
}