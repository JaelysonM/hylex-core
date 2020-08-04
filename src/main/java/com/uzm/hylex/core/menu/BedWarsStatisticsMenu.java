package com.uzm.hylex.core.menu;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.Group;
import com.uzm.hylex.core.api.HylexPlayer;
import com.uzm.hylex.core.api.container.BedWarsStatisticsContainer;
import com.uzm.hylex.core.api.container.LobbiesContainer;
import com.uzm.hylex.core.controllers.HylexPlayerController;
import com.uzm.hylex.core.java.util.StringUtils;
import com.uzm.hylex.core.spigot.inventories.PlayerMenu;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import com.uzm.hylex.core.spigot.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;

public class BedWarsStatisticsMenu extends PlayerMenu {


  private static DecimalFormat FORMAT;

  static {
    FORMAT = new DecimalFormat("##.#");
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      if (evt.getWhoClicked().equals(this.player)) {
        evt.setCancelled(true);
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ((Player) evt.getWhoClicked()).updateInventory();
        }
      }
    }
  }

  public BedWarsStatisticsMenu(HylexPlayer hp) {
    super(hp.getPlayer(), "Estatísticas do Bedwars", 5);

    long exp = HylexPlayerController.getExp(hp);
    this.setItem(3, new ItemBuilder(Material.BREWING_STAND_ITEM).name("§bNível do Bedwars")
      .lore("§7Nível: §f" + HylexPlayerController.getLevel(hp) + "❂", "§7Experiência total: §5" + StringUtils.formatNumber(HylexPlayerController.getTotalExp(hp)), "",
        "  §6► §7Progresso: §a" + exp + "§7/§b5000", StringUtils.formatColors(StringUtils.progressDataBar(exp, 5000, 30)), "").build());

    BedWarsStatisticsContainer statistics = hp.getBedWarsStatistics();
    long wins = statistics.getLong("wins", "global");
    long games = statistics.getLong("games", "global");
    long deaths = statistics.getLong("deaths", "global");
    long kills = statistics.getLong("kills", "global");
    long finalDeaths = statistics.getLong("finalDeaths", "global");
    long finalKills = statistics.getLong("finalKills", "global");
    this.setItem(5, new ItemBuilder(Material.EYE_OF_ENDER).name("§aEstatísticas gerais")
      .lore("", "§7Partidas jogadas: §f" + StringUtils.formatNumber(games), "§7Vitórias: §6" + StringUtils.formatNumber(wins),
        "§7Derrotas: §c" + StringUtils.formatNumber(games - wins), "§7Frequência de vitória: §b" + StringUtils.formatNumber(((double) wins / (double) games) * 100) + "%", "",
        "§7Camas quebradas: §c" + StringUtils.formatNumber(statistics.getLong("bedsBroken", "global")),
        "§7Camas perdidas: §c" + StringUtils.formatNumber(statistics.getLong("bedsLost", "global")), "", "§7Abates: §e" + StringUtils.formatNumber(kills),
        "§7Mortes: §e" + StringUtils.formatNumber(deaths), "§7KDR: §b" + FORMAT.format((double) kills / (double) deaths), "",
        "§7Abates finais: §e" + StringUtils.formatNumber(finalKills), "§7Mortes finais: §e" + StringUtils.formatNumber(finalDeaths),
        "§7KDR final: §b" +FORMAT.format((double) finalKills / (double) finalDeaths), "",
        "§7Coins: §6" + StringUtils.formatNumber(statistics.getLong("coins", "global")), "").build());



    long wins_solo = statistics.getLong("wins", "solo");
    long games_solo = statistics.getLong("games", "solo");
    long deaths_solo = statistics.getLong("deaths", "solo");
    long kills_solo = statistics.getLong("kills", "solo");
    long finalDeaths_solo = statistics.getLong("finalDeaths", "solo");
    long finalKills_solo = statistics.getLong("finalKills", "solo");
    this.setItem(19, new ItemBuilder(Material.PAPER).name("§aEstatísticas do modo solo")
      .lore("", "§7Partidas jogadas: §f" + StringUtils.formatNumber(games_solo), "§7Vitórias: §6" + StringUtils.formatNumber(wins_solo),
        "§7Derrotas: §c" + StringUtils.formatNumber(games_solo - wins_solo),
        "§7Frequência de vitória: §b" + StringUtils.formatNumber(((double) wins_solo / (double) games_solo) * 100) + "%", "",
        "§7Camas quebradas: §c" + StringUtils.formatNumber(statistics.getLong("bedsBroken", "solo")),
        "§7Camas perdidas: §c" + StringUtils.formatNumber(statistics.getLong("bedsLost", "solo")), "", "§7Abates: §e" + StringUtils.formatNumber(kills_solo),
        "§7Mortes: §e" + StringUtils.formatNumber(deaths_solo), "§7KDR: §b" + FORMAT.format((double) kills_solo / (double) deaths_solo), "",
        "§7Abates finais: §e" + StringUtils.formatNumber(finalKills_solo), "§7Mortes finais: §e" + StringUtils.formatNumber(finalDeaths_solo),
        "§7KDR final: §b" + FORMAT.format((double) finalKills_solo / (double) finalDeaths_solo), "").build());


    long wins_dupla = statistics.getLong("wins", "dupla");
    long games_dupla = statistics.getLong("games", "dupla");
    long deaths_dupla = statistics.getLong("deaths", "dupla");
    long kills_dupla = statistics.getLong("kills", "dupla");
    long finalDeaths_dupla = statistics.getLong("finalDeaths", "dupla");
    long finalKills_dupla = statistics.getLong("finalKills", "dupla");
    this.setItem(21, new ItemBuilder(Material.PAPER).amount(2).name("§aEstatísticas do modo dupla")
      .lore("", "§7Partidas jogadas: §f" + StringUtils.formatNumber(games_dupla), "§7Vitórias: §6" + StringUtils.formatNumber(wins_dupla),
        "§7Derrotas: §c" + StringUtils.formatNumber(games_dupla - wins_dupla),
        "§7Frequência de vitória: §b" + StringUtils.formatNumber(((double) wins_dupla / (double) games_dupla) * 100) + "%", "",
        "§7Camas quebradas: §c" + StringUtils.formatNumber(statistics.getLong("bedsBroken", "dupla")),
        "§7Camas perdidas: §c" + StringUtils.formatNumber(statistics.getLong("bedsLost", "dupla")), "", "§7Abates: §e" + StringUtils.formatNumber(kills_dupla),
        "§7Mortes: §e" + StringUtils.formatNumber(deaths_dupla), "§7KDR: §b" + FORMAT.format((double) kills_dupla / (double) deaths_dupla), "",
        "§7Abates finais: §e" + StringUtils.formatNumber(finalKills_dupla), "§7Mortes finais: §e" + StringUtils.formatNumber(finalDeaths_dupla),
        "§7KDR final: §b" + FORMAT.format((double) finalKills_dupla / (double) finalDeaths_dupla), "").build());



    long wins_trio = statistics.getLong("wins", "trio");
    long games_trio = statistics.getLong("games", "trio");
    long deaths_trio = statistics.getLong("deaths", "trio");
    long kills_trio = statistics.getLong("kills", "trio");
    long finalDeaths_trio = statistics.getLong("finalDeaths", "trio");
    long finalKills_trio = statistics.getLong("finalKills", "trio");
    this.setItem(23, new ItemBuilder(Material.PAPER).amount(3).name("§aEstatísticas do modo trio")
      .lore("", "§7Partidas jogadas: §f" + StringUtils.formatNumber(games_trio), "§7Vitórias: §6" + StringUtils.formatNumber(wins_trio),
        "§7Derrotas: §c" + StringUtils.formatNumber(games_trio - wins_trio),
        "§7Frequência de vitória: §b" + StringUtils.formatNumber(((double) wins_trio / (double) games_trio) * 100) + "%", "",
        "§7Camas quebradas: §c" + StringUtils.formatNumber(statistics.getLong("bedsBroken", "trio")),
        "§7Camas perdidas: §c" + StringUtils.formatNumber(statistics.getLong("bedsLost", "trio")), "", "§7Abates: §e" + StringUtils.formatNumber(kills_trio),
        "§7Mortes: §e" + StringUtils.formatNumber(deaths_trio), "§7KDR: §b" + FORMAT.format((double) kills_trio / (double) deaths_trio), "",
        "§7Abates finais: §e" + StringUtils.formatNumber(finalKills_trio), "§7Mortes finais: §e" + StringUtils.formatNumber(finalDeaths_trio),
        "§7KDR final: §b" + FORMAT.format((double) finalKills_trio / (double) finalDeaths_trio), "").build());


    long wins_squad = statistics.getLong("wins", "squad");
    long games_squad = statistics.getLong("games", "squad");
    long deaths_squad = statistics.getLong("deaths", "squad");
    long kills_squad = statistics.getLong("kills", "squad");
    long finalDeaths_squad = statistics.getLong("finalDeaths", "squad");
    long finalKills_squad = statistics.getLong("finalKills", "squad");
    this.setItem(25, new ItemBuilder(Material.PAPER).amount(4).name("§aEstatísticas do modo quartetos")
      .lore("", "§7Partidas jogadas: §f" + StringUtils.formatNumber(games_squad), "§7Vitórias: §6" + StringUtils.formatNumber(wins_squad),
        "§7Derrotas: §c" + StringUtils.formatNumber(games_squad - wins_squad),
        "§7Frequência de vitória: §b" + StringUtils.formatNumber(((double) wins_squad / (double) games_squad) * 100) + "%", "",
        "§7Camas quebradas: §c" + StringUtils.formatNumber(statistics.getLong("bedsBroken", "squad")),
        "§7Camas perdidas: §c" + StringUtils.formatNumber(statistics.getLong("bedsLost", "squad")), "", "§7Abates: §e" + StringUtils.formatNumber(kills_squad),
        "§7Mortes: §e" + StringUtils.formatNumber(deaths_squad), "§7KDR: §b" + FORMAT.format((double) kills_squad / (double) deaths_squad), "",
        "§7Abates finais: §e" + StringUtils.formatNumber(finalKills_squad), "§7Mortes finais: §e" + StringUtils.formatNumber(finalDeaths_squad),
        "§7KDR final: §b" + FORMAT.format((double) finalKills_squad / (double) finalDeaths_squad), "").build());



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
