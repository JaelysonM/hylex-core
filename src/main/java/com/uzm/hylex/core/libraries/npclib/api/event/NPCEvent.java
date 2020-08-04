package com.uzm.hylex.core.libraries.npclib.api.event;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.event.Event;

/**
 * @author Maxter
 */
public abstract class NPCEvent extends Event {
  final NPC npc;

  protected NPCEvent(NPC npc) {
    super();
    this.npc = npc;
  }

  /**
   * Get the npc involved in the event.
   *
   * @return the npc involved in the event
   */
  public NPC getNPC() {
    return npc;
  }
}
