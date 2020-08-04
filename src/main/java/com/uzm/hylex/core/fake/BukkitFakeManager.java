package com.uzm.hylex.core.fake;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.controllers.FakeController;
import com.uzm.hylex.core.controllers.TagController;
import com.uzm.hylex.core.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BukkitFakeManager implements PluginMessageListener {


  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] data) {
    if (channel.equals("hylex-core")) {
      ByteArrayDataInput in = ByteStreams.newDataInput(data);
      String subChannel = in.readUTF();
      if (subChannel.equals("FAKE")) {
        Player fakePlayer = Bukkit.getPlayerExact(in.readUTF());
        if (fakePlayer != null) {
          String name = in.readUTF();
          FakeController.apply(player, name);
          player.setDisplayName(Group.NORMAL.getDisplay() + name);
          TagController.remove(player);
          TagController tag = TagController.create(player);
          tag.setOrder(Group.NORMAL.getOrder());
          tag.setSuffix(Group.NORMAL.getColor());
          tag.update();
          player.setAllowFlight(false);
          player.setFlying(false);

          NMS.refreshPlayer(fakePlayer);
        }
      }
    }
  }

}
