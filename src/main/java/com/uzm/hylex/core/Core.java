package com.uzm.hylex.core;

import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.listeners.PlayerChatListener;
import com.uzm.hylex.core.listeners.PluginMessageListener;
import com.uzm.hylex.core.loaders.PluginLoader;
import com.uzm.hylex.core.party.BukkitPartyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class Core extends JavaPlugin {

  private static Core core;
  public static PluginLoader loader;
  public static String SOCKET_NAME;
  public static boolean IS_ARENA_CLIENT;
  public static boolean DISABLE_FLY;

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
    Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "hylex-core");
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new BukkitPartyManager());
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new PluginMessageListener());

    getServer().getConsoleSender()
      .sendMessage("§b[Hylex Module: Core] §7Plugin §fdefinitivamente §7carregado com sucesso (§f" + (System.currentTimeMillis() - aux + " milisegundos§7)"));
  }

  public void onDisable() {
    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");
    HylexPlayer.listPlayers().forEach(HylexPlayer::save);
    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("mNPCS");
    if (team != null) {
      team.unregister();
    }
    for (Group group : Group.values()) {
      team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(group.getOrder());
      if (team != null) {
        team.unregister();
      }
    }
  }

  public static Core getInstance() {
    return core;
  }

  public static PluginLoader getLoader() {
    return loader;
  }
}
