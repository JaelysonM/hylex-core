package com.uzm.hylex.core.listeners.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.uzm.hylex.core.Core;

import java.util.Arrays;

public class TabComplete extends PacketAdapter {
  public static String[] TAB_DISABLE;

  static {
    TAB_DISABLE = new String[] {"about", "ver", "version", "help"};
  }

  public TabComplete() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Client.TAB_COMPLETE));
  }
  @Override
  public void onPacketReceiving(PacketEvent evt) {
    if (evt.getPacketType() ==PacketType.Play.Client.TAB_COMPLETE) {

      String lastText = evt.getPacket().getStrings().read(0).split(" ")[evt.getPacket().getStrings().read(0).split(" ").length - 1];
      if (Arrays.asList(TAB_DISABLE).contains(lastText.replace("/", "").replace("bukkit:", "").toLowerCase())) {
        evt.setCancelled(true);
      }

    }
  }
}
