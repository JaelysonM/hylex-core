package com.uzm.hylex.core.bungee;

import com.uzm.hylex.core.bungee.commands.*;
import com.uzm.hylex.core.bungee.configuration.ConfigurationCreator;
import com.uzm.hylex.core.bungee.controllers.FakeController;
import com.uzm.hylex.core.bungee.controllers.MotdController;
import com.uzm.hylex.core.bungee.controllers.QueueController;
import com.uzm.hylex.core.bungee.listeners.*;
import com.uzm.hylex.core.bungee.loaders.ServicesLoader;
import com.uzm.hylex.core.skins.bungee.SkinApplier;
import com.uzm.hylex.core.skins.bungee.SkinsRestorerBungeeAPI;
import com.uzm.hylex.core.skins.bungee.listeners.LoginListener;
import com.uzm.hylex.core.skins.shared.storage.Config;
import com.uzm.hylex.core.skins.shared.storage.SkinStorage;
import com.uzm.hylex.core.skins.shared.utils.MineSkinAPI;
import com.uzm.hylex.core.skins.shared.utils.MojangAPI;
import com.uzm.hylex.core.skins.shared.utils.SkinLogger;
import com.uzm.hylex.core.skins.shared.utils.SkinsRestorerAPI;
import com.uzm.hylex.core.sql.MariaDB;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Maxter
 */
public class Bungee extends Plugin {

  private static Bungee instance;

  private static PluginManager pm;



  private SkinApplier skinApplier;


  private SkinStorage skinStorage;

  private MojangAPI mojangAPI;

  private MineSkinAPI mineSkinAPI;

  private SkinsRestorerAPI skinsRestorerAPI;

  private boolean multiBungee = true;

  private SkinLogger srLogger;

  @Override
  public void onEnable() {
    instance = this;
    srLogger = new SkinLogger();

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
    getPluginManager().registerCommand(this, new SendReportCommand());
    getPluginManager().registerCommand(this, new MegaTeleportCommand());
    getPluginManager().registerCommand(this, new ToggleCommand());
    getPluginManager().registerCommand(this, new SkinCommand(this));
    getPluginManager().registerCommand(this, new FakeCommand());
    getPluginManager().registerCommand(this, new FakeListCommand());
    getPluginManager().registerCommand(this, new FakeResetCommand());

    getPluginManager().registerListener(this, new ChatListener());
    getPluginManager().registerListener(this, new ProxyPingListener());
    getPluginManager().registerListener(this, new ProxyConnectListener());
    getPluginManager().registerListener(this, new PluginMessageListener());
    getPluginManager().registerListener(this, new LoginListener(this));
    getPluginManager().registerListener(this, new TabCompleteListener());

    new ConfigurationCreator(this, "%datafolder%/setup.yml");

    MotdController.load();
    QueueController.run();

    new ServicesLoader(this);
    Configuration config = com.uzm.hylex.core.bungee.configuration.ConfigurationCreator.find("setup.yml").get();

    Config.MYSQL_HOST = config.getString("storage.host");
    Config.MYSQL_DATABASE = config.getString("storage.database");
    Config.MYSQL_USERNAME = config.getString("storage.username");
    Config.MYSQL_PORT = config.getString("storage.port");
    Config.MYSQL_PASSWORD = config.getString("storage.password");
    Config.DEFAULT_SKINS_ENABLED = config.getBoolean("default-skin");
    Config.DEFAULT_SKINS.addAll(config.getStringList("default-skins"));

    if (config.get("blacklist-fake") != null) {
      FakeController.setBlacklistedNames(new HashSet<>(config.getStringList("blacklist-fake")));
    }

    if (config.get("random-fake") != null) {
      FakeController.setRandoms(config.getStringList("random-fake"));
    }


    this.skinStorage = new SkinStorage();

    this.mojangAPI = new MojangAPI(this.srLogger);
    this.mineSkinAPI = new MineSkinAPI(this.srLogger);

    this.skinStorage.setMojangAPI(this.mojangAPI);

    MariaDB mysql = MariaDB.create("skins", Config.MYSQL_HOST, Config.MYSQL_PORT, Config.MYSQL_DATABASE, Config.MYSQL_USERNAME, Config.MYSQL_PASSWORD);
    mysql.setTablesRules(
      "CREATE TABLE IF NOT EXISTS " + Config.MYSQL_PLAYERTABLE + " (" + "Nick varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "Skin varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "PRIMARY KEY (Nick)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci",
      "CREATE TABLE IF NOT EXISTS " + Config.MYSQL_SKINTABLE + " (" + "Nick varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "Value text COLLATE utf8_unicode_ci," + "Signature text COLLATE utf8_unicode_ci," + "timestamp text COLLATE utf8_unicode_ci," + "PRIMARY KEY (Nick)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");
    this.skinStorage.setMysql(mysql);

    this.mojangAPI.setSkinStorage(this.skinStorage);
    this.mineSkinAPI.setSkinStorage(this.skinStorage);


    this.skinApplier = new SkinApplier(this);
    this.skinApplier.init();


    this.skinsRestorerAPI = new SkinsRestorerBungeeAPI(this, mojangAPI, skinStorage);

    Bungee.getInstance().getProxy().getScheduler().schedule(Bungee.getInstance(), () -> {

      for (String stringName : FakeController.listNicked()) {
        if (getProxy().getPlayer(stringName) != null) {
          ProxiedPlayer player = getProxy().getPlayer(stringName);
          if (!player.getServer().getInfo().getName().contains("mega")) {
            player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§fVocê está atualmente §cDISFARÇADO"));
          }
        }
      }
    }, 1, 2, TimeUnit.SECONDS);

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



  public SkinStorage getSkinStorage() {
    return skinStorage;
  }

  public MojangAPI getMojangAPI() {
    return mojangAPI;
  }

  public boolean isMultiBungee() {
    return multiBungee;
  }

  public MineSkinAPI getMineSkinAPI() {
    return mineSkinAPI;
  }

  public SkinApplier getSkinApplier() {
    return skinApplier;
  }

  public SkinsRestorerAPI getSkinsRestorerAPI() {
    return skinsRestorerAPI;
  }
}
