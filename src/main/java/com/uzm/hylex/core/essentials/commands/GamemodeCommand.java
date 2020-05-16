package com.uzm.hylex.core.essentials.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.utils.HylexMethods;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class GamemodeCommand implements CommandExecutor {

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

    if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
      switch (args.length) {
        case 1:
          switch (args[0].toLowerCase()) {
            case "creative":
            case "1":
              if (player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }

              player.sendMessage("§eSeu modo de jogo foi alterado para §f§nCREATIVE§e.");
              player.setGameMode(GameMode.CREATIVE);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE_PERPLAYER, player.getName(), "CREATIVE");
              break;
            case "survival":
            case "0":
              if (player.getGameMode() == GameMode.SURVIVAL) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }
              player.sendMessage("§eSeu modo de jogo foi alterado para §f§nSURVIVAL§e.");
              player.setGameMode(GameMode.SURVIVAL);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE_PERPLAYER, player.getName(), "CRIATIVO");
              break;
            case "spectator":
            case "3":
              if (player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }
              player.sendMessage("§eSeu modo de jogo foi alterado para §f§nESPECTADOR§e.");
              player.setGameMode(GameMode.SPECTATOR);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE_PERPLAYER, player.getName(), "SPECTATOR");
              break;
            case "adventure":
            case "2":
              if (player.getGameMode() == GameMode.ADVENTURE) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }
              player.sendMessage("§eSeu modo de jogo foi alterado para §f§nADVENTURE§e.");
              player.setGameMode(GameMode.ADVENTURE);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE_PERPLAYER, player.getName(), "ADVENTURE");
              break;
            default:
              help(player, label);
              break;
          }
          break;
        case 2:
          Player target = Bukkit.getPlayer(args[0]);
          if (target == null) {
            player.sendMessage("§c[Hylex] '§f" + args[0] + "§c' §cnão está conectado a este servidor.");
            return true;
          }

          switch (args[1].toLowerCase()) {
            case "creative":
            case "1":
              if (target.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }
              player.sendMessage("§eVocê alterou o modo de jogo de §f" + target.getName() + " §epara §f§nCREATIVE§e.");
              target.sendMessage("§eSeu modo de jogo foi alterado para §f§nCREATIVE§e por §f" + player.getName() + "§e.");
              target.setGameMode(GameMode.CREATIVE);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE, target.getName(), "CREATIVE");
              break;
            case "survival":
            case "0":
              if (target.getGameMode() == GameMode.SURVIVAL) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }

              player.sendMessage("§eVocê alterou o modo de jogo de §f" + target.getName() + " §epara §f§nSURVIVAL§e.");
              target.sendMessage("§eSeu modo de jogo foi alterado para §f§nSURVIVAL§e por §f" + player.getName() + "§e.");
              target.setGameMode(GameMode.SURVIVAL);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE, target.getName(), "CRIATIVO");
              break;
            case "spectator":
            case "3":
              if (target.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }

              player.sendMessage("§eVocê alterou o modo de jogo de §f" + target.getName() + " §epara §f§nESPECTADOR§e.");
              target.sendMessage("§eSeu modo de jogo foi alterado para §f§nESPECTADOR§e por §f" + player.getName() + "§e.");
              target.setGameMode(GameMode.SPECTATOR);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE, target.getName(), "SPECTATOR");
              break;
            case "adventure":
            case "2":
              if (target.getGameMode() == GameMode.ADVENTURE) {
                player.sendMessage("§c[Hylex] Você já está nesse modo de jogo.");
                return true;
              }

              player.sendMessage("§eVocê alterou o modo de jogo de §f" + target.getName() + " §epara §f§nADVENTURE§e.");
              target.sendMessage("§eSeu modo de jogo foi alterado para §f§nADVENTURE§e por §f" + player.getName() + "§e.");
              target.setGameMode(GameMode.ADVENTURE);
              hp.broadcastAction(HylexMethods.StaffAction.GAMEMODE, target.getName(), "ADVENTURE");
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
    return true;
  }

  public static List<String> getInvoke() {
    return Lists.newArrayList("gm", "gamemode");
  }

  public void help(Player player, String label) {
    player.sendMessage("");
    player.sendMessage("   §eAjuda do comando §f'" + label + "'");
    player.sendMessage("");
    player.sendMessage("  §e- §f/" + label + " <0|survival, 1|creative, 2|adventure, 3|spectator> §7Altere o seu modo de jogo.");
    player.sendMessage("  §e- §f/" + label + " <nick> <0|survival, 1|creative, 2|adventure, 3|spectator> §7Altere o modo de jogo de um jogador.");
    player.sendMessage("");
  }
}
