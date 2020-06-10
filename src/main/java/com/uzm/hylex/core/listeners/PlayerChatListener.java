package com.uzm.hylex.core.listeners;

import com.google.common.collect.Maps;
import com.uzm.hylex.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerChatListener implements Listener {

  private static Map<UUID, Long> COOLDOWN;
  private static Map<UUID, Long> COOLDOWN_CHAT;
  private static String[] TO_DISABLE;

  static {
    COOLDOWN = Maps.newHashMap();
    COOLDOWN_CHAT = Maps.newHashMap();
    TO_DISABLE = new String[] {"me", "whisper", "msg", "w", "pl", "plugins", "ver", "say"};
  }



  @EventHandler(priority =  EventPriority.HIGHEST)
  void onTabComplete(  PlayerChatTabCompleteEvent evt) {
    if (evt.getChatMessage().startsWith("/") && !evt.getPlayer().hasPermission("*")) {
      evt.getTabCompletions().clear();
    }
  }


  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerSpam(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    if (!player.hasPermission("hylex.fastchat")) {
      long currentTime = COOLDOWN_CHAT.getOrDefault(player.getUniqueId(), 0L);
      if (COOLDOWN_CHAT.containsKey(player.getUniqueId())) {
        if (currentTime >= System.currentTimeMillis()) {
          long time =  ((currentTime - System.currentTimeMillis()) / 1000);
          player.sendMessage("§cAguarde " + time  + (time <= 1?" segundo": " segundos")+ " para escrever novamente.");
          evt.setCancelled(true);
          return;
        }
        COOLDOWN_CHAT.remove(player.getUniqueId());
      }
      COOLDOWN_CHAT.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
    }

  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    Player player = evt.getPlayer();
    if ((!player.hasPermission("hylex.fastchat"))) {
      long currentTime = COOLDOWN.getOrDefault(player.getUniqueId(), 0L);
      if (COOLDOWN.containsKey(player.getUniqueId())) {
        if (currentTime > System.currentTimeMillis()) {
          player.sendMessage("§cVocê está digitando comandos de uma maneira §f§nmuito rápida§c, aguarde um pouco.");
          evt.setCancelled(true);
          return;
        }
        COOLDOWN.remove(player.getUniqueId());
      }
      COOLDOWN.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
    }

    if (!evt.isCancelled()) {
      String cmd = evt.getMessage().split(" ")[0];

      HelpTopic help = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
      if (help == null) {
        evt.setCancelled(true);
        evt.getPlayer().sendMessage("§cO comando §n'" + evt.getMessage().split(" ")[0].replace("/", "") + "'§c não está acessível ou não existe.");
       return;
      }
    }

    String command = evt.getMessage().split("/")[1].replace("bukkit:", "").replace("minecraft:", "").split(" ")[0];
    if (command.startsWith("bukkit:") && !evt.getPlayer().hasPermission("*")) {
      evt.setCancelled(true);
      player.sendMessage("§cO acesso a esse comando foi negado§c.");
    }

    List<String> lists = Arrays.asList(TO_DISABLE);
    if (lists.contains(command)) {
      evt.setCancelled(true);
      player.sendMessage("§cO acesso a esse comando foi §cnegado§c.");
    }
  }
}
