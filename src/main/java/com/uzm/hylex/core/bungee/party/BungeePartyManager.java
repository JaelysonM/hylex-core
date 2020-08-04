package com.uzm.hylex.core.bungee.party;

import com.google.common.collect.ImmutableList;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.api.Group;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BungeePartyManager {

  private static ScheduledTask CLEAN_PARTIES;
  private static List<BungeeParty> BUNGEE_PARTIES = new ArrayList<>();

  public static BungeeParty createParty(ProxiedPlayer leader) {
    BungeeParty bp = new BungeeParty(leader.getName(), Group.getPlayerGroup(leader).getPartySize());
    BUNGEE_PARTIES.add(bp);
    if (CLEAN_PARTIES == null) {
      CLEAN_PARTIES = ProxyServer.getInstance().getScheduler().schedule(Bungee.getInstance(), () -> ImmutableList.copyOf(BUNGEE_PARTIES).forEach(bps -> {
        if (!bps.update()) {
          bps.destroy();
          BUNGEE_PARTIES.remove(bps);
        }
      }), 0L, 1L, TimeUnit.SECONDS);
    }

    return bp;
  }

  public static BungeeParty getLeaderParty(String player) {
    return BUNGEE_PARTIES.stream().filter(bp -> bp.isLeader(player)).findAny().orElse(null);
  }

  public static BungeeParty getMemberParty(String player) {
    return BUNGEE_PARTIES.stream().filter(bp -> bp.isMember(player)).findAny().orElse(null);
  }

  public static List<BungeeParty> listParties() {
    BUNGEE_PARTIES.removeIf(Objects::isNull);
    return BUNGEE_PARTIES;
  }
}
