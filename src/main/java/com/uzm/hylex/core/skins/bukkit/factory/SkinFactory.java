package com.uzm.hylex.core.skins.bukkit.factory;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.skins.shared.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SkinFactory {

    /**
     * Applies the skin In other words, sets the skin data, but no changes will
     * be visible until you reconnect or force update with
     *
     * @param p     - Player
     * @param props - Property Object
     */
    public void applySkin(final Player p, Object props) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
            try {
                if (props == null)
                    return;

                Object ep = ReflectionUtil.invokeMethod(p.getClass(), p, "getHandle");
                Object profile = ReflectionUtil.invokeMethod(ep.getClass(), ep, "getProfile");
                Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
                ReflectionUtil.invokeMethod(propmap.getClass(), propmap, "put", new Class[]{Object.class, Object.class}, "textures", props);

                Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
                    updateSkin(p);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Instantly updates player's skin
     *
     * @param p - Player
     */
    public abstract void updateSkin(Player p);

}