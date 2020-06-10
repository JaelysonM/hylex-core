package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.java.util.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command {

  public StaffChatCommand() {
    super("sc");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }


    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (player.hasPermission("hylex.staff")) {
      if (args.length == 0) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /sc <mensagem>"));
        return;
      }

      String message = StringUtils.join(args, " ");
      for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
        if (players.hasPermission("hylex.staff")) {
          players.sendMessage("§5[STAFF] " + Group.getColored(player) + "§f: " + message);
        }
      }
    }else {
      sender.sendMessage(TextComponent.fromLegacyText("§c[Stone] §cSem §c§npermissão §cpara executar esse comando."));

    }
  }
}
