package com.uzm.hylex.core.java.util;

import com.google.common.collect.Maps;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigurationBuilder {


    public static HashMap<String, ConfigurationBuilder> configs = Maps.newHashMap();


    private File file;

    private JavaPlugin plugin;

    private YamlConfiguration yamlConfiguration;

    public ConfigurationBuilder(JavaPlugin plugin, String filename) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }


        file = new File(plugin.getDataFolder(), filename + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("[Hylex - Core] Ocorreu um erro ao criar o arquivo " + filename + ".yml");
            }
        }
        load();
        save();

        configs.put(filename, this);
    }

    public void load() {

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration get() {
        return yamlConfiguration;
    }


    public static ConfigurationBuilder find(String name) {
        return configs.get(name.toLowerCase());
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void save() {
        try {
            get().save(file);

        } catch (IOException ignored) {
        }


    }


}
