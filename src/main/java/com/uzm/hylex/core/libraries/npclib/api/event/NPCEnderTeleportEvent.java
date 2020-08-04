package com.uzm.hylex.core.libraries.npclib.api.event;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;


/**
 * Called when an NPC is teleported after using an ender pearl.
 */
public class NPCEnderTeleportEvent extends NPCEvent implements Cancellable {
  private boolean cancelled;

  public NPCEnderTeleportEvent(NPC npc) {
    super(npc);
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean arg0) {
    this.cancelled = arg0;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  private static final HandlerList handlers = new HandlerList();
}