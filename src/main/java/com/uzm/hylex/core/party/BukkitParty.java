package com.uzm.hylex.core.party;


import com.uzm.hylex.core.api.party.Party;
import com.uzm.hylex.core.api.party.PartyPlayer;

public class BukkitParty extends Party {

  public BukkitParty(String leader) {
    super(leader, 0);
  }

  @Override
  public PartyPlayer buildPlayer(String name) {
    return new BukkitPartyPlayer(name);
  }

  @Override
  public void delete() {
    this.destroy();
  }

}
