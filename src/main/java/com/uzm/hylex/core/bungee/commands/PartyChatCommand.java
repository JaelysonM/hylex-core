package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.bungee.party.BungeeParty;
import com.uzm.hylex.core.bungee.party.BungeePartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyChatCommand extends Command {

  public PartyChatCommand() {
    super("p");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /p [mensagem] para conversar com a sua Party."));
      return;
    }

    BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
    if (party == null) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
      return;
    }

    party.broadcast("§d[Party] " + Group.getColored(player.getName()) + "§f: " + String.join(" ", args));
  }
}
