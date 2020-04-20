package com.uzm.hylex.core.nms.versions.v_1_14_R1;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.java.util.MathUtils;
import com.uzm.hylex.core.libraries.holograms.api.Hologram;
import com.uzm.hylex.core.libraries.holograms.api.HologramLine;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.hylex.core.nms.interfaces.IArmorStand;
import com.uzm.hylex.core.nms.interfaces.INMS;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.spigot.features.Titles;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;


public class NMSv1_14_R1 implements INMS {

    @Override
    public ItemStack glow(ItemStack stackToGlow) {
        if(stackToGlow == null) return null;
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stackToGlow);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        assert tag != null;
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public void sendTitle(Player player, Titles.TitleType type, String bottomMessage, String topMessage, int fadeIn, int stayTime, int fadeOut) {
        PacketPlayOutTitle top = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(topMessage), fadeIn, stayTime, fadeOut);
        PacketPlayOutTitle bottom = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(bottomMessage), fadeIn, stayTime, fadeOut);

        switch (type) {
            case BOTH:
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottom);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(top);
                break;
            case TITLE:
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(top);
                break;
            default:
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottom);
                break;

        }

    }

    @Override
    public void sendActionBar(Player player, String message) {
       CraftPlayer p = (CraftPlayer) player;
       IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }

    @Override
    public void sendTabColor(Player player, String footer, String bottom) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer +"\"}");
        IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom +"\"}");
       PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();;
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, headerJSON);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerJSON);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    @Override
    public boolean addToWorld(World world, org.bukkit.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
       Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.spawnIn(((CraftWorld) world).getHandle());
        return ((CraftWorld) world).getHandle().addEntity(nmsEntity, reason);
    }

    @Override
    public void setValueAndSignature(Player player, String value, String signature) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        if (value != null && signature != null) {
            profile.getProperties().clear();
            profile.getProperties().put("textures", new Property("textures", value, signature));
        }
    }

    @Override
    public void sendTabListAdd(Player player, Player listPlayer) {
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
    }

    @Override
    public void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
        SkinnableEntity[] skinnables = skinnableEntities.toArray(new SkinnableEntity[0]);
        EntityPlayer[] entityPlayers = new EntityPlayer[skinnableEntities.size()];

        for (int i = 0; i < skinnables.length; i++) {
            entityPlayers[i] = (EntityPlayer) skinnables[i];
        }

        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayers));
    }


    @Override
    public void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }
    @Override
    public void sendTabListRemove(Player player, Player listPlayer) {
        sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
    }

    @Override
    public SkinnableEntity getSkinnable(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull(entity);
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof SkinnableEntity) {
            return (SkinnableEntity) nmsEntity;
        }

        return null;
    }

    @Override
    public void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) nmsEntity;
            yaw = MathUtils.clampYaw(yaw);
            living.aL = yaw;
            if (!(living instanceof EntityHuman)) {
                living.aK= yaw;
            }
            living.aM = yaw;
        }
    }

    @Override
    public void setStepHeight(LivingEntity entity, float height) {
        ((CraftLivingEntity) entity).getHandle().K = height;
    }

    @Override
    public float getStepHeight(LivingEntity entity) {
        return ((CraftLivingEntity) entity).getHandle().K;
    }

    @Override
    public void replaceTrackerEntry(Player player) {

    }

    @Override
    public void removeFromPlayerList(Player player) {
       EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ep.world.getPlayers().remove(ep);
    }

    @Override
    public void flyingMoveLogic(LivingEntity entity, float f, float f1) {

    }

    @Override
    public void removeFromServerPlayerList(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ((CraftServer) Bukkit.getServer()).getHandle().players.remove(ep);
    }

    @Override
    public void removeFromWorld(org.bukkit.entity.Entity entity) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        ((WorldServer)        nmsEntity.world).removeEntity(nmsEntity);
    }


    @Override
    public void playAnimation(org.bukkit.entity.Entity entity, int id) {
     Entity nmsEntity = ((CraftEntity) entity).getHandle();
      PacketPlayOutAnimation packet = new PacketPlayOutAnimation(nmsEntity, id);

      NMS.sendPacketNearby(null, entity.getLocation(), (Packet<?>)packet, 15);
    }

    public  void sendPacketsNearby(Player from, Location location, Collection<Object> packets, double radius) {
        radius *= radius;
        final org.bukkit.World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld() || (from != null && !ply.canSee(from))) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }
            for (Packet<?> packet : Collections.singleton((Packet<?>) packets)) {
                sendPacket(ply, packet);
            }
        }
    }


    public  void sendPacketNearby(Player from, Location location, Object packet, double radius) {
        radius *= radius;
        final org.bukkit.World world = location.getWorld();
        for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
            if (ply == null || world != ply.getWorld() || (from != null && !ply.canSee(from))) {
                continue;
            }
            if (location.distanceSquared(ply.getLocation()) > radius) {
                continue;
            }

            sendPacket(ply, packet);
        }
    }

    @Override
    public void refreshNPCSlot(org.bukkit.entity.Entity entity, int slot, ItemStack stack) {
        CraftHumanEntity nmsEntity = ((EntityHuman) entity).getBukkitEntity();
        nmsEntity.setItemInHand(stack);

    }


    @Override
    public Hologram getHologram(org.bukkit.entity.Entity entity) {
     return null;
    }

    @Override
    public IArmorStand createArmorStand(Location location, String name, HologramLine line) {
         return null;
    }


    @Override
    public boolean isHologramEntity(org.bukkit.entity.Entity entity) {
        return false;
    }


    private boolean addEntity(Entity entity) {
        try {
            return entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Override
    public String[] getPlayerTextures(Player player) {
        EntityPlayer playerNMS = ((CraftPlayer) player).getHandle();
        GameProfile profile = playerNMS.getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[] {texture, signature};
    }

}
