package com.uzm.hylex.core.bungee.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public enum ReportType {
  CHEATING("Cheating"),
  FLY("Fly"),
  KILL_AURA("Kill Aura"),
  SPEED("Speed"),
  ANTI_KNOCKBACK("Anti Knockback"),
  REACH("Reach"),
  MENSAGEM_OFENSIVA("Mensagem Ofensiva", true),
  NICK_IMPROPRIO("Nick impróprio", true),
  SKIN_IMPROPRIA("Skin imprópria", true),
  NOME_DE_PET_IMPROPRIO("Nome de pet impróprio", true),
  CAPA_IMPROPRIA("Capa imprópria", true),
  CLAN_IMPOPRIO("Clan impróprio", true);

  private String name;
  private boolean requiresProof;

  ReportType(String name) {
    this(name, false);
  }

  ReportType(String name, boolean requiresProof) {
    this.name = name;
    this.requiresProof = requiresProof;
  }

  public String getName() {
    return this.name;
  }

  public boolean isRequiresProof() {
    return this.requiresProof;
  }

  public int getProofId() {
    return 1 + this.name.split(" ").length;
  }

  public static final TextComponent REASONS;
  private static final ReportType[] VALUES;

  static {
    VALUES = values();
    REASONS = new TextComponent("");
    for (BaseComponent component : TextComponent.fromLegacyText(" \n §7Por qual motivo deseja reportar este jogador?\n ")) {
      REASONS.addExtra(component);
    }
    for (ReportType types : VALUES) {
      for (BaseComponent component : TextComponent.fromLegacyText("§f\n " + types.getName())) {
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique para reportar o jogador por " + types.getName() + ".")));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/report {player} " + types.getName() + (types.isRequiresProof() ? " <prova>" : "")));
        REASONS.addExtra(component);
      }
    }
  }

  public static ReportType fromName(String name) {
    for (ReportType type : VALUES) {
      if (type.getName().toLowerCase().startsWith(name.toLowerCase())) {
        return type;
      }
    }

    return null;
  }

  public static ReportType fromValue(String name) {
    for (ReportType type : VALUES) {
      if (type.toString().equalsIgnoreCase(name)) {
        return type;
      }
    }

    return null;
  }
}
