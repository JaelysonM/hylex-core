package com.uzm.hylex.core.bungee.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.bungee.api.HylexPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;


public class TellCommand extends Command {

  private String label;


  public static ArrayList<String> getInvoke() {
    return Lists.newArrayList("tpall");
  }

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /" + label + " <jogador> <mensagem>"));
  }

  public TellCommand() {
    super("tell");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }


    ProxiedPlayer player = (ProxiedPlayer) sender;
    switch (args.length) {
      case 1:
      case 0:
        help(player, label);
        break;
      default:
        HylexPlayer hp = HylexPlayer.getByPlayer((ProxiedPlayer) sender);
        if (!hp.isAccountLoaded()) {
          return;
        }

        if (!hp.getLobbiesContainer().isCanSendTell()) {
          sender.sendMessage(TextComponent.fromLegacyText("§cVocê desativou as mensagens privadas."));
          return;
        }

        String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        ProxiedPlayer target = Bungee.getInstance().getProxy().getPlayer(args[0]);
        if (target == null) {
          sender.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
          break;
        }

        Group group = Group.getPlayerGroup((ProxiedPlayer) sender);
        Group tgroup = Group.getPlayerGroup(target);
        sender.sendMessage(TextComponent.fromLegacyText("§8Mensagem para " + tgroup.getDisplay() + target.getName() + "§7: " + text));
        target.sendMessage(TextComponent.fromLegacyText("§8Mensagem de " + group.getDisplay() + sender.getName() + "§7: " + text));
        hp.setLastMessager(target, 100);
        break;
    }
  }
}
