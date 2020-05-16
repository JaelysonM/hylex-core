package com.uzm.hylex.core.api.party;

import com.uzm.hylex.core.bungee.api.Group;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class Party {

  /**
   * O tempo em minutos que demora até deletar uma Party caso todos os jogadores dela estejam offline.
   */
  private static final long MINUTES_UNTIL_DELETE = 5L;
  /**
   * O tempo em minutos que demora até deletar uma Party caso todos os jogadores dela estejam offline.
   */
  private static final long MINUTES_UNTIL_EXPIRE_INVITE = 1L;

  private int slots;
  protected PartyPlayer leader;
  protected List<PartyPlayer> members;
  protected Map<String, Long> invitesMap;

  private long lastOnlineTime;

  public Party(String leader, int slots) {
    this.slots = slots;
    this.leader = this.buildPlayer(leader);
    this.members = new ArrayList<>();
    this.invitesMap = new ConcurrentHashMap<>();
    this.members.add(this.leader);
  }

  public abstract void delete();

  public abstract PartyPlayer buildPlayer(String name);

  public TextComponent invite(String target) {
    this.invitesMap.put(target.toLowerCase(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_EXPIRE_INVITE));
    TextComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(" \n§a" + this.leader.getColored() + " §aconvidou você para a Party dele!\n§7Você pode ")) {
      component.addExtra(components);
    }
    BaseComponent accept = new TextComponent("ACEITAR");
    accept.setColor(ChatColor.GREEN);
    accept.setBold(true);
    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party aceitar " + this.getLeader()));
    accept
      .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique para aceitar o convite de Party de " + this.leader.getColored() + "§7.")));
    component.addExtra(accept);
    for (BaseComponent components : TextComponent.fromLegacyText(" §7ou ")) {
      component.addExtra(components);
    }
    BaseComponent reject = new TextComponent("NEGAR");
    reject.setColor(ChatColor.RED);
    reject.setBold(true);
    reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party negar " + this.getLeader()));
    reject
      .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique para negar o convite de Party de " + this.leader.getColored() + "§7.")));
    component.addExtra(reject);
    for (BaseComponent components : TextComponent.fromLegacyText(" §7o convite.\n ")) {
      component.addExtra(components);
    }

    return component;
  }

  public void reject(String member) {
    this.invitesMap.remove(member.toLowerCase());
    this.leader.sendMessage(" \n" + Group.getColored(member) + " §anegou seu convite de Party!\n ");
  }

  public void join(String member) {
    this.broadcast(" \n" + Group.getColored(member) + " §aentrou na Party!\n ");
    this.members.add(this.buildPlayer(member));
    this.invitesMap.remove(member.toLowerCase());
  }

  public void leave(String member) {
    String leader = this.getLeader();
    this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));
    if (this.members.isEmpty()) {
      this.delete();
      return;
    }

    if (leader.equals(member)) {
      this.leader = this.members.get(0);
      this.broadcast(" \n" + this.leader.getColored() + " §ase tornou o novo Líder da Party!\n ");
    }
    this.broadcast(" \n" + Group.getColored(member) + " §asaiu da Party!\n ");
  }

  public void kick(String member) {
    this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(member)).findFirst().ifPresent(pp -> {
      pp.sendMessage(" \n" + this.leader.getColored() + " §aexpulsou você da Party!\n ");
      this.members.removeIf(pap -> pap.equals(pp));
    });
  }

  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    if (newLeader == null) {
      return;
    }
    this.leader = newLeader;
  }

  public void broadcast(String message) {
    this.broadcast(message, false);
  }

  public void broadcast(String message, boolean ignoreLeader) {
    this.members.stream().filter(pp -> !ignoreLeader || !pp.equals(this.leader)).forEach(pp -> pp.sendMessage(message));
  }

  public boolean update() {
    if (onlineCount() == 0) {
      return this.lastOnlineTime + (TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_DELETE)) >= System.currentTimeMillis();
    }

    this.lastOnlineTime = System.currentTimeMillis();
    this.invitesMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
    return true;
  }

  public void destroy() {
    this.slots = 0;
    this.leader = null;
    this.members.clear();
    this.members = null;
    this.invitesMap.clear();
    this.invitesMap = null;
    this.lastOnlineTime = 0L;
  }

  public int getSlots() {
    return this.slots;
  }

  public long onlineCount() {
    return this.members.stream().filter(PartyPlayer::isOnline).count();
  }

  public String getLeader() {
    return this.leader.getName();
  }

  public String getName(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).map(PartyPlayer::getName).findAny().orElse(name);
  }

  public PartyPlayer getPlayer(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).findAny().orElse(null);
  }

  public boolean canJoin() {
    return this.members.size() < this.slots;
  }

  public boolean isInvited(String name) {
    return this.invitesMap.containsKey(name.toLowerCase());
  }

  public boolean isMember(String name) {
    return this.members.stream().anyMatch(pp -> pp.getName().equalsIgnoreCase(name));
  }

  public boolean isLeader(String name) {
    return this.leader.getName().equalsIgnoreCase(name);
  }

  public List<PartyPlayer> listMembers() {
    return this.members;
  }
}
