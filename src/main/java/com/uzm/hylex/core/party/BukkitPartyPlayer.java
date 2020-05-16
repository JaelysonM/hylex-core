package com.uzm.hylex.core.party;

import com.uzm.hylex.core.api.party.PartyPlayer;
import org.bukkit.Bukkit;

public class BukkitPartyPlayer extends PartyPlayer {

  public BukkitPartyPlayer(String member) {
    super(member);
  }

  @Override
  public void sendMessage(String message) {}

  @Override
  public String getPrefixed() {
    return null;
  }

  @Override
  public String getColored() {
    return null;
  }

  @Override
  public boolean isOnline() {
    return Bukkit.getPlayerExact(this.getName()) != null;
  }
}
