package com.uzm.hylex.core.java.util.configuration;

import com.google.common.collect.Maps;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigurationCreator {


    public static HashMap<String, ConfigurationCreator> configs = Maps.newHashMap();


    private File file;

    private JavaPlugin plugin;

    private YamlConfiguration yamlConfiguration;
    public ConfigurationCreator(JavaPlugin plugin, String filename, String dir) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), dir +filename + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                load();
                loadByResource();
                save();
            } catch (IOException e) {
                System.err.println("[Hylex - Core] Ocorreu um erro ao criar o arquivo " + filename + ".yml");
            }
        }else {
            load();
            save();
        }

        configs.put(filename.toLowerCase() + "-" +plugin.getName().toLowerCase(), this);
    }

    public void load() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void loadByResource() {
        try {
            if (getPlugin().getResource(yamlConfiguration.getName()) !=null)
            yamlConfiguration.load(  getPlugin().getResource(yamlConfiguration.getName() + ".yml"));
        } catch (IOException | InvalidConfigurationException ignored) {  }
     }

    public YamlConfiguration get() {
        return yamlConfiguration;
    }


    public static ConfigurationCreator find(String name, JavaPlugin plugin) {
        return configs.getOrDefault(name.toLowerCase() + "-" + plugin.getName().toLowerCase(),null);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void save() {
        try {
            get().save(file);
        } catch (IOException ignored) {  }


    }


}
