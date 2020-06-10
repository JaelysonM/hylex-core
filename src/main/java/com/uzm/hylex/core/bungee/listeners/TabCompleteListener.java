package com.uzm.hylex.core.bungee.listeners;


import com.uzm.hylex.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.stream.Collectors;

public class TabCompleteListener implements Listener {
  @EventHandler
  public void onTabComplete(TabCompleteEvent evt) {
    if (evt.getCursor().trim().equalsIgnoreCase("/report")) {
    evt.getSuggestions().clear();
    evt.getSuggestions().addAll(Bungee.getInstance().getProxy().getPlayers().stream().map(CommandSender::getName).collect(Collectors.toList()));
    }
  }
}
