package com.uzm.hylex.core.bungee.configuration;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

public class ConfigurationCreator {


  public static HashMap<String, ConfigurationCreator> configs = Maps.newHashMap();


  private String file;

  private Plugin plugin;

  private Configuration configuration;

  public ConfigurationCreator(Plugin plugin, String file) {
    this.plugin = plugin;
    try {
      File dataFolder = plugin.getDataFolder();
      this.file = file.replace("%datafolder%", dataFolder.toPath().toString());
      File configFile = new File(this.file);
      if (!configFile.exists()) {
        String[] files = this.file.split("/");
        InputStream inputStream = this.plugin.getClass().getClassLoader().getResourceAsStream(files[files.length - 1]);
        File parentFile = configFile.getParentFile();
        if (parentFile != null)
          parentFile.mkdirs();
        if (inputStream != null) {
          Files.copy(inputStream, configFile.toPath());
          System.out.print(("[%pluginname%] File " + configFile + " has been created!").replace("%pluginname%", this.plugin.getDescription().getName()));
        } else {
          configFile.createNewFile();
        }
      }

      this.configuration = this.getConfiguration();
      configs.put(configFile.getName().toLowerCase() + "-hylex-core", this);
    } catch (IOException e) {
      System.out.print("[%pluginname%] Unable to create configuration file!".replace("%pluginname%", "hylex-core"));
    }
  }

  public Configuration get() {
    return configuration;
  }

  private Configuration getConfiguration() {
    try {
      return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.file));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static ConfigurationCreator find(String name) {
    return configs.getOrDefault(name.toLowerCase() + "-hylex-core", null);
  }

  public Plugin getPlugin() {
    return plugin;
  }

  public void save() {
    this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
      try {
        File dataFolder = this.plugin.getDataFolder();
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(file.replace("%datafolder%", dataFolder.toPath().toString())));
      } catch (IOException e) {
        System.out.print("[%pluginname%] Unable to save configuration file!".replace("%pluginname%", this.plugin.getDescription().getName()));
      }
    });
  }
}
