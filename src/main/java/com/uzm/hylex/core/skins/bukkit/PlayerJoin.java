package com.uzm.hylex.core.skins.bukkit;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.skins.shared.exception.SkinRequestException;
import com.uzm.hylex.core.skins.shared.storage.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.regex.Pattern;

/**
 * Created by McLive on 21.01.2019.
 */
public class PlayerJoin implements Listener {

    private Core plugin;

    public PlayerJoin(Core plugin) {
        this.plugin=plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
            try {
                if (Config.DISABLE_ONJOIN_SKINS) {
                    // factory.applySkin(e.getPlayer(), SkinStorage.getSkinData(SkinStorage.getPlayerSkin(e.getPlayer().getName())));
                    // shouldn't it just skip it if it's true?
                    return;
                }

                // Don't change skin if player has no custom skin-name set and his username is invalid
                if (plugin.getSkinStorage().getPlayerSkin(e.getPlayer().getName()) == null && !validUsername(e.getPlayer().getName())) {
                    System.out.println("[hylex-core Fork SkinsRestorer] Not applying skin to " + e.getPlayer().getName() + " (invalid username).");
                    return;
                }

                String skin = plugin.getSkinStorage().getDefaultSkinNameIfEnabled(e.getPlayer().getName());

                plugin.getFactory().applySkin(e.getPlayer(), plugin.getSkinStorage().getOrCreateSkinForPlayer(skin));
            } catch (SkinRequestException ignored) {
            }
        });
    }

    private static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_\\-]+$");

    public static String c(String msg) {
        return msg.replaceAll("&", "§");
    }

    public static boolean validUsername(String username) {
        if (username.length() > 16)
            return false;

        return namePattern.matcher(username).matches();
    }
}