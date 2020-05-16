package com.uzm.hylex.core.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.api.party.PartyPlayer;
import com.uzm.hylex.core.bungee.party.BungeeParty;
import com.uzm.hylex.core.bungee.party.BungeePartyManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class PluginMessageListener implements Listener {

  @EventHandler
  public void onPluginMessage(PluginMessageEvent evt) {
    if (evt.getTag().equals("hylex-core")) {
      if (evt.getSender() instanceof Server && evt.getReceiver() instanceof ProxiedPlayer) {
        ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
        String subChannel = in.readUTF();
        if (subChannel.equals("Parties")) {
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("Parties");
          BungeePartyManager.listParties().forEach(bp -> {
            JSONObject json = new JSONObject();
            json.put("leader", bp.getLeader());
            JSONArray members = new JSONArray();
            bp.listMembers().stream().filter(PartyPlayer::isOnline).map(PartyPlayer::getName).forEach(members::add);
            json.put("members", members);
            out.writeUTF(json.toString());
            members.clear();
            json.clear();
          });
          ((Server) evt.getSender()).sendData("hylex-core", out.toByteArray());
        } else if (subChannel.equals("SendPartyMembers")) {
          String leader = in.readUTF();
          BungeeParty party = BungeePartyManager.listParties().stream().filter(result -> result.getLeader().equalsIgnoreCase(leader)).findFirst().orElse(null);

          if (party != null) {
            ServerInfo server = ProxyServer.getInstance().getServerInfo(in.readUTF());
            if (server != null) {
              String mini = in.readUTF();
              party.listMembers().forEach(pp -> {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(pp.getName());
                if (player != null) {
                  if (player.getServer().getInfo().getName().equals("auth")) {
                    return;
                  }
                  ByteArrayDataOutput out = ByteStreams.newDataOutput();
                  out.writeUTF("SendPartyMember");
                  out.writeUTF(player.getName());
                  out.writeUTF(server.getName());
                  out.writeUTF(mini);
                  out.writeUTF(player.getServer().getInfo() == server?"INSIDE": "OUTSIDE");
                  player.getServer().sendData("hylex-core", out.toByteArray());
                }
              });
            }
          }
        }
      }
    }
  }
}
