package com.uzm.hylex.core.listeners.bungeeguard;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.bungeeguard.LoginController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Collection;

/**
 * @author NickUC
 */

public class PlayerLoginListeners implements Listener {
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onTokenCheck(PlayerLoginEvent e) {
    GameProfile gameProfile;
    if (e.getAddress() == null || e.getAddress().getHostAddress() == null) {
      e.disallow(PlayerLoginEvent.Result.KICK_OTHER,
        "§c[HylexMC - Proteção]\n\n§cNão foi possível se autenticar, pois um pacote inválido de endereço de ip foi enviado.\n§cTente reinciar seu cliente.\n\n§7Dúvidas? Nosso discord: §bhylex.net/discord");
      Bukkit.getOnlinePlayers().stream().filter(on -> on.hasPermission("bungeeguard.warning")).forEach(on -> {
        on.sendMessage("");
        on.sendMessage(" §cUma tentativa de invasão foi bloqueada pelo BungeeGuard");
        on.sendMessage(" §cO jogador tentou enviar um pacote inválido de endereço de ip.");
        on.sendMessage("");
      });
      return;
    }
    if (!Core.BUNGEE_GUARD) {
      return;
    }
    Player player = e.getPlayer();
    try {
      gameProfile = (GameProfile) player.getClass().getDeclaredMethod("getProfile", new Class[0]).invoke(player, new Object[0]);
    } catch (ReflectiveOperationException ex) {
      ex.printStackTrace();
      Core.getUtils().denyLogin(e, LoginController.InvalidLoginType.UNKNOWN_GAMEPROFILE, (String) null);
      return;
    }
    Collection<Property> tokens = gameProfile.getProperties().get("bungeeguard-token");
    if (tokens.isEmpty()) {
      Core.getUtils().denyLogin(e, LoginController.InvalidLoginType.UNKNOWN_TOKEN, (String) null);
      return;
    }
    Property tokenProperty = tokens.iterator().next();
    String token = tokenProperty.getValue();
    if (Core.getUtils().denyLogin(e, LoginController.InvalidLoginType.INVALID_TOKEN, token))
      return;
    gameProfile.getProperties().removeAll("bungeeguard-token");
  }
}
