package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.bungee.api.ReportType;
import com.uzm.hylex.core.java.util.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.chat.ComponentSerializer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SendReportCommand extends Command {

  public static final Map<UUID, Long> DELAY = new HashMap<>();
  private static final SimpleDateFormat DF = new SimpleDateFormat("mm:ss");

  public SendReportCommand() {
    super("sendr");
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
      player.sendMessage(TextComponent.fromLegacyText("§cVocê precisa aguardar " + (current - System.currentTimeMillis()) / 1000 + " segundos para enviar uma nova denúncia."));
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

    //ReportType rt = ReportType.fromName(StringUtils.join(args, 1, " "));
    ReportType rt = ReportType.fromValue(args[1]);
    if (rt == null) {
      player.sendMessage(ComponentSerializer.parse(ReportType.REASONS.toString().replace("{player}", args[0])));
      return;
    }

    String proof = null;
    if (rt.isRequiresProof()) {
      proof = args[2];
    }
    boolean needsProof = proof != null;
    DELAY.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(150));
    player.sendMessage(TextComponent.fromLegacyText("§aSua denúncia foi enviada para nossa Equipe."));

    String mini = null;
    if (HylexPlayer.getByPlayer(pp) != null)
      mini = HylexPlayer.getByPlayer(pp).getCurrentMini();

    TextComponent report = new TextComponent(
      "\n     §e[!] §7Nova denúncia §f" + pp.getName() + "" + "\n §8(" + (needsProof ? "Denúncia com prova" : "Denúncia sem provas visuais") + ")\n\n§7  Quem enviou §f" + player
        .getName() + "\n§7  Motivo §f" + rt.getName() + "\n  §7Servidor §f" + (pp.getServer().getInfo().getName().contains("mega") ?
        pp.getServer().getInfo().getName() + "-" + mini :
        pp.getServer().getInfo().getName()));
    if (needsProof) {
      report.addExtra("\n§7  Prova §f'" + args[2] + "'");
    }
    TextComponent hover = new TextComponent("\n\n   §a§nVá até la!\n ");
    hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClique para ir até §f" + pp.getName())));

    String command = null;
    if (pp.getServer().getInfo().getName().contains("mega") && mini == null) {
      command = "/server " + pp.getServer().getInfo().getName();

    } else {
      command = "/mtp " + pp.getServer().getInfo().getName() + " " + mini;
    }
    hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

    report.addExtra(hover);
    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
      if (players.hasPermission("hylex.staff")) {
        HylexPlayer hp = HylexPlayer.getByPlayer(players);
        if (hp != null) {
          if (hp.isAccountLoaded()) {
            if (!hp.getLobbiesContainer().canSendReport()) {
              return;
            }
            players.sendMessage(report);
          }

        }

      }
    }
  }
}
