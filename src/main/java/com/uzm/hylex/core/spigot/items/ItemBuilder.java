package com.uzm.hylex.core.spigot.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.nms.NMS;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class ItemBuilder {
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

    public ItemBuilder(String item) {
        if (item == null || item.isEmpty()) {
            this.item = null;
        }

        try {
            item = ChatColor.translateAlternateColorCodes('&', item.replace("\\n", "\n"));
            String[] split = item.split(" : ");
            String mat = split[0].split(":")[0];

            ItemStack stack = new ItemStack(Material.matchMaterial(mat.toUpperCase()));
            if (split[0].split(":").length > 1)
                stack.setDurability((short) Integer.parseInt(split[0].split(":")[1]));
            this.meta = stack.getItemMeta();

            BookMeta book = meta instanceof BookMeta ? ((BookMeta) meta) : null;
            SkullMeta skull = meta instanceof SkullMeta ? ((SkullMeta) meta) : null;
            PotionMeta potion = meta instanceof PotionMeta ? ((PotionMeta) meta) : null;
            FireworkEffectMeta effect = meta instanceof FireworkEffectMeta ? ((FireworkEffectMeta) meta) : null;
            LeatherArmorMeta
                    armor = meta instanceof LeatherArmorMeta ? ((LeatherArmorMeta) meta) : null;
            EnchantmentStorageMeta enchantment = meta instanceof EnchantmentStorageMeta ? ((EnchantmentStorageMeta) meta) : null;

            if (split.length > 1) {
                stack.setAmount(Integer.parseInt(split[1]) > 64 ? 64 : Integer.parseInt(split[1]));
            }

           this.lore = new ArrayList<>();
            for (int i = 2; i < split.length; i++) {
                String opt = split[i];

                if (opt.startsWith("display=")) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', opt.split("=")[1]));
                }

                if (opt.startsWith("lore=")) {
                    for (String lored : opt.split("=")[1].split("\n")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', lored));
                    }
                }

                if (opt.startsWith("enchantments=")) {
                    for (String enchanted : opt.split("=")[1].split("\n")) {
                        if (enchantment != null) {
                            enchantment.addStoredEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                            continue;
                        }

                        meta.addEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                    }
                }

                if (opt.startsWith("owner=") && skull != null) {
                    skull.setOwner(opt.split("=")[1]);
                }

                if (opt.startsWith("skinvalue=") && skull != null) {
                    try {
                        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
                        gp.getProperties().put("textures", new Property("textures", opt.split("=")[1]));
                        Field f = skull.getClass().getDeclaredField("profile");
                        f.setAccessible(true);
                        f.set(meta, gp);
                    } catch (ReflectiveOperationException ex) {
                        Core.getInstance().getLogger().log(Level.WARNING, "Unexpected error ocurred profile on skull: ", ex);
                    }
                }

                if (opt.startsWith("page=") && book != null) {
                    book.setPages(opt.split("=")[1].split("\\{page\\}"));
                }

                if (opt.startsWith("author=") && book != null) {
                    book.setAuthor(opt.split("=")[1]);
                }

                if (opt.startsWith("title=") && book != null) {
                    book.setTitle(opt.split("=")[1]);
                }

                if (opt.startsWith("effect=") && potion != null) {
                    String[] splitter = opt.split("=")[1].split("\n");
                    potion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(splitter[0]), Integer.parseInt(splitter[2]), Integer.parseInt(splitter[1])), false);
                }

                if (opt.startsWith("flags=")) {
                    String[] flags = opt.split("=")[1].split("\n");
                    for (String flag : flags) {
                        if (flag.equalsIgnoreCase("all")) {
                            meta.addItemFlags(ItemFlag.values());
                            break;
                        } else {
                            meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                        }
                    }
                }
            }
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }

            stack.setItemMeta(meta);

            this.item = stack;
        } catch (Exception ex) {
            Core.getInstance().getLogger().log(Level.WARNING, "Cant deserializeItem() from \"" + item + "\"", ex);
            this.item = new ItemStack(Material.BARRIER, 1);

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
        this.meta.setDisplayName(dis.replace("&", "§"));
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


    public ItemStack glow() {

        return NMS.glow(this.item);

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
        return glow ? glow() : this.item;
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
        return glow ? glow() : this.item;
    }


    public ItemBuilder updateLoreLine(int line, String text) {

        if (item.getItemMeta().hasLore()) {
            List<String> lores = new ArrayList<String>(item.getItemMeta().getLore());
            if (lores.get(line) != null) {
                lores.set(line, text);
                item.getItemMeta().setLore(lores);
            }

        }

        return this;
    }

    public ItemBuilder color(Color color){
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null && meta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta) meta).setColor(color);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder skinFromValue(String value) {

        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
        return this;
    }


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
