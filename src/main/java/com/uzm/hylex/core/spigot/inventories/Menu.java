package com.uzm.hylex.core.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maxter
 */
public class Menu {

  private Inventory inventory;
  private List<Integer> slots = new ArrayList<>();
  private Map<Integer, Object> attached = new HashMap<>();

  public Menu() {
    this("", 1);
  }

  public Menu(String title) {
    this(title, 1);
  }

  public Menu(String title, int rows) {
    this.inventory = Bukkit.createInventory(null, rows > 6 || rows < 1 ? 9 : rows * 9, title);
    for (int i = 0; i < this.inventory.getSize(); i++) {
      this.slots.add(i);
    }
  }

  public void attachObject(int slot, Object value) {
    this.attached.put(slot, value);
  }

  public void setItem(int slot, ItemStack item) {
    this.inventory.setItem(slot, item);
  }

  public void setItems(List<ItemStack> items) {
    for (int i = 0; i < this.slots.size(); i++) {
      if (i >= items.size()) {
        break;
      }

      ItemStack item = items.get(i);
      this.inventory.setItem(this.slots.get(i), item);
    }
  }
  public void remove(int slot) {
    this.inventory.setItem(slot, new ItemStack(Material.AIR));
  }

  public void removeAll() {
    this.slots.forEach(slot -> this.inventory.setItem(slot, new ItemStack(Material.AIR)));
  }

  public void clear() {
    this.inventory.clear();
  }

  public ItemStack getItem(int slot) {
    return this.inventory.getItem(slot);
  }

  public Object getAttached(int slot) {
    return this.attached.get(slot);
  }

  public ItemStack[] getContents() {
    return this.inventory.getContents();
  }

  public Inventory getInventory() {
    return inventory;
  }

  public List<Integer> getSlots() {
    return slots;
  }
}