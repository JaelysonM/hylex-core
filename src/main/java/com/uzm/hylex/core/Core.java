package com.uzm.hylex.core;

import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.listeners.PlayerChatListener;
import com.uzm.hylex.core.loaders.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

  private static Core core;
  public static PluginLoader loader;
  public static String SOCKET_NAME;
  public static boolean IS_ARENA_CLIENT;

  public void onEnable() {
    long aux = System.currentTimeMillis();

    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §fessencialmente §7carregado com sucesso.");
    getServer().getConsoleSender().sendMessage("§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthors());

    /*
     * Declarations
     */

    core = this;
    loader = new PluginLoader(this);

    getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);

    Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


    getServer().getConsoleSender()
      .sendMessage("§b[Hylex Module: Core] §7Plugin §fdefinitivamente §7carregado com sucesso (§f" + (System.currentTimeMillis() - aux + " milisegundos§7)"));
  }

  public void onDisable() {
    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");

    HylexPlayer.listPlayers().forEach(HylexPlayer::save);
  }

  public static Core getInstance() {
    return core;
  }

  public static PluginLoader getLoader() {
    return loader;
  }
}
