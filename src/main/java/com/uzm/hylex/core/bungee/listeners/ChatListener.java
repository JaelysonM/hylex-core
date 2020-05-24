package com.uzm.hylex.core.bungee.listeners;

import com.uzm.hylex.core.bungee.api.HylexPlayer;
import com.uzm.hylex.core.nms.reflections.Accessors;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatListener implements Listener {

  private final boolean blockTell,blockLobby;

  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();

  public ChatListener() {
    Map<?, ?> map = Accessors.getField(PluginManager.class, "commandMap", Map.class).get(ProxyServer.getInstance().getPluginManager());
    blockTell = map.containsKey("tell");
    blockLobby = map.containsKey("lobby");
  }

  @EventHandler(priority = (byte) 128)
  public void onChat(ChatEvent evt) {
    if (evt.getSender() instanceof ProxiedPlayer) {
      if (evt.isCommand()) {
        ProxiedPlayer player = (UserConnection) evt.getSender();
        String[] args = evt.getMessage().replace("/", "").split(" ");
        String command = args[0];
        if (blockLobby && command.equals("lobby")) {
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }

          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
          player.sendMessage(TextComponent.fromLegacyText("§aVocê tem certeza? Utilize /lobby novamente para voltar ao lobby."));
        }
        if ((command.equalsIgnoreCase("lp")
          ||command.equalsIgnoreCase("luckperms")
          ||command.equalsIgnoreCase("perms")
          ||command.equalsIgnoreCase("permissions")
          ||command.equalsIgnoreCase("perm")
          ||command.equalsIgnoreCase("luckypermsbungee")
          ||command.equalsIgnoreCase("bperms")
          ||command.equalsIgnoreCase("bpermissions")
          ||command.equalsIgnoreCase("bperm")
          ||command.equalsIgnoreCase("lpb")
          ||command.equalsIgnoreCase("litebans")
          ||command.equalsIgnoreCase("antibot")
          ||command.equalsIgnoreCase("ab")
          ||command.equalsIgnoreCase("skin")
          ||command.equalsIgnoreCase("sr")
          ||command.equalsIgnoreCase("help")
        ) && !player.hasPermission("hylex.superior")) {
          evt.setCancelled(true);
          player.sendMessage(TextComponent.fromLegacyText("§cO comando §n'" + command + "'§c não está acessível ou não existe."));
        }
       else if (args.length > 2) {
          ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);

          if (target != null) {
            HylexPlayer hp = HylexPlayer.getByPlayer(target);
            if (!hp.isAccountLoaded()) {
              return;
            }

            if (blockTell && command.equals("tell") && !args[1].equalsIgnoreCase(player.getName())) {
              if (!hp.getLobbiesContainer().canSendTell()) {
                evt.setCancelled(true);
                player.sendMessage(TextComponent.fromLegacyText("§cEste usuário desativou as mensagens privadas."));
              }else {
                hp.setLastMessager(player, 30);
              }
            }

          }
        }
      }
    }
  }
}
