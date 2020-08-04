package com.uzm.hylex.core.party;


import com.uzm.hylex.core.api.party.Party;
import com.uzm.hylex.core.api.party.PartyPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class BukkitParty extends Party {

  private HashSet<Player> lockedOnParty = new HashSet<>();
  public BukkitParty(String leader) {
    super(leader, 0);
  }

  @Override
  public PartyPlayer buildPlayer(String name) {
    return new BukkitPartyPlayer(name);
  }

  @Override
  public void delete()
    {
    this.destroy();
  }

  public HashSet<Player> getLockedOnParty() {
    return lockedOnParty;
  }
}
