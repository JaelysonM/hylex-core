package com.uzm.hylex.core.nms.interfaces;

import com.uzm.hylex.core.libraries.holograms.api.Hologram;
import com.uzm.hylex.core.libraries.holograms.api.HologramLine;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.hylex.core.spigot.features.Titles;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface INMS {

    ItemStack glow(ItemStack stackToGlow);

    void sendTitle(Player player, Titles.TitleType type, String bottom, String top, int fadeIn, int stayTime, int fadeOut);

    void sendActionBar(Player player, String message);

    void sendTabColor(Player player, String footer, String bottom);

    boolean addToWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason);


    void setValueAndSignature(Player player, String value, String signature);

    void sendTabListAdd(Player player, Player listPlayer);

    void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities);

    void sendTabListRemove(Player player, Player listPlayer);

    void sendPacket(Player player, Object packet);

    SkinnableEntity getSkinnable(Entity entity);

    void setHeadYaw(Entity entity, float yaw);

    void setStepHeight(LivingEntity entity, float height);

    float getStepHeight(LivingEntity entity);

    void replaceTrackerEntry(Player player);

    void removeFromPlayerList(Player type);

    void flyingMoveLogic(LivingEntity entity, float f, float f1);

    void removeFromServerPlayerList(Player player);


    void removeFromWorld(Entity entity);


    void playAnimation(Entity entity, int id);

    void sendPacketNearby(Player from, Location location, Object packet, double radius);

    void sendPacketsNearby(Player from, Location location, Collection<Object> packets, double radius);

    void refreshNPCSlot(Entity entity, int slot , ItemStack stack);

    void look(org.bukkit.entity.Entity entity, float yaw, float pitch);

    boolean isHologramEntity(Entity entity);

    boolean isNavigationFinished(Object navigation);


    Hologram getHologram(Entity entity);

    IArmorStand createArmorStand(Location location, String name, HologramLine line);

    String[]getPlayerTextures(Player player);

    void look(org.bukkit.entity.Entity entity, Location to, boolean headOnly, boolean immediate);

    void look(org.bukkit.entity.Entity from, org.bukkit.entity.Entity to);

    void updateNavigation(Object navigation);

    void updateAI(Object entity);




}

