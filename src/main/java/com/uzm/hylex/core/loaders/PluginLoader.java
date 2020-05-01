package com.uzm.hylex.core.loaders;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.java.util.ConfigurationCreator;
import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.nms.interfaces.INMS;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.spigot.enums.MinecraftVersion;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

public class PluginLoader {

  private Core core;
  private MinecraftVersion version;

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
}
