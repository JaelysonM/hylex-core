package com.uzm.hylex.core.listeners;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

import java.util.*;

public class PlayerChatListener implements Listener {
  private static Map<UUID, Long> COOLDOWN;
  private static String[] TO_DISABLE;

  static {
    COOLDOWN = Maps.newHashMap();
    TO_DISABLE = new String[]{"me", "whisper", "msg", "w", "pl", "plugins", "ver", "say"};
  }

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    Player player = evt.getPlayer();

    long defaultTime = System.currentTimeMillis() / 1000L;
    long currentTime = COOLDOWN.getOrDefault(player.getUniqueId(), 0L);
    if (COOLDOWN.containsKey(player.getUniqueId())) {
      if (defaultTime - currentTime < 3L && (!player.hasPermission("hylex.fastchat"))) {
        player.sendMessage("§cVocê está digitando comandos de uma maneira §f§nmuito rápida§c, aguarde um pouco.");
        evt.setCancelled(true);
        return;
      }
      COOLDOWN.remove(player.getUniqueId());
    }
    COOLDOWN.computeIfAbsent(player.getUniqueId(), result -> defaultTime);
    if (!evt.isCancelled()) {
      String cmd = evt.getMessage().split(" ")[0];

      HelpTopic help = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
      if (help == null) {
        evt.setCancelled(true);
        evt.getPlayer().sendMessage("§cO comando §n'" + evt.getMessage().split(" ")[0].replace("/", "") + "'§c não está acessível ou não existe.");
      }
    }

    String command = evt.getMessage().split("/")[1].replace("minecraft:", "").split(" ")[0];
    if (command.startsWith("bukkit:") && !evt.getPlayer().hasPermission("*")) {
      evt.setCancelled(true);
      player.sendMessage("§cO acesso a esse comando foi §c§nnegado§c.");
    }

    List<String> lists = Arrays.asList(TO_DISABLE);
    if (lists.contains(command)) {
      evt.setCancelled(true);
      player.sendMessage("§cO acesso a esse comando foi §c§nnegado§c.");
    }
  }
}
