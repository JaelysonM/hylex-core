package com.uzm.hylex.core.libraries.npclib.api.npc;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author Maxter
 */
public interface EntityController {
  
  void spawn(Location location, NPC npc);
  
  void remove();
  
  Entity getBukkitEntity();
}
