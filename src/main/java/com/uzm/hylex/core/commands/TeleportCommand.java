package com.uzm.hylex.core.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.utils.HylexMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§fHey brother, stop do it! You cannot execute commands.");
      return true;
    }

    Player player = (Player) sender;
    HylexPlayer hp = HylexPlayer.getByPlayer(player);
    if (!player.hasPermission(Core.getLoader().permissions.get(label.toLowerCase()))) {
      player.sendMessage("§b[Hylex] §cSem §c§npermissão §cpara executar esse comando.");
      return true;
    }

    if (label.equalsIgnoreCase("tp")) {
      switch (args.length) {
        case 1:
          Player found = Bukkit.getPlayer(args[0]);

          if (found != null) {
            player.sendMessage("§eVocê foi teletransportado para §f§n" + found.getName() + "§e.");
            player.teleport(found);
            hp.broadcastAction(HylexMethods.StaffAction.TELEPORTTO, found.getName(), "");
          } else {
            player.sendMessage(
              HylexMethods.isNumeric(args[0]) || args[0].startsWith("~") ? "§c* Use: /tp <x> <y> <z>" : "§c[Hylex] '§f" + args[0] + "§c' §cnão está conectado a este servidor.");
          }
          break;
        case 2:
          Player tp = Bukkit.getPlayer(args[0]);
          Player tp1 = Bukkit.getPlayer(args[1]);
          if (tp != null && tp1 != null) {
            player.sendMessage("§eVocê forçou o teletransporte de §f§n" + tp.getName() + "§e para §f" + tp1.getName() + "§e.");
            tp.teleport(tp1);
            hp.broadcastAction(HylexMethods.StaffAction.TELEPORT, tp.getName(), tp1.getName());
          } else {
            List<String> offline = Lists.newArrayList();
            if (tp == null) {
              offline.add(args[0]);
            }
            if (tp1 == null) {
              offline.add(args[1]);
            }

            player.sendMessage((HylexMethods.isNumeric(args[0]) && HylexMethods.isNumeric(args[1])) || (args[0].startsWith("~") && args[1].startsWith("~")) ?
              "§c* Use: /tp <x> <y> <z>" :
              "§c[Hylex] '§f" + String.join(", ", offline) + "§c' §cnão está(ão) conectado(s) a este servidor.");
          }
          break;
        case 3:

          if (HylexMethods.isNumeric(args[0].replace("~", "0")) && HylexMethods.isNumeric(args[1].replace("~", "0")) && HylexMethods.isNumeric(args[2].replace("~", "0"))) {
            int x = (int) (args[0].startsWith("~") ? (Integer.parseInt(args[0].replace("~", "")) + player.getLocation().getX()) : Integer.parseInt(args[0]));
            int y = (int) (args[1].startsWith("~") ? (Integer.parseInt(args[1].replace("~", "")) + player.getLocation().getY()) : Integer.parseInt(args[1]));
            int z = (int) (args[2].startsWith("~") ? (Integer.parseInt(args[2].replace("~", "")) + player.getLocation().getZ()) : Integer.parseInt(args[2]));
            String[] array = {x + "", y + "", z + ""};
            player.sendMessage("§eVocê foi teletransportado para §f§n" + String.join(", ", array) + "§e.");
            player.teleport(new Location(player.getWorld(), x, y, z));

            hp.broadcastAction(HylexMethods.StaffAction.TELEPORTTO, String.join(", ", array), "");
          } else {
            player.sendMessage("§c[Hylex] As coordenadas cedidas devem ser números.");
          }
          break;
        case 4:
          Player target = Bukkit.getPlayer(args[0]);
          if (target != null) {
            if (HylexMethods.isNumeric(args[1].replace("~", "0")) && HylexMethods.isNumeric(args[2].replace("~", "0")) && HylexMethods.isNumeric(args[3].replace("~", "0"))) {
              int x = (int) (args[1].startsWith("~") ? (Integer.parseInt(args[1].replace("~", "")) + player.getLocation().getX()) : Integer.parseInt(args[1]));
              int y = (int) (args[2].startsWith("~") ? (Integer.parseInt(args[2].replace("~", "")) + player.getLocation().getY()) : Integer.parseInt(args[2]));
              int z = (int) (args[3].startsWith("~") ? (Integer.parseInt(args[3].replace("~", "")) + player.getLocation().getZ()) : Integer.parseInt(args[3]));
              String[] array = {x + "", y + "", z + ""};
              player.sendMessage("§eVocê forçou o teletransporte de §f§n" + target.getName() + "§e para §f" + String.join(", ", array) + "§e.");
              player.teleport(new Location(player.getWorld(), x, y, z));

              hp.broadcastAction(HylexMethods.StaffAction.TELEPORT, target.getName(), String.join(", ", array));
            } else {
              player.sendMessage("§c[Hylex] As coordenadas cedidas devem ser números.");
            }
          } else {
            player.sendMessage("§c[Hylex] '§f" + args[0] + "§c' §cnão está conectado a este servidor.");
          }
          break;
        default:
          help(player, label);
          break;
      }
    }
    return false;
  }

  public static List<String> getInvoke() {
    return Lists.newArrayList("tp");
  }

  public void help(Player player, String label) {
    player.sendMessage("");
    player.sendMessage("   §eAjuda do comando §f'" + label + "'");
    player.sendMessage("");
    player.sendMessage("  §e- §f/" + label + " <nick>  §7Teleporte-se para um jogador.");
    player.sendMessage("  §e- §f/" + label + " <nick> <nick> §7Teleporte um jogador até outro.");
    player.sendMessage("  §e- §f/" + label + " <x> <y> <z> §7Teleporte-se para uma localização específica.");
    player.sendMessage("  §e- §f/" + label + " <nick> <x> <y> <z> §7Teleporte um jogador para uma localização específica.");
    player.sendMessage("");
  }
}
