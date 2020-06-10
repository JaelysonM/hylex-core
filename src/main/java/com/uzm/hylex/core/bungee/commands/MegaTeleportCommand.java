package com.uzm.hylex.core.bungee.commands;

import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.services.lan.WebSocket;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class MegaTeleportCommand extends Command {

  private String label;


  public MegaTeleportCommand() {
    super("mtp");
    this.label = this.getName();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }
    if (!sender.hasPermission("hylex.staff")) {
      sender.sendMessage(TextComponent.fromLegacyText("§c[Stone] §cSem §c§npermissão §cpara executar esse comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    switch (args.length) {
      case 1:
      case 0:
        help(player, label);
        break;
      default:
        String mega = args[0];
        String mini = args[1];
        if (!player.getServer().getInfo().getName().equals(mega)) {
          player.connect(Bungee.getInstance().getProxy().getServerInfo(mega));
        }
        JSONObject json = new JSONObject();
        JSONArray players = new JSONArray();
        players.add(player.getName());
        json.put("players", players);
        json.put("name", mini);
        json.put("clientName", "core-bedwars-" + mega);

        WebSocket.get("core-bungee").getSocket().emit("join-mini-force", json);
        break;
    }

  }

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText("§cUtilize /" + label + " <servidor> <mini>"));
  }

}
