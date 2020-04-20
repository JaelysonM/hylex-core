package com.uzm.hylex.core.spigot.inventorys;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ProgressBarMenu {

    private Inventory base;

    private double current;

    private double maxreach;

    private int segments;
    private int[] setup;

    private ItemStack[] stacks;

    public ProgressBarMenu(Inventory inv, double now, double max, int segments, ItemStack[] stacks, int[] setup) {
        this.base = inv;

        this.current = now;

        this.maxreach = max;

        this.segments = segments;

        this.stacks = stacks;

        this.setup = setup;

    }

    public void build() {
        int value = (int) (((double) current / (double) maxreach * 100) / (100 / segments));

        for (int x = 0; x < segments; x++) {
            int slot = setup[0] + x;
            if (x <= (value - 1)) {

                base.setItem(slot, stacks[0]);
            } else {
                base.setItem(slot, stacks[1]);

            }

        }
    }

}
