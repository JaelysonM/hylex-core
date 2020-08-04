package com.uzm.hylex.core.loaders;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.java.util.configuration.ConfigurationCreator;
import com.uzm.hylex.services.lan.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.UUID;

public class ServicesLoader {

  public ServicesLoader(PluginLoader loader) {
    WebSocket socket = WebSocket.create("core-" + Core.SOCKET_NAME, ConfigurationCreator.find("setup", loader.getCore()).get().getString("server-configuration.services-address"));
    socket.addQueryParam("?server=core-" + Core.SOCKET_NAME);
    socket.addQueryParam("&arenaClient=" + Core.IS_ARENA_CLIENT);

    socket.build();
    socket.sendHeaders("Authorization", "00f1ff268656703e14faf2d05");
    socket.connect().setup();

    buildEvents(loader);
  }

  private void buildEvents(PluginLoader loader) {
    WebSocket socket = WebSocket.get("core-" + Core.SOCKET_NAME);

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
          if (hp.getSchemas().size() > 0) {
            hp.loadAccount();
          }else {
            hp.getPlayer().kickPlayer("§c§lREDE STONE §c- Carregamento de dados\n\n§cPelo visto não conseguimos carregar sua conta\n§ccorretamente, isso se deve a diversos fatores tanto internos quanto externos.\n\n§cNos desculpe o incômodo, porém tentar se reconectar à nossa rede\n§cpode resolver o problema.\n\n§cSe esse problema for muito recorrente contate um superior.\n§c✰ Atenciosamente,Equipe de desenvolvimento Stone.");
          }
        }
      } catch (Exception ex) {
        System.err.println("[Socket.io - DataController]  Não foi possível processar os dados recibos.");
      }
    });
  }
}
