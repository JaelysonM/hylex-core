package com.uzm.hylex.core.menu;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.api.container.BedWarsStatisticsContainer;
import com.uzm.hylex.core.spigot.inventories.PlayerMenu;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import com.uzm.hylex.core.utils.BukkitUtils;
import com.uzm.hylex.core.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileMenu extends PlayerMenu {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      if (evt.getWhoClicked().equals(this.player)) {
        evt.setCancelled(true);

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          // TODO: click on items
        }
      }
    }
  }

  public ProfileMenu(HylexPlayer hp) {
    super(hp.getPlayer(), "Meu Perfil", 3);

    this.setItem(10, BukkitUtils
      .putProfileOnSkull(this.player, new ItemBuilder("SKULL_ITEM:3 : 1 : display=" + Group.getColored(this.player) + " : lore=&fGrupo: " + hp.getGroup().getName()).build()));

    BedWarsStatisticsContainer statistics = hp.getBedWarsStatistics();
    long wins = statistics.getLong("wins", "global");
    this.setItem(12, new ItemBuilder(
      "BED : 1 : display=&aBed Wars : lore=&fPartidas: &7" + StringUtils.formatNumber(statistics.getLong("games", "global")) + "\n \n&fCamas Destruídas: &7" + StringUtils
        .formatNumber(statistics.getLong("bedsBroken", "global")) + "\n&fCamas Perdidas: &7" + StringUtils
        .formatNumber(statistics.getLong("bedsLost", "global")) + "\n \n&fAbates: &7" + StringUtils
        .formatNumber(statistics.getLong("kills", "global") + statistics.getLong("finalKills", "global")) + "\n&fMortes: &7" + StringUtils
        .formatNumber(statistics.getLong("deaths", "global")) + "\n&fAbates Finais: &7" + StringUtils
        .formatNumber(statistics.getLong("finalKills", "global")) + "\n&fMortes Finais: &7" + StringUtils
        .formatNumber(statistics.getLong("finalDeaths", "global")) + "\n \n&fVitórias: &7" + StringUtils.formatNumber(wins) + "\n&fDerrotas: &7" + StringUtils
        .formatNumber(statistics.getLong("games", "global") - wins) + "\n \n&fCoins: &6" + StringUtils.formatNumber(statistics.getLong("coins", "global"))).build());

    this.register(Core.getInstance());
    this.open();
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
