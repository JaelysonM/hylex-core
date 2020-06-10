package com.uzm.hylex.core.bungeeguard;

import com.uzm.hylex.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginController {

  /**
   * @author NickUC
   * @forked JaelysonMarth
   */

  private Core plugin;

  public LoginController(Core plugin) {
    this.plugin = plugin;
  }

  public enum InvalidLoginType {
    UNKNOWN_GAMEPROFILE,
    UNKNOWN_TOKEN,
    INVALID_TOKEN;
  }

  public boolean denyLogin(PlayerLoginEvent loginEvent, InvalidLoginType invalidLoginType, String token) {
    Player player = loginEvent.getPlayer();
    String address = loginEvent.getAddress().getHostAddress();

    switch (invalidLoginType) {
      case UNKNOWN_GAMEPROFILE:
        sendMessage("§c[BungeeGuard] Recusando conexão de " + player.getName() + " (" + player.getUniqueId() + ") @ " + address + " - Impossível pegar o GameProfile.");
        loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER,
          "§c[HylexMC - Proteção]\n\n§cNão foi possível se autenticar, pois nenhum dado foi encaminhado pelo proxy.\n§cContate algum superior se esse erro persistir.\n\n§7Nosso discord: §bhylex.net/discord");
        return true;
      case UNKNOWN_TOKEN:
        sendMessage("§c[BungeeGuard] Recusando conexão de " + player.getName() + " (" + player
          .getUniqueId() + ") @ " + address + " - Um token não foi incluído nas propriedades do GameProfile.");
        loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER,
          "§c[HylexMC - Proteção]\n\n§cNão foi possível se autenticar, pois nenhum dado foi encaminhado pelo proxy.\n§cContate algum superior se esse erro persistir.\n\n§7Nosso discord: §bhylex.net/discord");
        Bukkit.getOnlinePlayers().stream().filter(on -> on.hasPermission("bungeeguard.warning")).forEach(on -> {
          on.sendMessage("");
          on.sendMessage(" §cUma tentativa de invasão foi bloqueada pelo BungeeGuard");
          on.sendMessage("  §7Jogador: §f" + player.getName());
          on.sendMessage("  §7UUID: §f" + player.getUniqueId());
          on.sendMessage("  §7Endereço de ip: §f" + address);
          on.sendMessage("");
        });
        return true;
      case INVALID_TOKEN:
        if (getPlugin().getAllowedTokens().isEmpty()) {
          sendMessage(
            "§6[HylexMC - BungeeGuard] §7Nenhum token foi configurado. Permitindo e adicionando o primeiro token da conexão " + player.getUniqueId() + " @ " + address + "!");
          sendMessage("§6[HylexMC - BungeeGuard] §7Para mais informações siga o tutorial: §fhttps://github.com/NickUltracraft/nplugins-doc/wiki/nLogin-Bungee");
          getPlugin().getAllowedTokens().add(token);
          return false;
        }
        if (!getPlugin().getAllowedTokens().contains(token)) {
          sendMessage(
            "§c[BungeeGuard] Recusando conexão de " + player.getName() + " (" + player.getUniqueId() + ") @ " + address + " - Um token inválido foi utilizado §f" + token + "§c.");
          loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER,
            "§c[HylexMC - Proteção]\n\n§cDesculpe, mas um código de autenticação inválido foi fornecido.\n§cCertifique-se que você está conectado na proxy.\n\n§7Nosso discord: §bhylex.net/discord");

          Bukkit.getOnlinePlayers().stream().filter(on -> on.hasPermission("bungeeguard.warning")).forEach(on -> {
            on.sendMessage("");
            on.sendMessage(" §cUma tentativa de invasão foi bloqueada pelo BungeeGuard");
            on.sendMessage("  §7Jogador: §f" + player.getName());
            on.sendMessage("  §7UUID: §f" + player.getUniqueId());
            on.sendMessage("  §7Endereço de ip: §f" + address);
            on.sendMessage("");
          });
          return true;
        }

        break;
    }
    return false;
  }



  public void sendMessage(String message) {
    Bukkit.getConsoleSender().sendMessage(message);
  }


  public Core getPlugin() {
    return plugin;
  }
}
