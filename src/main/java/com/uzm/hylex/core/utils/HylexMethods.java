package com.uzm.hylex.core.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class HylexMethods {

  public static boolean isNumeric(String strNum) {
    return strNum.matches("-?\\d+(\\.\\d+)?");
  }

  public enum StaffAction {
    TELEPORT("§7[%player% forcou o teletransporte de %actionname% para §n%loc%]"),
    TELEPORTTO("§7[%player% teletransportou-se para §n%actionname%]"),
    TPALL("§7[%player% teletransportou todos os(as) %actionname% até ele]"),
    INVENTORYSEE(""),
    GAMEMODE("§7[%player% alterou o modo de jogo de %actionname% para §n%loc%]"),
    GAMEMODE_PERPLAYER("§7[%player% alterou o modo de jogo para §n%loc%]"),
    FLY("§7[%player% alterou o modo de vôo de %actionname% para §n%loc%]"),
    FLY_PERPLAYER("§7[%player% alterou o modo de vôo para §n%loc%]"),
    INVISIBLE("§7[%player% alterou o modo de visibilidade para §n%loc%]"),
    BUILD("§7[%player% alterou o modo de construção para §n%loc%]");

    private String message;

    StaffAction(String message) {
      this.message = message;
    }

    public String getMessage() {
      return this.message;
    }
  }

  public static String modifyBalance(double value) {
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-US"));
    if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.").length > 1) {
      if (numberFormat.format(value).concat(" ").concat("").replace(" ", "").split("\\.")[1].length() == 3) {
        return numberFormat.format(value).replace(" ", "").substring(0, numberFormat.format(value).replace(" ", "").length() - 1);
      }
    } else {
      return numberFormat.format(value).concat(" ").concat("").replace(" ", "");
    }

    return numberFormat.format(value).concat(" ").concat("").replace(" ", "");
  }

  public static void sendTitle(Player jogador, String mensagem) {
    PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
    ((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(packet1);
  }

  public static void sendSubTitle(Player jogador, String mensagem) {
    PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
    ((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(packet1);
  }

  public static void sendActionBar(Player player, String message) {
    CraftPlayer p = (CraftPlayer) player;

    IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc);
    p.getHandle().playerConnection.sendPacket(ppoc);
  }
}
