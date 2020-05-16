package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.configuration.ConfigurationCreator;
import com.uzm.hylex.core.bungee.controllers.MotdController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;


public class MotdCommand extends Command {

  private String label;

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText(""));
    player.sendMessage(TextComponent.fromLegacyText("   §eAjuda do comando §f'" + label + "'"));
    player.sendMessage(TextComponent.fromLegacyText(""));
    player.sendMessage(TextComponent.fromLegacyText("  §e- §f/" + label + " list §7Liste todas as motds."));
    player.sendMessage(TextComponent
      .fromLegacyText("  §e- §f/" + label + " add <motd> §7Adicione na lista uma motd. §8(<newline> Nova linha; <online> Jogadores online; <max> Máximo de jogadorez )"));
    player.sendMessage(TextComponent.fromLegacyText("  §e- §f/" + label + " clear §7Limpe a lista de motds."));
    player.sendMessage(TextComponent.fromLegacyText(""));
  }

  public MotdCommand() {
    super("motd");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    if (!sender.hasPermission("hylex.motd")) {
      sender.sendMessage(TextComponent.fromLegacyText("§b[Hylex] §cSem §c§npermissão §cpara executar esse comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;

    switch (args.length) {
      case 0:
        help(player, label);
        break;
      case 1:
        switch (args[0].toLowerCase()) {
          case "list":
            player.sendMessage(TextComponent.fromLegacyText(""));
            player.sendMessage(TextComponent.fromLegacyText("§e✉ §eMotds que §f§naparecerão§a no servidor: "));
            player.sendMessage(TextComponent.fromLegacyText(""));
            MotdController.getMotds().forEach(motd -> {
              player.sendMessage(TextComponent.fromLegacyText("§6- §8[" + ChatColor.translateAlternateColorCodes('&', motd) + "§8]"));
            });
            break;
          case "clear":
            player.sendMessage(TextComponent.fromLegacyText("§e✄ §7Você limpou as motds do servidor."));
            MotdController.getMotds().clear();
            ConfigurationCreator.find("setup").get().set("motds", MotdController.getMotds());
            ConfigurationCreator.find("setup").save();

            break;
          default:
            help(player, label);
            break;
        }
        break;
      default:
        switch (args[0].toLowerCase()) {
          case "add":
            String text = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ").replace("&", "§");
            TextComponent textC = new TextComponent("§e* §7Você §aadicionou §7uma motd da lista ");
            TextComponent hover = new TextComponent("§e§lPASSE PARA VER-LA");
            hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(text)));
            textC.addExtra(hover);
            player.sendMessage(textC);
            MotdController.getMotds().add(text);
            ConfigurationCreator.find("setup").get().set("motds", MotdController.getMotds());
            ConfigurationCreator.find("setup").save();
            break;
          default:
            help(player, label);
            break;
        }
        break;
    }

  }
}
