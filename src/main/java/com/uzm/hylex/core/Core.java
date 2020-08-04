package com.uzm.hylex.core;

import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.bungeeguard.LoginController;
import com.uzm.hylex.core.fake.BukkitFakeManager;
import com.uzm.hylex.core.java.util.configuration.ConfigurationCreator;
import com.uzm.hylex.core.skins.bukkit.PlayerJoin;
import com.uzm.hylex.core.skins.bukkit.PlayerMessageListener;
import com.uzm.hylex.core.spigot.PluginMessageListener;
import com.uzm.hylex.core.loaders.PluginLoader;
import com.uzm.hylex.core.party.BukkitPartyManager;
import com.uzm.hylex.core.skins.bukkit.SkinsRestorerBukkitAPI;
import com.uzm.hylex.core.skins.bukkit.factory.SkinFactory;
import com.uzm.hylex.core.skins.bukkit.factory.UniversalSkinFactory;
import com.uzm.hylex.core.skins.shared.storage.Config;
import com.uzm.hylex.core.skins.shared.storage.SkinStorage;
import com.uzm.hylex.core.skins.shared.utils.MineSkinAPI;
import com.uzm.hylex.core.skins.shared.utils.MojangAPI;
import com.uzm.hylex.core.skins.shared.utils.SkinLogger;
import com.uzm.hylex.core.skins.shared.utils.SkinsRestorerAPI;
import com.uzm.hylex.core.sql.MariaDB;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class Core extends JavaPlugin {

  private static Core core;
  public static PluginLoader loader;
  public static String SOCKET_NAME;
  public static boolean IS_ARENA_CLIENT;
  public static boolean DISABLE_FLY;
  public static boolean BUNGEE_GUARD = true;

  private SkinStorage skinStorage;

  private MojangAPI mojangAPI;

  private MineSkinAPI mineSkinAPI;

  private SkinLogger srLogger;

  private SkinFactory factory;

  private SkinsRestorerAPI skinsRestorerAPI;

  private Set<String> allowedTokens;

  private static LoginController utils;



  public void onEnable() {
    long aux = System.currentTimeMillis();

    getServer().getConsoleSender().sendMessage("§b[Hylex Module: Core] §7Plugin §fessencialmente §7carregado com sucesso.");
    getServer().getConsoleSender().sendMessage("§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthors());

    /*
     * Declarations
     */

    core = this;
    srLogger = new SkinLogger();

    this.skinStorage = new SkinStorage();

    this.mojangAPI = new MojangAPI(this.srLogger);
    this.mineSkinAPI = new MineSkinAPI(this.srLogger);

    factory = new UniversalSkinFactory(this);


    this.skinStorage.setMojangAPI(mojangAPI);

    this.mojangAPI.setSkinStorage(this.skinStorage);
    this.mineSkinAPI.setSkinStorage(this.skinStorage);
    new ConfigurationCreator(this, "setup", "");
    YamlConfiguration config = ConfigurationCreator.find("setup", this).get();

    this.allowedTokens = new HashSet<>(config.getStringList("allowed-tokens"));


    Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);


    if (config.get("bungee-guard") != null) {
      BUNGEE_GUARD = (boolean) config.get("bungee-guard");
    }


    Config.MYSQL_HOST = config.getString("storage.host");
    Config.MYSQL_HOST = config.getString("storage.host");
    Config.MYSQL_DATABASE = config.getString("storage.database");
    Config.MYSQL_USERNAME = config.getString("storage.username");
    Config.MYSQL_USERNAME = config.getString("storage.username");
    Config.MYSQL_PORT = config.getString("storage.port");
    Config.MYSQL_PASSWORD = config.getString("storage.password");
    Config.DEFAULT_SKINS_ENABLED = config.getBoolean("default-skin");
    Config.DEFAULT_SKINS.addAll(config.getStringList("default-skins"));


    MariaDB mysql = MariaDB.create("skins", Config.MYSQL_HOST, Config.MYSQL_PORT, Config.MYSQL_DATABASE, Config.MYSQL_USERNAME, Config.MYSQL_PASSWORD);
    mysql.setTablesRules(
      "CREATE TABLE IF NOT EXISTS " + Config.MYSQL_PLAYERTABLE + " (" + "Nick varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "Skin varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "PRIMARY KEY (Nick)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci",
      "CREATE TABLE IF NOT EXISTS " + Config.MYSQL_SKINTABLE + " (" + "Nick varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "Value text COLLATE utf8_unicode_ci," + "Signature text COLLATE utf8_unicode_ci," + "timestamp text COLLATE utf8_unicode_ci," + "PRIMARY KEY (Nick)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");

    this.skinStorage.setMysql(mysql);


    loader = new PluginLoader(this);

    utils = new LoginController(this);



    Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "hylex-core");
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new BukkitPartyManager());
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new BukkitFakeManager());
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new PluginMessageListener());
    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "hylex-core", new PlayerMessageListener(this));

    this.skinsRestorerAPI = new SkinsRestorerBukkitAPI(this, mojangAPI, skinStorage);

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


  public MineSkinAPI getMineSkinAPI() {
    return mineSkinAPI;
  }

  public MojangAPI getMojangAPI() {
    return mojangAPI;
  }

  public SkinStorage getSkinStorage() {
    return skinStorage;
  }

  public SkinFactory getFactory() {
    return factory;
  }

  public SkinsRestorerAPI getSkinsRestorerAPI() {
    return skinsRestorerAPI;
  }

  public Set<String> getAllowedTokens() {
    return allowedTokens;
  }

  public static LoginController getUtils() {
    return utils;
  }
}
