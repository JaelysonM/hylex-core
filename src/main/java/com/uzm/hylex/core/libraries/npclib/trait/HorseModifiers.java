package com.uzm.hylex.core.libraries.npclib.trait;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.nms.reflections.Accessors;
import com.uzm.hylex.core.nms.reflections.acessors.MethodAccessor;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;

/**
 * Persists various {@link Horse} metadata.
 *
 * @see Horse
 */
public class HorseModifiers extends NPCTrait {
    private ItemStack armor = null;
    private boolean carryingChest;
    private Color color = Color.CREAMY;
    private ItemStack saddle = null;
    private Style style = Style.NONE;

    public HorseModifiers(NPC npc) {
        super(npc);
    }

    public ItemStack getArmor() {
        return armor;
    }

    /**
     * @see Horse#getColor()
     */
    public Color getColor() {
        return color;
    }

    public ItemStack getSaddle() {
        return saddle;
    }

    /**
     * @see Horse#getStyle()
     */
    public Style getStyle() {
        return style;
    }

    @Override
    public void onSpawn() {
        updateModifiers();
    }

    @Override
    public void run() {
        if (getNPC().getEntity() instanceof Horse) {
            Horse horse = (Horse) getNPC().getEntity();
            saddle = horse.getInventory().getSaddle();
            armor = horse.getInventory().getArmor();
        }
    }

    public void setArmor(ItemStack armor) {
        this.armor = armor;
    }

    /**
     * @see Horse#setCarryingChest(boolean)
     */
    public void setCarryingChest(boolean carryingChest) {
        this.carryingChest = carryingChest;
        updateModifiers();
    }

    /**
     * @see Horse#setColor(Color)
     */
    public void setColor(Horse.Color color) {
        this.color = color;
        updateModifiers();
    }

    public void setSaddle(ItemStack saddle) {
        this.saddle = saddle;
    }

    /**
     * @see Horse#setStyle(Style)
     */
    public void setStyle(Horse.Style style) {
        this.style = style;
        updateModifiers();
    }

    private void updateModifiers() {
        if (getNPC().getEntity() instanceof Horse) {
            Horse horse = (Horse) getNPC().getEntity();
            horse.setColor(color);
            horse.setStyle(style);
            horse.getInventory().setArmor(armor);
            horse.getInventory().setSaddle(saddle);
        }
        EntityType type = getNPC().getEntity().getType();
        if (type.name().equals("LLAMA") || type.name().equals("TRADER_LLAMA") || type.name().equals("DONKEY")
                || type.name().equals("MULE")) {
            try {
                CARRYING_CHEST_METHOD.invoke(getNPC().getEntity(), carryingChest);
            } catch (Throwable e) {
            }
        }
    }

    private static MethodAccessor CARRYING_CHEST_METHOD;

    static {
        try {
            CARRYING_CHEST_METHOD = Accessors.getMethod(ChestedHorse.class, "setCarryingChest");

        } catch (Throwable e) {
        }
    }
}