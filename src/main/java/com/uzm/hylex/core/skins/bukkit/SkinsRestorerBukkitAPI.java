package com.uzm.hylex.core.skins.bukkit;

import com.google.common.annotations.Beta;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.skins.shared.interfaces.ISkinsRestorerAPI;
import com.uzm.hylex.core.skins.shared.storage.SkinStorage;
import com.uzm.hylex.core.skins.shared.utils.MojangAPI;
import com.uzm.hylex.core.skins.shared.utils.SkinsRestorerAPI;
import org.bukkit.entity.Player;


/**
 * Created by McLive on 27.08.2019.
 */
public class SkinsRestorerBukkitAPI extends SkinsRestorerAPI implements ISkinsRestorerAPI<Player> {
  private Core plugin;

  public SkinsRestorerBukkitAPI(Core plugin, MojangAPI mojangAPI, SkinStorage skinStorage) {
    super(mojangAPI, skinStorage);
    this.plugin = plugin;
  }

  // Todo: We need to refactor applySkin through all platforms to behave the same!
  @Beta
  @Override
  public void applySkin(Player player, Object props) {
    plugin.getFactory().applySkin(player, props);
  }

  @Beta
  @Override
  public void applySkin(Player player) {
    plugin.getFactory().applySkin(player, this.getSkinData(this.getSkinName(player.getName())));

  }
}