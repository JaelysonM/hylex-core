package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.controllers.FakeController;
import com.uzm.hylex.core.skins.bungee.listeners.LoginListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class FakeListCommand extends Command {

  private String label;


  public FakeListCommand() {
    super("fakel");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("hylex.staff")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }

    List<String> nicked = FakeController.listNicked();
    StringBuilder sb = new StringBuilder();
    for (int index = 0; index < nicked.size(); index++) {
      sb.append("§c").append(FakeController.getFake(nicked.get(index))).append(" §fé na verdade ").append("§a").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
    }

    nicked.clear();
    if (sb.length() == 0) {
      sb.append("§cNão há nenhum usuário utilizando um nickname falso.");
    }

    player.sendMessage(TextComponent.fromLegacyText(" \n§eLista de nicknames falsos:\n \n" + sb.toString() + "\n "));

  }


}
