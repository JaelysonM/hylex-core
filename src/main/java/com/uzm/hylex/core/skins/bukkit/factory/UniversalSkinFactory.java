package com.uzm.hylex.core.skins.bukkit.factory;


import com.uzm.hylex.core.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class UniversalSkinFactory extends SkinFactory {
  private final Plugin plugin;

  public UniversalSkinFactory(Plugin plugin) {this.plugin = plugin;}

  @Override
  public void updateSkin(Player player) {
    if (!player.isOnline())
      return;
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
      for (Player ps : Bukkit.getOnlinePlayers()) {
        // Some older spigot versions only support hidePlayer(player)
        ps.hidePlayer(player);
        ps.showPlayer(player);

      }

    });
    NMS.refreshPlayer(player);
  }

}