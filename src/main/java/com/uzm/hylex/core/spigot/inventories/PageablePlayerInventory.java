package com.uzm.hylex.core.spigot.inventories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.primitives.Ints;
import com.uzm.hylex.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;


public class PageablePlayerInventory implements Listener {

    private Player viewer;

    private Inventory baseInventory;

    private String title;


    private ItemStack ocupeItem = new ItemStack(Material.AIR);

    private Map<ItemStack, Object> attached = new HashMap<>();

    public void attachObject(ItemStack item, Object value) {
        this.attached.put(item, value);
    }

    public Object getAttached(ItemStack item) {
        return this.attached.get(item);
    }



    private int now;
    private int[] availableSlots;

    public PageablePlayerInventory(Player viewer, Inventory base, String title) {
        this.viewer = viewer;
        this.baseInventory = base;
        this.title = title.replace("&", "§");
        Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());

    }

    public PageablePlayerInventory config(int[] avaliableSlots) {
        this.availableSlots = avaliableSlots;
        return this;
    }

    public PageablePlayerInventory fill(ItemStack[] content, Object[][] actionItems, Object[][] fixedContent, Object[] emptyItem) {
        int totalPages = getTotalPages(content);
        int inventorySize = (int) Math.ceil(baseInventory.getSize() / 9.0D) * 9;
        int index= 0;

        ItemStack[] basementContent = new ItemStack[inventorySize];

        for (Object[] o : fixedContent) {
            basementContent[(Integer) o[1]] = (ItemStack) o[0];
        }
        ItemStack[] inventoryContent = basementContent.clone();

        if (content.length == 0) {
            Inventory inv = Bukkit.createInventory(null, inventoryContent.length, title.replace("{c}", (pages.size() + 1) + "").replace("{m}",  totalPages+ ""));
            inv.setContents(inventoryContent);
            inv.setItem((int)emptyItem[0],(ItemStack)emptyItem[1]);
            pages.add(inv);
            return this;
        }
        for (int in = 0; in < content.length; in++) {
            if(inventoryContent[availableSlots[index]]  == null) {
                inventoryContent[availableSlots[index]] = content[in];
            }
            index++;
            if (index == (availableSlots.length) || (in + 1) == content.length) {
                Inventory inv = Bukkit.createInventory(null, inventoryContent.length,
                        title.replace("{c}", (this.pages.size() + 1) + "").replace("{m}", totalPages + ""));
                inv.setContents(inventoryContent);
                if (pages.size() > 0) {
                    inv.setItem((int)actionItems[0][0], (ItemStack) actionItems[0][1]);
                    pages.get((pages.size() - 1)).setItem((int)actionItems[1][0], (ItemStack) actionItems[1][1]);
                }
                this.pages.add(inv);
                inventoryContent= basementContent.clone();
                index=0;
            }
        }

        return this;
    }
    public int getTotalPages(ItemStack[] items) {
        if (items.length < availableSlots.length) {
            return 1;
        } else {
            return items.length % availableSlots.length == 0
                    ? (Math.round((float)items.length / availableSlots.length) == 0 ? 1 : Math.round((float)items.length / availableSlots.length))
                    : (Math.round((float)items.length / availableSlots.length) + 1);
        }
    }

    public PageablePlayerInventory open(Player player, int page) {
        if (page > 0 && page <= this.pages.size()) {
            if (player != null) {
                now = page;
                player.openInventory(this.pages.get(page - 1));
            }
        }
        return this;

    }

    public boolean exists(Inventory inv) {
        return ((Inventory) this.pages.get(this.now - 1)).equals(inv);
    }

    public PageablePlayerInventory destroy() {
        pages.clear();
        return this;
    }

    public int getCurrent() {
        return now;
    }

    public Player getPlayer() {
        return viewer;
    }

    private List<Inventory> pages = Lists.newArrayList();

    public List<Inventory> getPages() {
        return pages;
    }
}
