package com.uzm.hylex.core.spigot.inventorys;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.spigot.enums.MaterialEnums;
import com.uzm.hylex.core.spigot.enums.SoundEnums;
import com.uzm.hylex.core.spigot.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static com.uzm.hylex.core.Core.getLoader;

import java.lang.reflect.Method;
import java.util.Objects;

public class AcceptableInventory extends Menu implements Listener{

    private String[] lore;
    private Player player;
    private ItemStack core;
    private Method method;

    public AcceptableInventory(Player p, ItemStack core, String[] lore) {
        super("§7Confirmação", 4);
        this.player = p;
        this.core = core;
        this.lore = lore;
        setup();

    }
    public void setup() {

        setItem(11, new ItemBuilder(MaterialEnums.RED_WOOL.getItemStack()).name("§cRecusar")
                .lore("§7Deseja cancelar essa ação?").build());

        setItem(13, new ItemBuilder(core.clone()).lore(lore).build());

        setItem(15, new ItemBuilder(MaterialEnums.GREEN_WOOL.getItemStack()).durability(5).name("§aConfirmar")
                .lore("§7Deseja aceitar fazer essa ação?").build());

        setItem(31, new ItemBuilder(Material.SEA_LANTERN).amount(30).name("§3Tempo").build());
    }

    public void build(Method m) {
        method = m;
        new BukkitRunnable() {
            float x = 30.0F;

            @Override
            public void run() {
                if (!getInventory().getViewers().contains(player)) {
                    cancel();
                } else {
                    if (x <= 0) {
                        try {
                            m.invoke("", player);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cancel();
                        HandlerList.unregisterAll(AcceptableInventory.this);
                        return;
                    }
                    x -= 0.5F;
                    setItem(31, new ItemBuilder(getItem(31).clone())
                            .amount(Math.round(x) == 0 ? 1 : Math.round(x)).build());
                    if (x >= 1 && x <= 5 && x == Math.round(x)) {
                        if (getInventory().getViewers().contains(player)) {
                            player.playSound(player.getLocation(), Objects.requireNonNull(SoundEnums.ENTITY_PLAYER_LEVELUP.getFixedSound(getLoader().getSpigotVersion())), 1, 1);
                        }
                    }
                }

            }
        }.runTaskTimer(Core.getInstance(), 1L, 10L);
        open(player);
    }

    @EventHandler
    void click(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        if ((item != null) && (item.hasItemMeta())) {
            if (e.getInventory().equals(getInventory())) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {

                    try {
                        method.invoke("", player);
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                    p.closeInventory();
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRecusar")) {
                    p.sendMessage("§c* Você recusou essa confirmação");
                    p.closeInventory();

                }

            }
        }
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
