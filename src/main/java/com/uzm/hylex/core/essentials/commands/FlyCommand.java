package com.uzm.hylex.core.essentials.commands;

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

public class FlyCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§fHey brother, stop do it! You cannot execute commands.");
      return true;
    }

    Player player = (Player) sender;
    HylexPlayer hp = HylexPlayer.getByPlayer(player);
    if (Core.DISABLE_FLY && !player.hasPermission("hylex.staff")) {
      player.sendMessage("§b[Hylex] §cComando desabilitado.");
      return true;
    }
    if (!player.hasPermission(Core.getLoader().permissions.get(label.toLowerCase()))) {
      player.sendMessage("§b[Hylex] §cSem §c§npermissão §cpara executar esse comando.");
      return true;
    }

    switch (args.length) {
      case 0:
        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(player.getAllowFlight() ? "§eVocê ativou o seu modo de vôo." : "§eVocê desativou o seu modo de vôo.");
        hp.broadcastAction(HylexMethods.StaffAction.FLY_PERPLAYER, player.getName(), player.getAllowFlight() ? "Voando" : "Não voando");
        break;
      case 1:
        if (player.getPlayer().hasPermission("hylex.setfly")) {
          Player target = Bukkit.getPlayerExact(args[0]);
          if (target != null) {
            target.setAllowFlight(!target.getAllowFlight());
            target.sendMessage(target.getAllowFlight() ? "§eVocê ativou o modo de vôo do jogador." : "§eVocê desativou o modo de vôo do jogador.");
            hp.broadcastAction(HylexMethods.StaffAction.FLY, target.getName(), target.getAllowFlight() ? "Voando" : "Não voando");
          } else {
            player.sendMessage("§c[Hylex] '§f" + args[0] + "§c' §cnão está conectado a este servidor.");
          }
        } else {
          help(player.getPlayer(), label);
        }
        break;
      default:
        help(player.getPlayer(), label);
        break;
    }
    return false;
  }

  public static List<String> getInvoke() {
    return Lists.newArrayList("voar", "fly");
  }

  public void help(Player player, String label) {
    player.sendMessage("");
    player.sendMessage("   §eAjuda do comando §f'" + label + "'");
    player.sendMessage("");
    if (player.hasPermission("hylex.setfly")) {
      player.sendMessage("  §e- §f/" + label + " <nome> §7Altere o modo vôo de um jogador.");
    }
    player.sendMessage("  §e- §f/" + label + " §7Altere seu modo de vôo.");
    player.sendMessage("");
  }
}
