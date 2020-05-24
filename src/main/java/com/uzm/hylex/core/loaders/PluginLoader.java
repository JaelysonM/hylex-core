package com.uzm.hylex.core.loaders;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.collect.Maps;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.java.util.configuration.ConfigurationCreator;
import com.uzm.hylex.core.java.util.JavaReflections;
import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.listeners.protocol.TabComplete;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.spigot.enums.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PluginLoader {

  private Core core;
  private MinecraftVersion version;
  public HashMap<String, String> permissions = Maps.newHashMap();

  public PluginLoader(Core core) {
    this.core = core;
    version =
      MinecraftVersion.getVersion(Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1));
    Bukkit.getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Indentificamos a versão " + version.toString());

    if (NMS.setupNMS(this.version)) {
      Bukkit.getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Carregado o NMS " + version.toString() + " (NPCs não funcionam na 1.14)");
    }

    NPCLibrary.setupNPCs(Core.getInstance());
    HologramLibrary.setupHolograms(Core.getInstance());
    loadFiles();
    new ServicesLoader(this);
    registerCommandsPermission();
    registerCommands();
    registerProtocolListeners();
  }

  public Core getCore() {
    return this.core;
  }

  public void loadFiles() {
    new ConfigurationCreator(getCore(), "setup", "");
  }

  public MinecraftVersion getSpigotVersion() {
    return this.version;
  }

  public void registerCommands() {
    long registered = 0;
    List<Class<?>> classes = JavaReflections.getClasses("com.uzm.hylex.core.commands", Core.getInstance());

    try {
      for (Class<?> c : classes) {
        Method handshake = JavaReflections.getMethod(c, "getInvoke");
        ArrayList<String> list = (ArrayList<String>) handshake.invoke(null);
        for (String r : list) {
          getCore().getCommand(r).setExecutor((CommandExecutor) c.newInstance());
        }
        registered++;
      }
    } catch (Exception ex) {
      System.err.println("Probally An error occurred while trying to register some commands.");
      ex.printStackTrace();
    }

    Bukkit.getConsoleSender().sendMessage("§b[Hylex - Core] §7We're registered §f(" + registered + "/" + classes.size() + ") §7commands.");
  }

  public void registerProtocolListeners() {
    ProtocolLibrary.getProtocolManager().addPacketListener(new TabComplete());
  }



  public void registerCommandsPermission() {
    permissions.put("group", "hylex.group");

    permissions.put("tp", "hylex.teleport");
    permissions.put("tpall", "hylex.tpall");
    permissions.put("gamemode", "hylex.gamemode");
    permissions.put("gm", "hylex.gamemode");

    permissions.put("fly", "hylex.fly");
    permissions.put("voar", "hylex.fly");
    permissions.put("speed", "hylex.speed");
  }
}
