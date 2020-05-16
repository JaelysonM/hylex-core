package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.bungee.api.ReportType;
import com.uzm.hylex.core.java.util.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.chat.ComponentSerializer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command {

  public static final Map<UUID, Long> DELAY = new HashMap<>();
  private static final SimpleDateFormat DF = new SimpleDateFormat("mm:ss");

  public ReportCommand() {
    super("report");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    long current = DELAY.getOrDefault(player.getUniqueId(), 0L);
    if (current > System.currentTimeMillis()) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê precisa aguardar " + DF.format(current / 1000) + " segundos para enviar uma nova denúncia."));
      return;
    }

    DELAY.remove(player.getUniqueId());
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /report <jogador>"));
      return;
    }

    ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(args[0]);
    if (pp == null) {
      player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
      return;
    }

    ReportType rt = ReportType.fromName(StringUtils.join(args, 1, " "));
    if (rt == null) {
      player.sendMessage(ComponentSerializer.parse(ReportType.REASONS.toString().replace("{player}", args[0])));
      return;
    }

    String proof = null;
    if (rt.isRequiresProof()) {
      if (args.length <= rt.getProofId()) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /report " + args[0] + " " + rt.getName() + " <prova>"));
        return;
      }

      proof = args[rt.getProofId()];
    }

    DELAY.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(150));
    player.sendMessage(TextComponent.fromLegacyText("§aSua denúncia foi enviada para nossa Equipe."));
    TextComponent report = new TextComponent("");
    for (BaseComponent component : TextComponent
      .fromLegacyText(" \n §b§lNOVA DENÚNCIA\n \n §aO jogador(a) " + Group.getColored(player) + " §aacusou " + Group.getColored(pp) + " §ade Cheating!")) {
      report.addExtra(component);
    }
    if (proof != null) {
      for (BaseComponent component : TextComponent.fromLegacyText("\n §aProva: §f" + proof)) {
        report.addExtra(component);
      }
    }
    for (BaseComponent component : TextComponent.fromLegacyText("\n ")) {
      report.addExtra(component);
    }
    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
      if (players.hasPermission("hylex.staff")) {
        players.sendMessage(report);
      }
    }
  }
}
