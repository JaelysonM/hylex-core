package com.uzm.hylex.core.bungee.party;

import com.uzm.hylex.core.api.party.PartyPlayer;
import com.uzm.hylex.core.bungee.api.Group;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePartyPlayer extends PartyPlayer {

  private Group group;

  public BungeePartyPlayer(String member) {
    super(member);
    this.group = Group.getPlayerGroup(ProxyServer.getInstance().getPlayer(member));
  }

  @Override
  public void sendMessage(String message) {
    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.getName());
    if (player != null) {
      player.sendMessage(TextComponent.fromLegacyText(message));
    }
  }

  @Override
  public String getPrefixed() {
    return this.group.getDisplay() + this.getName();
  }

  @Override
  public String getColored() {
    return this.group.getName() + this.getName();
  }

  @Override
  public boolean isOnline() {
    return ProxyServer.getInstance().getPlayer(this.getName()) != null;
  }
}
