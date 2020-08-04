package com.uzm.hylex.core.menu;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.api.container.BedWarsStatisticsContainer;
import com.uzm.hylex.core.api.container.LobbiesContainer;
import com.uzm.hylex.core.java.util.StringUtils;
import com.uzm.hylex.core.spigot.inventories.PlayerMenu;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import com.uzm.hylex.core.spigot.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StatisticsMenu extends PlayerMenu {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      if (evt.getWhoClicked().equals(this.player)) {
        evt.setCancelled(true);

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          if (evt.getSlot() == 10) {
            if (HylexPlayer.getByPlayer(this.player) != null) {
              new BedWarsStatisticsMenu(HylexPlayer.getByPlayer(this.player));
            }else {
              evt.getWhoClicked().closeInventory();
            }

          }
        }
      }
    }
  }

  public StatisticsMenu(HylexPlayer hp) {
    super(hp.getPlayer(), "Estatísticas dos minigames", 3);

    this.setItem(10,
      new ItemBuilder(Material.BED).name("§cEstatísticas do Bedwars").lore("", "§7Modos disponíveis: §bGlobal, Solo, Dupla, Trio, Quartetos", "", "§eClique para ver!")
        .build());
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
