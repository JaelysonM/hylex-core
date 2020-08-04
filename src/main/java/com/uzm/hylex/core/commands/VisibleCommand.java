package com.uzm.hylex.core.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.utils.HylexMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class VisibleCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§fHey brother, stop do it! You cannot execute commands.");
      return true;
    }

    Player player = (Player) sender;
    HylexPlayer hp = HylexPlayer.getByPlayer(player);
    if (!player.hasPermission("hylex.staff")) {
      player.sendMessage("§c[Stone] §cSem §c§npermissão §cpara executar esse comando.");
      return true;
    }

    switch (args.length) {
      case 0:
        hp.setVisibility(!hp.isInvisible());
        player.sendMessage(hp.isInvisible() ? "§aAgora você está invisível para jogadores normais." : "§eAgora você está visível para todos os jogadores.");
        hp.broadcastAction(HylexMethods.StaffAction.INVISIBLE, player.getName(), hp.isInvisible() ? "Invisível" : "Visível");
        for (Player pls : Bukkit.getOnlinePlayers()) {
          if (hp.isInvisible()) {
            if (!pls.hasPermission("hylex.staff"))
            pls.hidePlayer(player);
          } else {
              pls.showPlayer(player);
          }

        }
        break;
      default:
        help(player.getPlayer(), label);
        break;
    }
    return false;
  }

  public static List<String> getInvoke() {
    return Lists.newArrayList("v", "vis");
  }

  public void help(Player player, String label) {
    player.sendMessage("");
    player.sendMessage("   §eAjuda do comando §f'" + label + "'");
    player.sendMessage("");
    player.sendMessage("  §e- §f/" + label + " §7Altere seu modo de visibilidade.");
    player.sendMessage("");
  }
}
