package com.uzm.hylex.core.bungee.party;

import com.uzm.hylex.core.api.party.Party;
import com.uzm.hylex.core.api.party.PartyPlayer;

public class BungeeParty extends Party {

  public BungeeParty(String leader, int slots) {
    super(leader, slots);
  }

  @Override
  public void delete() {
    this.destroy();
    BungeePartyManager.listParties().remove(this);
  }

  @Override
  public PartyPlayer buildPlayer(String name) {
    return new BungeePartyPlayer(name);
  }
}
