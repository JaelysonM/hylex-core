package com.uzm.hylex.core.nms;


import com.uzm.hylex.core.libraries.holograms.api.Hologram;
import com.uzm.hylex.core.libraries.holograms.api.HologramLine;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.hylex.core.nms.interfaces.IArmorStand;
import com.uzm.hylex.core.nms.interfaces.INMS;
import com.uzm.hylex.core.nms.interfaces.ISkinFactory;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.NMSv1_8_R3;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.SkinFactory;
import com.uzm.hylex.core.spigot.enums.MinecraftVersion;
import com.uzm.hylex.core.spigot.features.Titles;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class NMS {

  private static INMS nms;
  private static ISkinFactory factory;


  public static boolean setupNMS(MinecraftVersion version) {

    if (version == MinecraftVersion.V1_8) {
      nms = new NMSv1_8_R3();
      factory = new SkinFactory();
      return true;
    }
    return false;
  }

  public static ItemStack glow(ItemStack stackToGlow) {
    return nms.glow(stackToGlow);
  }

  public static void sendTitle(Player player, Titles.TitleType type, String bottom, String top, int fadeIn, int stayTime, int fadeOut) {
    nms.sendTitle(player, type, bottom, top, fadeIn, stayTime, fadeOut);
  }

  public static void sendActionBar(Player player, String message) {
    nms.sendActionBar(player, message);
  }

  public static void sendTabColor(Player player, String footer, String bottom) {
    nms.sendTabColor(player, footer, bottom);
  }

  public static boolean addToWorld(World world, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
    return nms.addToWorld(world, entity, reason);
  }

  public static void setValueAndSignature(Player player, String value, String signature) {
    nms.setValueAndSignature(player, value, signature);
  }

  public static void sendPacket(Player player, Object packet) {
    nms.sendPacket(player, packet);
  }

  public static void sendTabListAdd(Player player, Player listPlayer) {
    nms.sendTabListAdd(player, listPlayer);
  }

  public static void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
    nms.sendTabListRemove(player, skinnableEntities);
  }

  public static void sendTabListRemove(Player player, Player listPlayer) {
    nms.sendTabListRemove(player, listPlayer);
  }

  public static SkinnableEntity getSkinnable(Entity entity) {
    return nms.getSkinnable(entity);
  }


  public static void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
    nms.setHeadYaw(entity, yaw);
  }

  public static void setStepHeight(LivingEntity entity, float height) {
    nms.setStepHeight(entity, height);
  }

  public static float getStepHeight(LivingEntity entity) {
    return nms.getStepHeight(entity);
  }

  public static void replaceTrackerEntry(Player player) {
    nms.replaceTrackerEntry(player);
  }

  public static void flyingMoveLogic(LivingEntity entity, float f, float f1) {
    nms.flyingMoveLogic(entity, f, f1);
  }


  public static void removeFromPlayerList(Player type) {
    nms.removeFromPlayerList(type);
  }

  public static void updateAI(Object entity) {
    nms.updateAI(entity);
  }
  public static void updateNavigation(Object navigation) {
    nms.updateNavigation(navigation);
  }

  public static boolean isNavigationFinished(Object navigation) {
    return nms.isNavigationFinished(navigation);
  }


  public static void removeFromServerPlayerList(Player player) {
    nms.removeFromServerPlayerList(player);
  }

  public static void look(Entity entity, float yaw, float pitch) {
    nms.look(entity, yaw, pitch);
  }

  public static void look(Entity from, Entity to) {
    nms.look(from, to);
  }

  public static void look(org.bukkit.entity.Entity entity, Location to, boolean headOnly, boolean immediate) {
    nms.look(entity, to, headOnly, immediate);
  }

  public static void removeFromWorld(Entity entity) {
    nms.removeFromWorld(entity);
  }

  public static void playAnimation(Entity entity, int id) {
    nms.playAnimation(entity, id);
  }

  public static void sendPacketNearby(Player from, Location location, Object packet, double radius) {
    nms.sendPacketNearby(from, location, packet, radius);
  }

  public static void sendPacketsNearby(Player from, Location location, Collection<Object> packet, double radius) {
    nms.sendPacketsNearby(from, location, packet, radius);
  }

  public static void refreshNPCSlot(Entity entity, int slot, ItemStack stack) {
    nms.refreshNPCSlot(entity, slot, stack);
  }

  public static IArmorStand createArmorStand(Location location, String name, HologramLine line) {
    return nms.createArmorStand(location, name, line);
  }

  public static Hologram getHologram(Entity entity) {
    return nms.getHologram(entity);
  }

  public static boolean isHologramEntity(Entity entity) {
    return nms.isHologramEntity(entity);
  }

  public static String[] getPlayerTextures(Player player) {
    return nms.getPlayerTextures(player);
  }

  public static void applySkin(Player p, Object props) {
     factory.applySkin(p,props);
  }

  public static void removeSkin(Player p) {
    factory.removeSkin(p);
  }

  public static void updateSkin(Player p) {
    factory.updateSkin(p);
  }

  public static void clearPathfinderGoals(Object entity) {
    nms.clearPathfinderGoals(entity);
  }
  public static void refreshPlayer(Player player) {
    nms.refreshPlayer(player);
  }




}
