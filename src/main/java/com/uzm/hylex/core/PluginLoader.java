package com.uzm.hylex.core;

import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.nms.interfaces.INMS;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.spigot.enums.MinecraftVersion;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;



public class PluginLoader {


    private PluginManager pm = Bukkit.getServer().getPluginManager();
    private Core core;
    private MinecraftVersion version;

    private ItemBuilder builder;

    private INMS nms;

    public PluginLoader(Core core) {
        this.core = core;
        version = MinecraftVersion.getVersion(Bukkit.getServer().getClass().getPackage().getName()
                .substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1));
        Bukkit.getServer().getConsoleSender()
                .sendMessage("§b[Hylex Module: Core] §7Indentificamos a versão " + version.toString());

        if (NMS.setupNMS(version)) {
            Bukkit.getServer().getConsoleSender()
                    .sendMessage("§b[Hylex Module: Core] §7Carregado o NMS " + version.toString()  +" (NPCs não funcionam na 1.14)");
        }

        NPCLibrary.setupNPCs(core);
        HologramLibrary.setupHolograms(core);

    }

    public MinecraftVersion getSpigotVersion() {
        return version;
    }

    public Core getCore() {
        return core;
    }

    public INMS getNms() {
        return nms;
    }
}
