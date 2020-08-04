package com.uzm.hylex.core.menu;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.api.container.BedWarsStatisticsContainer;
import com.uzm.hylex.core.api.container.LobbiesContainer;
import com.uzm.hylex.core.controllers.HylexPlayerController;
import com.uzm.hylex.core.spigot.inventories.PlayerMenu;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import com.uzm.hylex.core.spigot.utils.BukkitUtils;
import com.uzm.hylex.core.java.util.StringUtils;
import org.bukkit.Material;
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
          if (evt.getSlot() == 13) {
            player.sendMessage("§eLoja da RedeStone em: §b§nloja.redestone.com");
            player.closeInventory();
          }
          if (evt.getSlot() == 21) {
            if (HylexPlayer.getByPlayer(this.player) != null)
              new SettingsMenu(HylexPlayer.getByPlayer(this.player));
            else
              evt.getWhoClicked().closeInventory();
          }
          if (evt.getSlot() == 22 && evt.getCurrentItem().getType() == Material.BREWING_STAND_ITEM) {
            if (HylexPlayer.getByPlayer(this.player) != null)
              new BedWarsStatisticsMenu(HylexPlayer.getByPlayer(this.player));
            else
              evt.getWhoClicked().closeInventory();
          }
          if (evt.getSlot() == 23) {
            if (HylexPlayer.getByPlayer(this.player) != null)
             new StatisticsMenu(HylexPlayer.getByPlayer(this.player));
            else
              evt.getWhoClicked().closeInventory();
          }
        }
      }
    }
  }

  public ProfileMenu(HylexPlayer hp) {
    super(hp.getPlayer(), "Meu Perfil", 4);
    LobbiesContainer global = hp.getLobbiesContainer();


    this.setItem(12, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name("§7").build());
    this.setItem(13, BukkitUtils.putProfileOnSkull(this.player, new ItemBuilder(Material.SKULL_ITEM).durability(3).name("§bInformações do jogador")
      .lore("", "§7Nome: " + hp.getName(), "§7Grupo: " + hp.getGroup().getName(), "", "§7Cash: §6" + 0 /*TODO Add cash to system*/, "",
        "§7Primeiro login: §a" + StringUtils.formatDateBR(global.getFirstLogin()), "§7Último login: §b" + StringUtils.formatDateBR(global.getLastLogin()), "",
        "§e* Clique para ver a loja da Stone!").build()));
    this.setItem(14, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name("§7").build());

    this.setItem(21, new ItemBuilder(Material.PAINTING).name("§aPreferências").lore("§7","§7Aqui você irá alterar","§7sua preferências","", "§e* Clique aqui para abrir.").build());


    if (hp.getSchemas().containsKey("BedWarsData")) {
      long exp = HylexPlayerController.getExp(hp);
      this.setItem(22, new ItemBuilder(Material.BREWING_STAND_ITEM).name("§bNível atual do minigame")
        .lore("§7Minigame: §cBedWars", "", "§7Nível: §f" + HylexPlayerController.getLevel(hp) + "❂",
          "§7Experiência total: §5" + StringUtils.formatNumber(HylexPlayerController.getTotalExp(hp)), "", "  §6► §7Progresso: §a" + exp + "§7/§b5000",
         StringUtils.formatColors(StringUtils.progressDataBar(exp, 5000, 30)), "", "§e* Clique para ver as estatísticas completas").build());
    }else {
      this.setItem(22, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name("§7").build());

    }
    this.setItem(23, new ItemBuilder(Material.ARMOR_STAND).name("§aEstatísticas dos minigames").lore("","§7Disponíveis: "," §c- Bedwars","", "§e* Clique para abrir.").build());

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
