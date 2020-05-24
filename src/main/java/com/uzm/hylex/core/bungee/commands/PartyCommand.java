package com.uzm.hylex.core.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.api.Group;
import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.bungee.party.BungeeParty;
import com.uzm.hylex.core.bungee.party.BungeePartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.stream.Collectors;

public class PartyCommand extends Command {

  public PartyCommand() {
    super("party");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
     help(player);
     return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("aceitar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party aceitar [jogador]"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode aceitar convites de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + target + " não é um Líder de Party."));
        return;
      }

      target = party.getName(target);
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + target + " está lotada."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Group.getColored(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("ajuda")) {
      help(player);
    } else if (action.equalsIgnoreCase("deletar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }

      party.broadcast(" \n" + Group.getColored(player.getName()) + " §adeletou a Party!\n ", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§aVocê deletou a Party."));
    } else if (action.equalsIgnoreCase("puxar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da Party."));
        return;
      }

      if (Bungee.getInstance().getProxy().getPlayer(party.getLeader()).getServer().getInfo().getName().contains("mega")) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode puxar os jogadores para esse servidor."));
        return;
      }

      party.broadcast(" \n" + Group.getColored(player.getName()) + " §apuxou todos os jogadores para o seu servidor!\n ", true);

      player.sendMessage(TextComponent.fromLegacyText(""));
      player.sendMessage(TextComponent.fromLegacyText("§e* §aVocê puxou todos os jogadores para o seu servidor."));
      player.sendMessage(TextComponent.fromLegacyText(""));

      ServerInfo server = Bungee.getInstance().getProxy().getPlayer(party.getLeader()).getServer().getInfo();
      if (server != null) {
        party.listMembers().forEach(pp -> {
          ProxiedPlayer p = ProxyServer.getInstance().getPlayer(pp.getName());
          if (player.getServer().getInfo().getName().equals("auth")) {
            return;
          }
          if (pp.getName().equalsIgnoreCase(party.getLeader())) {
            return;
          }
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF("SendPartyMember");
          out.writeUTF(p.getName());
          out.writeUTF(server.getName());
          out.writeUTF(server.getName());
          out.writeUTF(p.getServer().getInfo() == server?"INSIDE": "OUTSIDE");
          p.getServer().sendData("hylex-core", out.toByteArray());
          p.sendMessage(TextComponent.fromLegacyText(""));
          p.sendMessage(TextComponent.fromLegacyText("§e* §f" + player.getName() + " §apuxou você para o servidor dele."));
          p.sendMessage(TextComponent.fromLegacyText(""));
        });
      }
    } else if (action.equalsIgnoreCase("expulsar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party expulsar [jogador]"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um Líder de Party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode se expulsar."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
        return;
      }

      target = party.getName(target);
      party.kick(target);
      party.broadcast(" \n" + Group.getColored(player.getName()) + " §aexpulsou " + Group.getColored(target) + " §ada Party!\n ");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }

      List<String> members = party.listMembers().stream().filter(pp -> !party.getLeader().equalsIgnoreCase(pp.getName())).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName())
        .collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
        " \n§6Líder: " + Group.getColored(party.getLeader()) + "\n§6Limite de Membros: §f" + party.listMembers().size() + "/" + party.getSlots() + "\n§6Membros: " + String
          .join("§7, ", members) + "\n "));
    } else if (action.equalsIgnoreCase("negar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party negar [jogador]"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode negar convites de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + target + " não é um Líder de Party."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + target + " não convidou você para Party."));
        return;
      }

      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê negou o convite de Party de " + Group.getColored(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("sair")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
        return;
      }

      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§aVocê saiu da Party!"));
    } else if (action.equalsIgnoreCase("transferir")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party transferir [jogador]"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um Líder de Party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode transferir a Party para você mesmo."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
        return;
      }

      target = party.getName(target);
      party.transfer(target);
      party.broadcast(" \n" + Group.getColored(player.getName()) + " §atransferiu a liderança da Party para " + Group.getColored(target) + "§a!\n ");
    } else {
      if (action.equalsIgnoreCase("convidar")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party convidar [jogador]"));
          return;
        }

        action = args[1];
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
        return;
      }

      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode enviar convites para você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cApenas o Líder da Party pode enviar convites!"));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA sua Party está lotada."));
        return;
      }

      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já enviou um convite para " + action + "."));
        return;
      }

      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + action + " já pertence a uma Party."));
        return;
      }

      HylexPlayer hp = HylexPlayer.getByPlayer(target);
      if (hp!=null) {
        if (!hp.getLobbiesContainer().canSendParty()) {
          player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode convidar esse jogador."));
          return;
        }
        target.sendMessage(party.invite(target.getName()));
        player.sendMessage(
          TextComponent.fromLegacyText(" \n" + Group.getColored(action) + " §afoi convidado para a Party. Ele tem 60 segundos para aceitar ou negar esta solicitação.\n "));

      }else {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode convidar esse jogador."));
      }
    }
  }

  public void help(ProxiedPlayer player) {
    player.sendMessage(TextComponent.fromLegacyText(" \n   §eAjuda do comando §f'" + "party" + "'\n" +
      "\n  §e- §f/party ajuda §7Veja todos os sub-comandos." +
      "\n  §e- §f/p <mensagem> §7Envie uma mensagem no chat privado da sua party." +
      "\n  §e- §f/party aceitar <jogador> §7Aceite o convite de party de um jogador." +
      "\n  §e- §f/party negar <jogador> §7Negue o convite de party de um jogador." +
      "\n  §e- §f/party deletar <jogador> §7Delete a party." +
      "\n  §e- §f/party expulsar <jogador> §7Expulse um jogador da sua party." +
      "\n  §e- §f/party sair §7Saia da sua party." +
      "\n  §e- §f/party transferir <jogador> §7Transfira a party para outro jogador." +
      "\n  §e- §f/party info §7Veja um resumo básico da sua party." +
      "\n  §e- §f/party convidar §7Veja todos os sub-comandos.\n "));

  }
}
