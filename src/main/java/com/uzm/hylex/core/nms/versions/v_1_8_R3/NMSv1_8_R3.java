package com.uzm.hylex.core.nms.versions.v_1_8_R3;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.java.util.MathUtils;
import com.uzm.hylex.core.libraries.holograms.api.Hologram;
import com.uzm.hylex.core.libraries.holograms.api.HologramLine;
import com.uzm.hylex.core.libraries.npclib.npc.EntityControllers;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.nms.interfaces.IArmorStand;
import com.uzm.hylex.core.nms.interfaces.INMS;
import com.uzm.hylex.core.nms.reflections.Accessors;
import com.uzm.hylex.core.nms.reflections.acessors.FieldAccessor;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.EntityNPCPlayer;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.EntityStand;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.HumanController;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.PlayerlistTrackerEntry;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.UUIDMetadataStore;
import com.uzm.hylex.core.spigot.features.Titles;
import com.uzm.hylex.core.utils.Utils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class NMSv1_8_R3 implements INMS {
  private FieldAccessor<Set> SET_TRACKERS;

  private static final Location FROM_LOCATION = new Location(null, 0, 0, 0);
  private static final Set<EntityType> BAD_CONTROLLER_LOOK = EnumSet.of(EntityType.SILVERFISH, EntityType.ENDERMITE,
    EntityType.ENDER_DRAGON, EntityType.BAT, EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.HORSE,
    EntityType.GHAST);

  public NMSv1_8_R3() {
    SET_TRACKERS = Accessors.getField(EntityTracker.class, "c", Set.class);

    FieldAccessor<PlayerMetadataStore> metadatastore = Accessors.getField(CraftServer.class, "playerMetadata", PlayerMetadataStore.class);
    if (!(metadatastore.get(Bukkit.getServer()) instanceof UUIDMetadataStore)) {
      metadatastore.set(Bukkit.getServer(), new UUIDMetadataStore());
    }

    EntityControllers.registerEntityController(EntityType.PLAYER, HumanController.class);
  }

  @Override
  public void look(org.bukkit.entity.Entity entity, float yaw, float pitch) {
    Entity handle = getHandle(entity);
    if (handle == null)
      return;
    yaw = Utils.clampYaw(yaw);
    handle.yaw = yaw;
    setHeadYaw(entity, yaw);
    handle.pitch = pitch;
  }

  @Override
  public void look(org.bukkit.entity.Entity entity, Location to, boolean headOnly, boolean immediate) {
    Entity handle = getHandle(entity);
    if (immediate || headOnly || BAD_CONTROLLER_LOOK.contains(handle.getBukkitEntity().getType()) || (!(handle instanceof EntityInsentient) && !(handle instanceof EntityNPCPlayer))) {
      Location fromLocation = entity.getLocation(FROM_LOCATION);
      double xDiff, yDiff, zDiff;
      xDiff = to.getX() - fromLocation.getX();
      yDiff = to.getY() - fromLocation.getY();
      zDiff = to.getZ() - fromLocation.getZ();

      double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
      double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

      double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
      double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90;
      if (zDiff < 0.0)
        yaw += Math.abs(180 - yaw) * 2;
      if (handle instanceof EntityEnderDragon) {
        yaw = getDragonYaw(handle, to.getX(), to.getZ());
      } else {
        yaw = yaw - 90;
      }
      if (headOnly) {
        setHeadYaw(entity, (float) yaw);
      } else {
        look(entity, (float) yaw, (float) pitch);
      }
      return;
    }
    if (handle instanceof EntityInsentient) {
      ((EntityInsentient) handle).getControllerLook().a(to.getX(), to.getY(), to.getZ(), 10, ((EntityInsentient) handle).bQ());
      while (((EntityInsentient) handle).aK >= 180F) {
        ((EntityInsentient) handle).aK -= 360F;
      }
      while (((EntityInsentient) handle).aK < -180F) {
        ((EntityInsentient) handle).aK += 360F;
      }
    } else {
      ((EntityNPCPlayer) handle).setTargetLook(to);
    }
  }

  @Override
  public void look(org.bukkit.entity.Entity from, org.bukkit.entity.Entity to) {
    Entity handle =getHandle(from), target = getHandle(to);
    if (BAD_CONTROLLER_LOOK.contains(handle.getBukkitEntity().getType())) {
      if (to instanceof LivingEntity) {
        look(from, ((LivingEntity) to).getEyeLocation(), false, true);
      } else {
        look(from, to.getLocation(), false, true);
      }
    } else if (handle instanceof EntityInsentient) {
      ((EntityInsentient) handle).getControllerLook().a(target, 10, ((EntityInsentient) handle).bQ());
      while (((EntityLiving) handle).aK >= 180F) {
        ((EntityLiving) handle).aK -= 360F;
      }
      while (((EntityLiving) handle).aK < -180F) {
        ((EntityLiving) handle).aK += 360F;
      }
    } else if (handle instanceof EntityNPCPlayer) {
      ((EntityNPCPlayer)handle).setTargetLook(target, 10F, 40F);
    }
  }

  public void updateAI(Object entity) {
      ((EntityNPCPlayer) entity).updateAI();

  }

  @Override
  public void updateNavigation(Object navigation) {
    ((NavigationAbstract)navigation).k();
  }


  @Override
  public ItemStack glow(ItemStack stackToGlow) {
    if (stackToGlow == null)
      return null;
    net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stackToGlow);
    NBTTagCompound tag = null;
    if (!nmsStack.hasTag()) {
      tag = new NBTTagCompound();
      nmsStack.setTag(tag);
    }
    if (tag == null)
      tag = nmsStack.getTag();
    NBTTagList ench = new NBTTagList();
    tag.set("ench", ench);
    nmsStack.setTag(tag);
    return CraftItemStack.asCraftMirror(nmsStack);
  }

  public void sendTitle(Player player, Titles.TitleType type, String bottomMessage, String topMessage, int fadeIn, int stayTime, int fadeOut) {
    PacketPlayOutTitle top =
      new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(topMessage), fadeIn, stayTime, fadeOut);
    PacketPlayOutTitle bottom =
      new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(bottomMessage), fadeIn, stayTime, fadeOut);

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
    IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
  }

  @Override
  public void sendTabColor(Player player, String footer, String bottom) {
    CraftPlayer craftplayer = (CraftPlayer) player;
    IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
    IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom + "\"}");
    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
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
    craftplayer.getHandle().playerConnection.sendPacket(packet);
  }

  @Override
  public boolean addToWorld(World world, org.bukkit.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
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
    SkinnableEntity[] skinnables = skinnableEntities.toArray(new SkinnableEntity[skinnableEntities.size()]);
    EntityPlayer[] entityPlayers = new EntityPlayer[skinnableEntities.size()];

    for (int i = 0; i < skinnables.length; i++) {
      entityPlayers[i] = (EntityPlayer) skinnables[i];
    }

    sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayers));
  }

  @Override
  public void sendTabListRemove(Player player, Player listPlayer) {
    sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
  }

  @Override
  public void sendPacket(Player player, Object packet) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
  }

  @Override
  public SkinnableEntity getSkinnable(org.bukkit.entity.Entity entity) {
    Preconditions.checkNotNull(entity);
    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    if (nmsEntity instanceof SkinnableEntity) {
      return (SkinnableEntity) nmsEntity;
    }

    return null;
  }

  @Override
  public void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    if (nmsEntity instanceof EntityLiving) {
      EntityLiving living = (EntityLiving) nmsEntity;
      yaw = MathUtils.clampYaw(yaw);
      living.aK = yaw;
      if (living instanceof EntityHuman) {
        living.aI = yaw;
      }
      living.aL = yaw;
    }
  }

  @Override
  public void setStepHeight(LivingEntity entity, float height) {
    ((CraftLivingEntity) entity).getHandle().S = height;
  }

  @Override
  public float getStepHeight(LivingEntity entity) {
    return ((CraftLivingEntity) entity).getHandle().S;
  }

  @Override
  public void replaceTrackerEntry(Player player) {
    WorldServer server = ((CraftWorld) player.getWorld()).getHandle();
    EntityTrackerEntry entry = server.getTracker().trackedEntities.get(player.getEntityId());

    if (entry != null) {
      PlayerlistTrackerEntry replace = new PlayerlistTrackerEntry(entry);
      server.getTracker().trackedEntities.a(player.getEntityId(), replace);
      if (SET_TRACKERS != null) {
        Set<Object> set = SET_TRACKERS.get(server.getTracker());
        set.remove(entry);
        set.add(replace);
      }
    }
  }

  @Override
  public void removeFromPlayerList(Player player) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ep.world.players.remove(ep);
  }

  @Override
  public void flyingMoveLogic(LivingEntity e, float f, float f1) {
    EntityLiving entity = ((CraftLivingEntity) e).getHandle();
    if (entity.bM()) {
      if (entity.V()) {
        double d0 = entity.locY;

        float f3 = 0.8f;
        float f4 = 0.02f;
        float f2 = EnchantmentManager.b(entity);
        if (f2 > 3.0f) {
          f2 = 3.0f;
        }

        if (!entity.onGround) {
          f2 *= 0.5;
        }

        if (f2 > 0.0f) {
          f3 += (0.5460001F - f3) * f2 / 3.0f;
          f4 += (entity.bI() * 1.0f - f4) * f2 / 3.0f;
        }

        entity.a(f, f1, f4);
        entity.move(entity.motX, entity.motY, entity.motZ);
        entity.motX *= f3;
        entity.motY *= 0.800000011920929D;
        entity.motZ *= f3;
        entity.motY -= 0.02D;
        if (entity.positionChanged && entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d0, entity.motZ)) {
          entity.motY = 0.300000011920929D;
        }
      } else if (entity.ab()) {
        double d0 = entity.locY;
        entity.a(f, f1, 0.02F);
        entity.move(entity.motX, entity.motY, entity.motZ);
        entity.motX *= 0.5D;
        entity.motY *= 0.5D;
        entity.motZ *= 0.5D;
        entity.motY -= 0.02D;
        if (entity.positionChanged && entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d0, entity.motZ)) {
          entity.motY = 0.300000011920929D;
        }
      } else {
        float f5 = 0.91F;

        if (entity.onGround) {
          f5 = entity.world.getType(new BlockPosition(MathHelper.floor(entity.locX), MathHelper.floor(entity.getBoundingBox().b) - 1, MathHelper.floor(entity.locZ)))
            .getBlock().frictionFactor * 0.91F;
        }

        float f6 = 0.162771F / (f5 * f5 * f5);
        float f3;
        if (entity.onGround) {
          f3 = entity.bI() * f6;
        } else {
          f3 = entity.aM;
        }

        entity.a(f, f1, f3);
        f5 = 0.91F;
        if (entity.onGround) {
          f5 = entity.world.getType(new BlockPosition(MathHelper.floor(entity.locX), MathHelper.floor(entity.getBoundingBox().b) - 1, MathHelper.floor(entity.locZ)))
            .getBlock().frictionFactor * 0.91F;
        }

        if (entity.k_()) {
          float f4 = 0.15F;
          entity.motX = MathHelper.a(entity.motX, -f4, f4);
          entity.motZ = MathHelper.a(entity.motZ, -f4, f4);
          entity.fallDistance = 0.0F;
          if (entity.motY < -0.15D) {
            entity.motY = -0.15D;
          }

          boolean flag = entity.isSneaking() && entity instanceof EntityHuman;

          if (flag && entity.motY < 0.0D) {
            entity.motY = 0.0D;
          }
        }

        entity.move(entity.motX, entity.motY, entity.motZ);
        if ((entity.positionChanged) && (entity.k_())) {
          entity.motY = 0.2D;
        }

        if (entity.world.isClientSide && (!entity.world.isLoaded(new BlockPosition((int) entity.locX, 0, (int) entity.locZ)) || !entity.world
          .getChunkAtWorldCoords(new BlockPosition((int) entity.locX, 0, (int) entity.locZ)).o())) {
          if (entity.locY > 0.0D) {
            entity.motY = -0.1D;
          } else {
            entity.motY = 0.0D;
          }
        } else {
          entity.motY -= 0.08D;
        }

        entity.motY *= 0.9800000190734863D;
        entity.motX *= f5;
        entity.motZ *= f5;
      }
    }
  }

  @Override
  public void removeFromServerPlayerList(Player player) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ((CraftServer) Bukkit.getServer()).getHandle().players.remove(ep);
  }

  @Override
  public void removeFromWorld(org.bukkit.entity.Entity entity) {
    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    nmsEntity.world.removeEntity(nmsEntity);
  }


  @Override
  public void playAnimation(org.bukkit.entity.Entity entity, int id) {
    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(nmsEntity, id);
    NMS.sendPacketNearby(null, entity.getLocation(), packet, 15);
  }

  @Override
  public void sendPacketsNearby(Player from, Location location, Collection<Object> packets, double radius) {
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

  @Override
  public void sendPacketNearby(Player from, Location location, Object packet, double radius) {
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


    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();

    net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(stack);
    ;

    PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entity.getEntityId(), slot, nmsItemStack);
    sendPacketNearby(null, entity.getLocation(), packet, 10);
  }


  @Override
  public Hologram getHologram(org.bukkit.entity.Entity entity) {
    if (entity == null) {
      return null;
    }

    if (!(entity instanceof CraftArmorStand)) {
      return null;
    }

    net.minecraft.server.v1_8_R3.Entity en = ((CraftEntity) entity).getHandle();
    if (!(en instanceof EntityArmorStand)) {
      return null;
    }

    HologramLine e = ((com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.EntityArmorStand) en).getLine();
    return e != null ? e.getHologram() : null;
  }

  @Override
  public IArmorStand createArmorStand(Location location, String name, HologramLine line) {

    IArmorStand armor =
      line == null ? new EntityStand(location) : new com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), line);

    Entity entity = (Entity) armor;

    armor.setLocation(location.getX(), location.getY(), location.getZ());

    entity.yaw = location.getYaw();
    entity.pitch = location.getPitch();
    armor.setName(name);
    if (this.addEntity(entity)) {
      return armor;
    }

    return null;
  }


  @Override
  public boolean isHologramEntity(org.bukkit.entity.Entity entity) {
    return this.getHologram(entity) != null;
  }


  private boolean addEntity(net.minecraft.server.v1_8_R3.Entity entity) {
    try {
      return entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }


  @Override
  public boolean isNavigationFinished(Object navigation) {
    return ((NavigationAbstract)navigation).m();
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

  public static Entity getHandle(org.bukkit.entity.Entity entity) {
    if (!(entity instanceof CraftEntity))
      return null;
    return ((CraftEntity) entity).getHandle();
  }

  private float getDragonYaw(Entity handle, double tX, double tZ) {
    if (handle.locZ > tZ)
      return (float) (-Math.toDegrees(Math.atan((handle.locX - tX) / (handle.locZ - tZ))));
    if (handle.locZ < tZ) {
      return (float) (-Math.toDegrees(Math.atan((handle.locX - tX) / (handle.locZ - tZ)))) + 180.0F;
    }
    return handle.yaw;
  }


}
