package com.uzm.hylex.core.libraries.npclib;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.libraries.npclib.api.npc.EntityController;
import com.uzm.hylex.core.libraries.npclib.npc.AbstractNPC;
import com.uzm.hylex.core.libraries.npclib.npc.EntityControllers;
import com.uzm.hylex.core.libraries.npclib.npc.ai.NPCHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

/**
 * @author Maxter
 */
public class NPCLibrary {

  private static Plugin plugin;
  private static Listener LISTENER;
  private static List<NPC> npcs = new ArrayList<>();



  public static void setupNPCs(Core pl) {
    if (pl == null || plugin != null) {
      return;
    }

    plugin = pl;
    LISTENER = new NPCListeners();
    Bukkit.getServer().getPluginManager().registerEvents(LISTENER, pl);



  }

  public static NPC createNPC(EntityType type, String name) {
    return createNPC(type, UUID.randomUUID(), name);
  }

  public static NPC createNPC(EntityType type, UUID uuid, String name) {
    Preconditions.checkNotNull(type, "Tipo nao pode ser null");
    Preconditions.checkNotNull(name, "Nome nao pode ser null");

    EntityController controller = EntityControllers.getController(type);
    NPC npc = new AbstractNPC(uuid, name, controller);
    npcs.add(npc);
    return npc;
  }

  public static void unregister(NPC npc) {
    npcs.remove(npc);
  }

  public static void unregisterAll() {
    for (NPC npc : listNPCS()) {
      npc.destroy();
    }
    HandlerList.unregisterAll(LISTENER);
    plugin = null;
    npcs = null;
  }

  public static boolean isNPC(Entity entity) {
    return getNPC(entity) != null;
  }

  public static NPC getNPC(Entity entity) {
    return entity instanceof NPCHolder ? ((NPCHolder) entity).getNPC() : null;
  }

  public static Plugin getPlugin() {
    return plugin;
  }

  public static Collection<NPC> listNPCS() {
    return ImmutableList.copyOf(npcs);
  }

  public static NPC findNPC(UUID uuid) {
    return listNPCS().stream().filter(npc -> npc.getUUID().equals(uuid)).findFirst().orElse(null);
  }
  public static List<NPC> getNpcs() {
    return npcs;
  }

}
