package com.uzm.hylex.core.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.bungee.controllers.FakeController;
import com.uzm.hylex.core.skins.bungee.listeners.LoginListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class FakeCommand extends Command {

  private String label;


  public FakeCommand() {
    super("fake");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("hylex.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }

    if (player.getServer().getInfo().getName().contains("mega")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }
    String fakeName = args.length > 0 ? args[0] : null;
    if (args.length == 0) {
      List<String> list = FakeController.getRandomNicks().stream().filter(FakeController::isUsable).collect(Collectors.toList());
      Collections.shuffle(list);
      fakeName = list.stream().findFirst().orElse(null);
      if (fakeName == null) {
        player.sendMessage(TextComponent.fromLegacyText(" \n §cNenhum nickname aleatório está disponível no momento.\n §cVocê pode utilizar um nome diferente através do comando /fake [nome]\n "));
        return;
      }
    }

    if (!FakeController.isUsable(fakeName)) {
      player.sendMessage(TextComponent.fromLegacyText("§cEste nickname falso não está disponível para uso."));
      return;
    }

    if (fakeName.length() > 16 || fakeName.length() < 4) {
      player.sendMessage(TextComponent.fromLegacyText("§cO nickname falso precisa conter de 4 a 16 caracteres."));
      return;
    }

    if (!LoginListener.validUsername(fakeName)) {
      player.sendMessage(TextComponent.fromLegacyText("§cO nickname falso não pode conter caracteres especiais."));
      return;
    }

    FakeController.applyFake(player, fakeName);
    player.sendMessage(TextComponent.fromLegacyText("§eVocê está disfarçado como: §f" + fakeName ));

  }


}
