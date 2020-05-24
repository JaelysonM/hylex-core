package com.uzm.hylex.core.menu;

import com.google.common.collect.Maps;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.api.container.BedWarsStatisticsContainer;
import com.uzm.hylex.core.api.container.LobbiesContainer;
import com.uzm.hylex.core.java.util.StringUtils;
import com.uzm.hylex.core.spigot.inventories.PlayerMenu;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import com.uzm.hylex.core.spigot.utils.BukkitUtils;
import com.uzm.hylex.core.utils.ProxyUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SettingsMenu extends PlayerMenu {
  private static Map<UUID, Long> COOLDOWN = Maps.newHashMap();
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      if (evt.getWhoClicked().equals(this.player)) {
        evt.setCancelled(true);


        Player player = (Player) evt.getWhoClicked();
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {

          if (getAttached(evt.getSlot()) != null) {
            String caseS = (String) getAttached(evt.getSlot());
            HylexPlayer hp = HylexPlayer.getByPlayer((Player) evt.getWhoClicked());
            if (hp == null) {
                evt.getWhoClicked().closeInventory();
                return;

            }
            if (!hp.isAccountLoaded()) {
              evt.getWhoClicked().closeInventory();
              return;
            }
            long currentTime = COOLDOWN.getOrDefault(player.getUniqueId(), 0L);
            if (COOLDOWN.containsKey(player.getUniqueId())) {
              if (currentTime > System.currentTimeMillis()) {
                 evt.setCancelled(true);
                return;
              }
              COOLDOWN.remove(player.getUniqueId());
            }
            COOLDOWN.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));

            switch (caseS) {
              case "tell":
                hp.getLobbiesContainer().toggleTell();
                evt.getInventory().setItem(evt.getSlot(), hp.getLobbiesContainer().canSendTell() ?
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build() :
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
                ProxyUtils.sendUpdateSettings(hp);
                break;
              case "report":
                hp.getLobbiesContainer().toggleReport();
                evt.getInventory().setItem(evt.getSlot(), hp.getLobbiesContainer().canSendReport() ?
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build() :
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
                ProxyUtils.sendUpdateSettings(hp);
                break;
              case "players":
                hp.getLobbiesContainer().togglePlayersVisible();
                evt.getInventory().setItem(evt.getSlot(), hp.getLobbiesContainer().isPlayersVisible() ?
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build() :
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
                ProxyUtils.sendUpdateSettings(hp);
                break;
              case "party":
                hp.getLobbiesContainer().toggleParty();
                evt.getInventory().setItem(evt.getSlot(), hp.getLobbiesContainer().canSendParty() ?
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build() :
                  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
                ProxyUtils.sendUpdateSettings(hp);
                break;
            }
          }else {
            if (evt.getSlot() == 0) {
              HylexPlayer hp = HylexPlayer.getByPlayer((Player) evt.getWhoClicked());
              if (hp == null) {
                evt.getWhoClicked().closeInventory();
                return;

              }
              if (!hp.isAccountLoaded()) {
                evt.getWhoClicked().closeInventory();
                return;

              }
              new ProfileMenu(hp);
            }
          }
        }
      }
    }
  }

  public SettingsMenu(HylexPlayer hp) {
    super(hp.getPlayer(), "Preferências", 4);


    this.setItem(0, new ItemBuilder(Material.ARROW).name("§eVoltar").build());
    if (!hp.isAccountLoaded()) {
      return;
    }
    LobbiesContainer pref = hp.getLobbiesContainer();

    if (hp.getPlayer().hasPermission("hylex.staff")) {

      this.setItem(10, new ItemBuilder(Material.SIGN).name("§bMensagens privadas").lore("§7", "§7Aqui você ativará/desativará").lore("§7o /tell.").build());
      this.setItem(12, new ItemBuilder(Material.REDSTONE).name("§cDenúncias §e§lSTAFF").lore("§7", "§7Aqui você ativará/desativará").lore("§7as mensagens do /report.").build());
      this.setItem(14,
        new ItemBuilder(Material.SKULL_ITEM).name("§aVisibilidade dos jogadores").lore("§7", "§7Aqui você ativará/desativará").lore("§7a visibilidade dos jogadores.").build());
      this.setItem(16, new ItemBuilder(Material.EXP_BOTTLE).name("§ePedidos de party").lore("§7", "§7Aqui você ativará/desativará").lore("§7os pedidos de party.").build());

      if (pref.canSendTell())
        this.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(19, "tell");

      if (pref.canSendReport())
        this.setItem(21, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(21, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(21, "report");

      if (pref.isPlayersVisible())
        this.setItem(23, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(23, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(23, "players");

      if (pref.canSendParty())
        this.setItem(25, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(25, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(25, "party");


    } else {
      this.setItem(11, new ItemBuilder(Material.SIGN).name("§bMensagens privadas").lore("§7", "§7Aqui você ativará/desativará").lore("§7o /tell.").build());
      this.setItem(13,
        new ItemBuilder(Material.SKULL_ITEM).name("§aVisibilidade dos jogadores").lore("§7", "§7Aqui você ativará/desativará").lore("§7a visibilidade dos jogadores.").build());
      this.setItem(15, new ItemBuilder(Material.EXP_BOTTLE).name("§ePedidos de party").lore("§7", "§7Aqui você ativará/desativará").lore("§7os pedidos de party.").build());

      if (pref.canSendTell())
        this.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(20, "tell");

      if (pref.isPlayersVisible())
        this.setItem(22, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(22, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(22, "players");

      if (pref.canSendParty())
        this.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§aAtivado").lore("§7", "§7Clique para desativar").build());
      else
        this.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name("§cDesativado").lore("§7", "§7Clique para ativar").build());
      attachObject(24, "party");

    }

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
