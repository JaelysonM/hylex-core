package com.uzm.hylex.core.api.party;

public abstract class PartyPlayer {

  private String name;

  public PartyPlayer(String name) {
    this.name = name;
  }

  public abstract void sendMessage(String message);

  public String getName() {
    return this.name;
  }

  public abstract String getPrefixed();

  public abstract String getColored();

  public abstract boolean isOnline();
}
