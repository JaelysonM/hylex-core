package com.uzm.hylex.core.skins.bungee.listeners;

import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.skins.shared.exception.SkinRequestException;
import com.uzm.hylex.core.skins.shared.storage.Config;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.regex.Pattern;

public class LoginListener implements Listener {
  private Bungee plugin;

  public LoginListener(Bungee plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onServerChange(final LoginEvent e) {
    e.registerIntent(plugin);
    String nick = e.getConnection().getName();

    if (e.isCancelled() && Config.NO_SKIN_IF_LOGIN_CANCELED) {
      e.completeIntent(plugin);
      return;
    }

    if (Config.DISABLE_ONJOIN_SKINS_BUNGEE) {
      e.completeIntent(plugin);
      return;
    }

    if (!validUsername(nick)) {
      e.completeIntent(plugin);
      return;
    }
    // Don't change skin if player has no custom skin-name set and his username is invalid
    if (plugin.getSkinStorage().getPlayerSkin(nick) == null  && !validUsername(nick)) {
      System.out.println("[hylex-core Fork SkinsRestorer] Not applying skin to " + nick + " (invalid username).");
      e.completeIntent(plugin);
      return;
    }

    Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
      String skin = plugin.getSkinStorage().getDefaultSkinNameIfEnabled(nick);
      try {
        plugin.getSkinApplier().applySkin(null, skin, (InitialHandler) e.getConnection());
      } catch (SkinRequestException ignored) {
      } catch (Exception e1) {
        e1.printStackTrace();
      }

      e.completeIntent(plugin);
    });
  }

  private static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_\\-]+$");
  private static  Pattern PATTERN = Pattern.compile(
    "^(Craft|Beach|Actor|Games|Tower|Elder|Mine|Nitro|Worms|Build|Plays|Hyper|Crazy|Super|_Itz|Slime)(Craft|Beach|Actor|Games|Tower|Elder|Mine|Nitro|Worms|Build|Plays|Hyper|Crazy|Super|_Itz|Slime)(11|50|69|99|88|HD|LP|XD|YT)");


  public static String c(String msg) {
    return msg.replaceAll("&", "§");
  }

  public static boolean validUsername(String username) {
    if (username.length() > 16)
      return false;

    if (PATTERN.matcher(username).find()) {
      return false;
    }else {
      return namePattern.matcher(username).matches();
    }
  }
}