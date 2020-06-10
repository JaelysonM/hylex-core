package com.uzm.hylex.core.skins.bungee;

import com.google.common.annotations.Beta;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.skins.shared.interfaces.ISkinsRestorerAPI;
import com.uzm.hylex.core.skins.shared.storage.SkinStorage;
import com.uzm.hylex.core.skins.shared.utils.MojangAPI;
import com.uzm.hylex.core.skins.shared.utils.SkinsRestorerAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;


/**
 * Created by McLive on 10.11.2019.
 */
public class SkinsRestorerBungeeAPI extends SkinsRestorerAPI implements ISkinsRestorerAPI<ProxiedPlayer> {
  private Bungee plugin;

  public SkinsRestorerBungeeAPI(Bungee plugin, MojangAPI mojangAPI, SkinStorage skinStorage) {
    super(mojangAPI, skinStorage);
    this.plugin = plugin;
  }

  // Todo: We need to refactor applySkin through all platforms to behave the same!
  @Beta
  @Override
  public void applySkin(ProxiedPlayer player, Object props) {
    this.applySkin(player);
  }

  @Beta
  @Override
  public void applySkin(ProxiedPlayer player) {
    try {
      plugin.getSkinApplier().applySkin(player);
    } catch (Exception e) {
    }
  }
}