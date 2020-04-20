package com.uzm.hylex.core.spigot.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.PluginLoader;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public  class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private List<String> lore;
    private boolean glow = false;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.lore = ((this.meta != null) && (this.meta.hasLore()) ? this.meta.getLore() : new ArrayList<>());
    }

    public ItemBuilder(Material mat) {
        this(new ItemStack(mat));
    }

    public ItemBuilder(String string) {
        if ((string == null) || (string.isEmpty())) {
            return;
        }
        String[] split = string.split(" : ");

        if (split[0].split(":").length > 1) {
            this.item = new ItemStack(Material.matchMaterial(split[0].split(":")[0].toUpperCase()));
        } else {
            this.item = new ItemStack(Material.matchMaterial(split[0].toUpperCase()));
        }
        this.meta = this.item.getItemMeta();
        this.lore = new ArrayList<>();
        if (split.length > 1) {
            try {
                amount(Integer.parseInt(split[1]));
            } catch (Exception ignored) {
            }
            for (int i = 2; i < split.length; i++) {
                String splitted = split[i];
                if (splitted.startsWith("name=")) {
                    name(splitted.split("=")[1].replace("&", "§"));
                }
                if (splitted.startsWith("glow=")) {
                    if (Boolean.parseBoolean(splitted.split("glow=")[1])) {
                        glow = true;
                    }
                }
                if (splitted.startsWith("enchant=")) {
                    for (int j = 0; j < splitted.split("=")[1].split("/").length; j++) {
                        if (splitted.split("=")[1].split("/")[j].split(":").length > 1) {
                            try {
                                enchant(getEnchant(splitted.split("=")[1].split("/")[j].split(":")[0]),
                                        Integer.parseInt(splitted.split("=")[1].split("/")[j].split(":")[1]));

                            } catch (Exception localException2) {
                                localException2.printStackTrace();
                            }
                        }
                    }
                }
                List<String> lore = new ArrayList<>();
                if (splitted.startsWith("lore=")) {
                    for (int j = 0; j < splitted.split("=")[1].split("/").length; j++) {
                        lore.add(splitted.split("=")[1].split("/")[j].replace("&", "§"));
                    }
                }
                if (lore.size() >= 1) {
                    lore((String[]) lore.toArray(new String[0]));
                }
            }
        }
    }

    public ItemBuilder material(Material mat) {
        if (this.item == null) {
            this.item = new ItemStack(mat);
        }
        this.item.setType(mat);
        return this;
    }

    public ItemBuilder amount(int amount) {
        if (this.item != null) {
            this.item.setAmount(amount);
        }
        return this;
    }

    public ItemBuilder durability(int durability) {
        if (this.item != null) {
            this.item.setDurability((short) durability);
        }
        return this;
    }

    public ItemBuilder name(String dis) {
        if (this.item == null) {
            return this;
        }
        this.meta.setDisplayName(dis);
        return this;
    }

    public ItemBuilder lore(String... strings) {
        if ((this.item == null) || (this.meta == null)) {
            return this;
        }
        String[] arrayOfString;
        int j = (arrayOfString = strings).length;
        this.lore.addAll(Arrays.asList(arrayOfString).subList(0, j));
        return this;
    }

    public ItemBuilder enchant(Enchantment ench, int level) {
        if (this.item == null) {
            return this;
        }
        this.meta.addEnchant(ench, level, true);
        return this;
    }


    public  ItemStack glow() {
        return Core.getLoader().getNms().glow(this.item);
    }

    public ItemStack build(boolean glow) {
        this.glow = glow;
        if (!this.lore.isEmpty()) {
            this.meta.setLore(this.lore);
            this.lore.clear();
        }
        if (this.meta != null) {
            this.item.setItemMeta(this.meta);
        }
        return glow ? (ItemStack) glow() : this.item;
    }

    public ItemStack build() {
        this.glow = false;
        if (!this.lore.isEmpty()) {
            this.meta.setLore(this.lore);
            this.lore.clear();
        }
        if (this.meta != null) {
            this.item.setItemMeta(this.meta);
        }
        return glow ? (ItemStack) glow() : this.item;
    }

    public ItemStack skin(String url) {
        SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField = null;
        try
        {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        }
        catch (NoSuchFieldException|SecurityException e)
        {
            e.printStackTrace();
        }
        assert (profileField != null);
        profileField.setAccessible(true);
        try
        {
            profileField.set(skullMeta, profile);
        }
        catch (IllegalArgumentException|IllegalAccessException e)
        {
            e.printStackTrace();
        }
        item.setItemMeta(skullMeta);

        return item;
    }
/*
    public static ItemStack itemFromUrl(String url)
    {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if ((url == null) || (url.isEmpty())) {
            return skull;
        }
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        skullMeta.setDisplayName(ChatColor.values()[new Random().nextInt(ChatColor.values().length)] + "AdvancedCosmetics");
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());


        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        }
        catch (NoSuchFieldException|SecurityException e)
        {
            e.printStackTrace();
        }
        assert (profileField != null);
        profileField.setAccessible(true);
        try
        {
            profileField.set(skullMeta, profile);
        }
        catch (IllegalArgumentException|IllegalAccessException e)
        {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
*/
    public Enchantment getEnchant(String enchant) {
        switch (enchant.toLowerCase()) {
            case "protection":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "projectileprotection":
                return Enchantment.PROTECTION_PROJECTILE;
            case "fireprotection":
                return Enchantment.PROTECTION_FIRE;
            case "featherfall":
                return Enchantment.PROTECTION_FALL;
            case "blastprotection":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "respiration":
                return Enchantment.OXYGEN;
            case "aquaaffinity":
                return Enchantment.WATER_WORKER;
            case "sharpness":
                return Enchantment.DAMAGE_ALL;
            case "smite":
                return Enchantment.DAMAGE_UNDEAD;
            case "baneofarthropods":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "knockback":
                return Enchantment.KNOCKBACK;
            case "fireaspect":
                return Enchantment.FIRE_ASPECT;
            case "depthstrider":
                return Enchantment.DEPTH_STRIDER;
            case "looting":
                return Enchantment.LOOT_BONUS_MOBS;
            case "power":
                return Enchantment.ARROW_DAMAGE;
            case "punch":
                return Enchantment.ARROW_KNOCKBACK;
            case "fire":
                return Enchantment.ARROW_FIRE;
            case "infinity":
                return Enchantment.ARROW_INFINITE;
            case "efficiency":
                return Enchantment.DIG_SPEED;
            case "silktouch":
                return Enchantment.SILK_TOUCH;
            case "unbreaking":
                return Enchantment.DURABILITY;
            case "fortune":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "luckofthesea":
                return Enchantment.LUCK;
            case "luck":
                return Enchantment.LUCK;
            case "lure":
                return Enchantment.LURE;
            case "thorns":
                return Enchantment.THORNS;

        }
        return Enchantment.getByName(enchant.toUpperCase());
    }

}
