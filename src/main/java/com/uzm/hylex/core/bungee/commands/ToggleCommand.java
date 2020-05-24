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


public class ToggleCommand extends Command {

  private String label;


  public static ArrayList<String> getInvoke() {
    return Lists.newArrayList("toggle");
  }

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText(
      " \n   §eAjuda do comando §f'" + "report" + "'\n" +
        "\n  §e- §f/toggle <tipo> <boleano >§7Troque as preferências." +
       "\n "
    ));
  }

  public ToggleCommand() {
    super("toggle");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }


    ProxiedPlayer player = (ProxiedPlayer) sender;

    HylexPlayer hp = HylexPlayer.getByPlayer(player);

    if (hp ==null) {
      if (!hp.isAccountLoaded()) {
        return;
      }
    }

    switch (args.length) {
      case 2:
        Boolean b = Boolean.parseBoolean(args[1]);
        switch (args[0].toLowerCase()) {
          case "tell":
               hp.getLobbiesContainer().setToggleTell(b);
            break;
          case "report":
            if (player.hasPermission("hylex.staff")) {
              hp.getLobbiesContainer().setToggleReport(b);
              break;
            }
          case "party":
            hp.getLobbiesContainer().setToggleParty(b);
            break;
        }
        break;
      default:
        help(player, label);
        break;
    }
  }
}
