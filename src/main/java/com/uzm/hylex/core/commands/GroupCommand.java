package com.uzm.hylex.core.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class GroupCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission(Core.getLoader().permissions.get(label.toLowerCase()))) {
      sender.sendMessage("§c[Stone] §cSem §c§npermissão §cpara executar esse comando.");
      return true;
    }

    if (args.length > 0) {
      String action = args[0];
      Group pg = null;
      if (sender instanceof Player) {
        pg = Group.getPlayerGroup((Player) sender);
      }
      if (action.equalsIgnoreCase("list")) {
        StringBuilder sb = new StringBuilder(" \n§eLista de Grupos:");
        for (Group group : Group.values()) {
          if (pg == null || group.ordinal() >= pg.ordinal()) {
            sb.append("\n").append(group.name().toLowerCase());
          }
        }

        sb.append("\n ");
        sender.sendMessage(sb.toString());
        return true;
      } else if (action.equalsIgnoreCase("set") || action.equalsIgnoreCase("remove")) {
        if (args.length <= 1) {
          sender.sendMessage("§cUtilize /group " + action + " <jogador> <grupo>");
          return true;
        }
        if (Bukkit.getPlayerExact(args[1]) != null) {
          Player player = Bukkit.getPlayerExact(args[1]);
          Group group = Group.getGroup(args[2]);
          if (group == null) {
            sender.sendMessage("§cGrupo não encontrado, utilize /group list para ver os grupos.");
            return true;
          }

          if (pg != null) {
            // grupo selecionado é superior ao do executer
            if (group.ordinal() < pg.ordinal()) {
              sender.sendMessage("§cVocê não possui permissão para realizar esta ação.");
              return true;
            }

            if (pg.ordinal() > HylexPlayer.getByPlayer(player).getGroup().ordinal()) {
              sender.sendMessage("§cVocê não possui permissão para realizar esta ação.");
              return true;
            }

            // grupo do alvo é maior ou igual ao do executer
          }

          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[1] + " parent " + (action.equalsIgnoreCase("set") ? "add" : "remove") + " " + group.getLpName());
          sender.sendMessage("§aVocê " + (action.equalsIgnoreCase("set") ? "setou" : "removeu") + " o grupo do jogador com sucesso.");
          return true;
        } else {
          Group group = Group.getGroup(args[2]);
          if (group == null) {
            sender.sendMessage("§cGrupo não encontrado, utilize /group list para ver os grupos.");
            return true;
          }

          if (pg != null) {
            // grupo selecionado é superior ao do executer
            if (group.ordinal() < pg.ordinal()) {
              sender.sendMessage("§cVocê não possui permissão para realizar esta ação.");
              return true;
            }

            // grupo do alvo é maior ou igual ao do executer
          }

          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[1] + " parent " + (action.equalsIgnoreCase("set") ? "add" : "remove") + " " + group.getLpName());
          sender.sendMessage("§aVocê " + (action.equalsIgnoreCase("set") ? "setou" : "removeu") + " o grupo do jogador com sucesso.");
          return true;
        }

      }


    }

    help(sender);
    return true;
  }

  public static List<String> getInvoke() {
    return Lists.newArrayList("group");
  }

  public void help(CommandSender sender) {
    sender.sendMessage("");
    sender.sendMessage("   §eAjuda do comando §f'group'");
    sender.sendMessage("");
    sender.sendMessage("  §e- §f/group set <player> <grupo> §7Adicionar um grupo ao jogador.");
    sender.sendMessage("  §e- §f/group remove <player> <grupo> §7Remover um grupo do jogador.");
    sender.sendMessage("  §e- §f/group list §7Ver a lista de grupos disponíveis.");
    sender.sendMessage("");
  }
}
