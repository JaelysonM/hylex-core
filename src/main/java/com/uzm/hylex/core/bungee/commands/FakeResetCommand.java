package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.controllers.FakeController;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Maxter
 */
public class FakeResetCommand extends Command {
  private String label;

  public FakeResetCommand() {
    super("faker");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("hylex.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }

    if (!FakeController.isFake(player.getName())) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não está utilizando um nickname falso."));
      return;
    }
    if (player.getServer().getInfo().getName().contains("mega")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }

    FakeController.removeFake(player);
  }
}