package com.uzm.hylex.core.api.events;

import com.uzm.hylex.core.api.HylexPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HylexPlayerLoadEvent extends Event {



  private HylexPlayer hylexPlayer;

  public HylexPlayerLoadEvent(HylexPlayer hylexPlayer) {
    this.hylexPlayer = hylexPlayer;
  }

  public HylexPlayer getHylexPlayer() {
    return this.hylexPlayer;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
