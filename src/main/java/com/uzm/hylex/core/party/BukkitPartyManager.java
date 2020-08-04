package com.uzm.hylex.core.party;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BukkitPartyManager implements PluginMessageListener {

  private static BukkitTask REQUEST_PARTIES;
  private static List<BukkitParty> BUKKIT_PARTIES = new ArrayList<>();

  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] data) {
    if (channel.equals("hylex-core")) {
      ByteArrayDataInput in = ByteStreams.newDataInput(data);
      String subChannel = in.readUTF();
      List<BukkitParty> parties = new ArrayList<>();
      if (subChannel.equals("Parties")) {

        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
          try {
            String arrayParties = in.readUTF();
            Object objectJSON = new JSONParser().parse(arrayParties);
            Object objectArray = ((JSONObject) objectJSON).get("parties");
            for (Object partys : (JSONArray) objectArray) {
              {
                JSONObject party = (JSONObject) partys;

                BukkitParty bp = new BukkitParty(party.get("leader").toString());
                for (Object object : (JSONArray) party.get("members")) {
                  if (!bp.isMember(object.toString())) {
                    bp.listMembers().add(bp.buildPlayer(object.toString()));
                  }
                }
                parties.add(bp);
              }
            }
            List<BukkitParty> oldParties = BUKKIT_PARTIES;
            BUKKIT_PARTIES = parties;
            oldParties.forEach(BukkitParty::delete);
            oldParties.clear();
          } catch (Exception ignore) {
            List<BukkitParty> oldParties = BUKKIT_PARTIES;
            BUKKIT_PARTIES = parties;
            oldParties.forEach(BukkitParty::delete);
            oldParties.clear();
          }
        });
      }
    }
  }

  public static void enableRequests() {
    if (REQUEST_PARTIES == null) {
      REQUEST_PARTIES = new BukkitRunnable() {
        @Override
        public void run() {
          Iterator<? extends Player> iterator = Bukkit.getOnlinePlayers().iterator();
          if (!iterator.hasNext()) {
            return;
          }
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("Parties");
          iterator.next().sendPluginMessage(Core.getInstance(), "hylex-core", out.toByteArray());
        }
      }.runTaskTimerAsynchronously(Core.getInstance(), 0L, 20L);
    }
  }

  public static BukkitParty getLeaderParty(String player) {
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isLeader(player)).findAny().orElse(null);
  }

  public static BukkitParty getMemberParty(String player) {
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isMember(player)).findAny().orElse(null);
  }

  public static List<BukkitParty> listParties() {
    return BUKKIT_PARTIES;
  }
}
