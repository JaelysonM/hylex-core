package com.uzm.hylex.core.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.configuration.ConfigurationCreator;
import com.uzm.hylex.core.bungee.controllers.MotdController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;


public class LobbyCommand extends Command {

  private String label;


  public LobbyCommand() {
    super("lobby");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    String serverName = player.getServer().getInfo().getName();
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("SendPlayerLobby");
    out.writeUTF(player.getName());
    out.writeUTF(serverName.startsWith("mega") && serverName.endsWith("B")?"bedwars": "main");
    player.getServer().sendData("hylex-core", out.toByteArray());
  }


}
