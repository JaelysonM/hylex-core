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

import java.util.ArrayList;
import java.util.UUID;


public class TpallCommand implements CommandExecutor {

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

    if (label.equalsIgnoreCase("tpall")) {
      switch (args.length) {
        case 0:
          for (Player players : Bukkit.getOnlinePlayers()) {
            if (players != player) {
              players.teleport(player.getLocation());
            }
          }

          player.sendMessage("§eVocê teletransportou §f§ntodos os jogadores§e §edo servidor até você§e.");
          hp.broadcastAction(HylexMethods.StaffAction.TPALL, player.getName(), "jogadores");
          break;
        case 1:
          String toLower = args[0].toLowerCase();
          switch (toLower) {
            case "staff":
              for (UUID uuid : HylexPlayer.STAFF) {
                Player players = Bukkit.getPlayer(uuid);
                if (players != null && players != player) {
                  players.teleport(player);
                }
              }
              player.sendMessage("§eVocê teletransportou §f§ntodos os 'staffs'§e §edo servidor até você§e.");
              hp.broadcastAction(HylexMethods.StaffAction.TPALL, player.getName(), "staffs");
              break;
            case "p":
              for (Player players : Bukkit.getOnlinePlayers()) {
                if (!HylexPlayer.STAFF.contains(players.getUniqueId())) {
                  players.teleport(player);
                }
              }

              player.sendMessage("§eVocê teletransportou §f§ntodos os 'no/staffs'§e §edo servidor até você§e.");
              hp.broadcastAction(HylexMethods.StaffAction.TPALL, player.getName(), "no/staffs");
              break;
            default:
              help(player, label);
              break;
          }
          break;
        default:
          help(player, label);
          break;
      }
    }
    return false;
  }

  public static ArrayList<String> getInvoke() {
    return Lists.newArrayList("tpall");
  }

  public void help(Player player, String label) {


    player.sendMessage("");
    player.sendMessage("   §eAjuda do comando §f'" + label + "'");
    player.sendMessage("");
    player.sendMessage("  §e- §f/" + label + "  §7Teleporte todos os jogadores e staffs até você.");
    player.sendMessage("  §e- §f/" + label + " p §7Teleporte todos os jogadores até você.");
    player.sendMessage("  §e- §f/" + label + " staff §7Teleporte toda a staff até você.");
    player.sendMessage("");
  }

}
