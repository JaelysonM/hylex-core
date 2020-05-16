package com.uzm.hylex.core.bungee.loaders;

import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.services.lan.WebSocket;
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

          hp.loadAccount();
        }
      } catch (Exception ex) {
        System.err.println("[HylexSocket.io - ]  Não foi possível processar os dados recibos.");
        ex.printStackTrace();
      }
    });
  }
}
