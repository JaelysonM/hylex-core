package com.uzm.hylex.core.bungee.loaders;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.bungee.controllers.QueueController;
import com.uzm.hylex.services.lan.WebSocket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.UUID;

public class ServicesLoader {

  public ServicesLoader(Plugin loader) {
    Configuration config = com.uzm.hylex.core.bungee.configuration.ConfigurationCreator.find("setup.yml").get();

    WebSocket socket =
      WebSocket.create("core-bungee", config.getString("server-configuration.services-address"));
    socket.addQueryParam("?server=core-bungee");
    socket.addQueryParam("&arenaClient=false");

    socket.build();
    socket.sendHeaders("Authorization", "00f1ff268656703e14faf2d05");
    socket.connect().setup();

    buildEvents(loader);
  }

  private void buildEvents(Plugin loader) {
    WebSocket socket = WebSocket.get("core-bungee");

    socket.getSocket().on("data-callback", args -> {
      if (!(args[0] instanceof org.json.JSONObject)) {
        return;
      }
      try {
        JSONObject response = (JSONObject) new JSONParser().parse(args[0].toString());

        HylexPlayer hp = HylexPlayer.getByUUID(UUID.fromString(response.get("uuid").toString()));
        if (hp != null) {
          JSONArray schemas = (JSONArray) response.get("schemas");
          for (Object object : schemas) {
            if (object instanceof JSONObject) {
              JSONObject json = (JSONObject) object;
              String schema = json.get("schemaName").toString();
              hp.computeData(schema, (JSONObject) json.get("data"));
            }
          }
          if (hp.getSchemas().size() >= 1) {
            hp.loadAccount();
          }else {
            hp.getPlayer()
              .disconnect(TextComponent.fromLegacyText("§c§lREDE STONE §c- Carregamento de dados\n\n§cPelo visto não conseguimos carregar sua conta\n§ccorretamente, isso se deve a diversos fatores tanto internos quanto externos.\n\n§cNos desculpe o incômodo, porém tentar se reconectar à nossa rede\n§cpode resolver o problema.\n\n§cSe esse problema for muito recorrente contate um superior.\n§c✰ Atenciosamente,Equipe de desenvolvimento Stone."));
          }

        }
      } catch (Exception ex) {
        System.err.println("[Socket.io]  Não foi possível processar os dados recibos.");
      }
    });
    socket.getSocket().on("send-restart-require", args -> {
      if (!(args[0] instanceof org.json.JSONObject)) {
        return;
      }

      try {
        JSONObject response = (JSONObject) new JSONParser().parse(args[0].toString());

      String clientName = (String) response.get("name");
        QueueController.addToQueue((Bungee.getInstance().getProxy().getServerInfo(clientName.replace("core-bedwars-", ""))));
        Bungee.getInstance().getLogger().info("§b[Hylex Module: Core] §7Requiring a permission to " + ( clientName.replace("core-bedwars-", "")) + " to restarting");


      } catch (Exception ex) {
        System.err.println("[Socket.io ]  Não foi possível processar os dados recibos.");
      }
    });


    socket.getSocket().on("save-mini", args -> {
      if (!(args[0] instanceof org.json.JSONObject)) {
        return;
      }

      try {
        JSONObject response = (JSONObject) new JSONParser().parse(args[0].toString());

        String miniName = (String) response.get("miniName");
        String nickname = (String) response.get("nickname");

        if (Bungee.getInstance().getProxy().getPlayer(nickname) ==null) {
          return;
        }
        ProxiedPlayer pp = Bungee.getInstance().getProxy().getPlayer(nickname);

        if (HylexPlayer.getByPlayer(pp) != null) {
          HylexPlayer.getByPlayer(pp).setCurrentMini(miniName);
        }
      } catch (Exception ex) {
        System.err.println("[Socket.io ]  Não foi possível processar os dados recibos.");
      }
    });

    socket.getSocket().on("send-finished-restart", args -> {
      if (!(args[0] instanceof org.json.JSONObject)) {
        return;
      }
      try {
        JSONObject response = (JSONObject) new JSONParser().parse(args[0].toString());
        String clientName = ((String) response.get("name")).replace("core-bedwars-", "");
        QueueController.isRestarting().remove(Bungee.getInstance().getProxy().getServerInfo(clientName).getName());
      } catch (Exception ex) {
        System.err.println("[Socket.io]  Não foi possível processar os dados recibos.");
      }
    });
  }
}
