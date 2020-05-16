package com.uzm.hylex.core.bungee;

import com.uzm.hylex.core.bungee.commands.*;
import com.uzm.hylex.core.bungee.configuration.ConfigurationCreator;
import com.uzm.hylex.core.bungee.controllers.MotdController;
import com.uzm.hylex.core.bungee.listeners.ChatListener;
import com.uzm.hylex.core.bungee.listeners.PluginMessageListener;
import com.uzm.hylex.core.bungee.listeners.ProxyConnectListener;
import com.uzm.hylex.core.bungee.listeners.ProxyPingListener;
import com.uzm.hylex.core.bungee.loaders.ServicesLoader;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * @author Maxter
 */
public class Bungee extends Plugin {

  private static Bungee instance;

  private static PluginManager pm;

  @Override
  public void onEnable() {
    instance = this;
    pm = getProxy().getPluginManager();

    getProxy().registerChannel("hylex-core");

    this.getLogger().info("§b[Hylex Module: Core] §7Plugin §fessencialmente §7carregado com sucesso.");
    this.getLogger().info("§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthor());

    getPluginManager().registerCommand(this, new TellCommand());
    getPluginManager().registerCommand(this, new RCommand());
    getPluginManager().registerCommand(this, new MotdCommand());
    getPluginManager().registerCommand(this, new PartyCommand());
    getPluginManager().registerCommand(this, new PartyChatCommand());
    getPluginManager().registerCommand(this, new ReportCommand());
    getPluginManager().registerCommand(this, new StaffChatCommand());
    getPluginManager().registerCommand(this, new LobbyCommand());

    getPluginManager().registerListener(this, new ChatListener());
    getPluginManager().registerListener(this, new ProxyPingListener());
    getPluginManager().registerListener(this, new ProxyConnectListener());
    getPluginManager().registerListener(this, new PluginMessageListener());

    new ConfigurationCreator(this, "%datafolder%/setup.yml");

    MotdController.load();

    new ServicesLoader(this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("§b[Hylex Module: Core] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");
  }


  public static Bungee getInstance() {
    return instance;
  }

  public static PluginManager getPluginManager() {
    return pm;
  }
}
