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
import java.util.List;


public class RCommand extends Command {

  private String label;


  public static ArrayList<String> getInvoke() {
    return Lists.newArrayList("r");
  }

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /" + label + " <mensagem>"));
  }

  public RCommand() {
    super("r");
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
      case 0:
        help(player, label);
        break;
      default:
        HylexPlayer hp = HylexPlayer.getByPlayer((ProxiedPlayer) sender);
        if (!hp.isAccountLoaded()) {
          return;
        }
        List<HylexPlayer> lastTell = hp.getLastMessager();

        if (!hp.getLobbiesContainer().canSendTell()) {
          sender.sendMessage(TextComponent.fromLegacyText("§cVocê desativou as mensagens privadas."));
          return;
        }
        if (lastTell.isEmpty()) {
          sender.sendMessage(TextComponent.fromLegacyText("§cNão há nenhuma mensagem para ser respondida."));
          return;
        }

        String text = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        HylexPlayer target = lastTell.get(0);



        if (Bungee.getInstance().getProxy().getPlayer(target.getName()) == null) {
          sender.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
          return;
        }
        Group group = Group.getPlayerGroup((ProxiedPlayer) sender);
        Group tgroup = Group.getPlayerGroup(target.getPlayer());
        sender.sendMessage(TextComponent.fromLegacyText("§8Mensagem para " + tgroup.getDisplay() + target.getName() + "§7: " + text));
        target.getPlayer().sendMessage(TextComponent.fromLegacyText("§8Mensagem de " + group.getDisplay() + sender.getName() + "§7: " + text));
        break;
    }
  }
}
