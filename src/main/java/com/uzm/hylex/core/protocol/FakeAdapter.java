package com.uzm.hylex.core.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.controllers.FakeController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Server.*;

public class FakeAdapter extends PacketAdapter {
  public FakeAdapter() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Client.CHAT, TAB_COMPLETE, PLAYER_INFO, CHAT, SCOREBOARD_OBJECTIVE, SCOREBOARD_SCORE, SCOREBOARD_TEAM));
  }

  @Override
  public void onPacketReceiving(PacketEvent evt) {
    PacketContainer packet = evt.getPacket();
    if (packet.getType() == PacketType.Play.Client.CHAT) {
      packet.getStrings().write(0, FakeController.replaceNickedPlayers(packet.getStrings().read(0), false));
    }
  }

  @Override
  public void onPacketSending(PacketEvent evt) {
    try {
      PacketContainer packet = evt.getPacket();
      if (packet.getType() == TAB_COMPLETE) {
        List<String> list = new ArrayList<>();
        for (String complete : packet.getStringArrays().read(0)) {
          list.add(FakeController.replaceNickedPlayers(complete, true));
        }

        packet.getStringArrays().write(0, list.toArray(new String[list.size()]));
      } else if (packet.getType() == PLAYER_INFO) {
        List<PlayerInfoData> infoDataList = new ArrayList<>();
        for (PlayerInfoData infoData : packet.getPlayerInfoDataLists().read(0)) {
          WrappedGameProfile profile = infoData.getProfile();
          if (FakeController.has(profile.getName())) {
            infoData = new PlayerInfoData(FakeController.cloneGameProfile(profile), infoData.getLatency(), infoData.getGameMode(), infoData.getDisplayName());
          }

          infoDataList.add(infoData);
        }

        packet.getPlayerInfoDataLists().write(0, infoDataList);
      } else if (packet.getType() == CHAT) {
        WrappedChatComponent component = packet.getChatComponents().read(0);
        if (component != null) {
          packet.getChatComponents().write(0, WrappedChatComponent.fromJson(FakeController.replaceNickedPlayers(component.getJson(), true)));
        }
      } else if (packet.getType() == SCOREBOARD_OBJECTIVE) {
        packet.getStrings().write(1, FakeController.replaceNickedPlayers(packet.getStrings().read(1), true));
      } else if (packet.getType() == SCOREBOARD_SCORE) {
        packet.getStrings().write(0, FakeController.replaceNickedPlayers(packet.getStrings().read(0), true));
      } else if (packet.getType() == SCOREBOARD_TEAM) {
        List<String> members = new ArrayList<>();
        for (String member : (Collection<String>) packet.getModifier().withType(Collection.class).read(0)) {
          if (FakeController.has(member)) {
            member = FakeController.getFake(member);
          }

          members.add(member);
        }

        packet.getModifier().withType(Collection.class).write(0, members);
      }
    }  catch (Exception ignore) {}
  }


}
