package com.uzm.hylex.core;

import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.loaders.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

  private static Core core;
  private static String CORE_PATH;
  public static PluginLoader loader;
  public static String SOCKET_NAME;
  public static boolean IS_ARENA_CLIENT;

  public void onEnable() {
    long aux = System.currentTimeMillis();

    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §fessencialmente §7carregado com sucesso.");
    getServer().getConsoleSender().sendMessage("§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthors());

    /*
     * Declations
     */

    core = this;
    CORE_PATH = getFile().getPath();
    loader = new PluginLoader(this);

    getServer().getConsoleSender()
      .sendMessage("§b[Hylex Module: Core] §7Plugin §fdefinitivamente §7carregado com sucesso (§f" + (System.currentTimeMillis() - aux + " milisegundos§7)"));
  }

  public void onDisable() {
    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");

    NPCLibrary.unregisterAll();
    HologramLibrary.unregisterAll();

  }

  public static Core getInstance() {
    return core;
  }

  public static String getPath() {
    return CORE_PATH;
  }

  public static PluginLoader getLoader() {
    return loader;
  }

}
