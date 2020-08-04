package com.uzm.hylex.core.nms.versions.v_1_8_R3.entity;

import com.mojang.authlib.GameProfile;
import com.uzm.hylex.core.java.util.MathUtils;
import com.uzm.hylex.core.libraries.npclib.trait.LookClose;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.network.EmptyNetHandler;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.player.PlayerControllerJump;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.player.PlayerControllerLook;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.player.PlayerControllerMove;
import com.uzm.hylex.core.nms.versions.v_1_8_R3.utils.player.PlayerNavigation;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.hylex.core.libraries.npclib.npc.skin.Skin;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinPacketTracker;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;


/**
 * @author Maxter
 */
public class EntityNPCPlayer extends EntityPlayer implements NPCHolder, SkinnableEntity {

  private final NPC npc;
  private Skin skin;
  private final SkinPacketTracker skinTracker;
  private PlayerControllerLook controllerLook;
  private PlayerNavigation navigation;
  private PlayerControllerMove controllerMove;
  private PlayerControllerJump controllerJump;

  public EntityNPCPlayer(MinecraftServer server, WorldServer world, GameProfile profile, PlayerInteractManager manager, NPC npc) {
    super(server, world, profile, manager);

    this.npc = npc;
    if (npc != null) {
      manager.setGameMode(EnumGamemode.SURVIVAL);
      skinTracker = new SkinPacketTracker(this);
      initialise();
    } else {
      skinTracker = null;
    }
  }

  protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {
    if (npc == null || !npc.isFlyable()) {
      super.a(d0, flag, block, blockposition);
    }
  }


  @Override
  public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
    super.collide(entity);
  }

  @Override
  public boolean damageEntity(DamageSource damagesource, float f) {
    return super.damageEntity(damagesource, f);
  }

  public void die(DamageSource damagesource) {
    if (this.dead) {
      return;
    }

    if (npc == null) {
      super.die(damagesource);
      return;
    }

    super.die(damagesource);
    Bukkit.getScheduler().runTaskLater(NPCLibrary.getPlugin(), () -> world.removeEntity(EntityNPCPlayer.this), 35L);
  }

  @Override
  public void e(float f, float f1) {
    if (npc == null || !npc.isFlyable()) {
      super.e(f, f1);
    }
  }

  public NavigationAbstract getNavigation() {
    return navigation;
  }


  public void setTargetLook(net.minecraft.server.v1_8_R3.Entity target, float yawOffset, float renderOffset) {
    controllerLook.a(target, yawOffset, renderOffset);
  }

  public void setTargetLook(Location target) {
    controllerLook.a(target.getX(), target.getY(), target.getZ(), 10, 40);
  }


  @Override
  public void g(double d0, double d1, double d2) {
    if (npc == null || !npc.isProtected()) {
      super.g(d0, d1, d2);
    }
  }

  @Override
  public void g(float f, float f1) {
    if (npc == null || !npc.isFlyable()) {
      super.g(f, f1);
    } else {
      NMS.flyingMoveLogic(this, f, f1);
    }
  }

  public CraftPlayer getBukkitEntity() {
    if (this.npc != null && bukkitEntity == null) {
      bukkitEntity = new PlayerNPC(this);
    }

    return super.getBukkitEntity();
  }

  public void initialise() {


    AttributeInstance range = getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
    if (range == null) {
      range = getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
    }
    range.setValue(25.0D);

    this.controllerJump = new PlayerControllerJump(this);
    this.controllerMove = new PlayerControllerMove(this);
    this.controllerLook = new PlayerControllerLook(this);
    this.navigation = new PlayerNavigation(this, world);

    this.playerConnection = new EmptyNetHandler(this);
    this.playerConnection.networkManager.a(playerConnection);
    NMS.setStepHeight(getBukkitEntity(), 1.0f);

    setSkinFlags((byte) -1);
  }

  @Override
  public boolean k_() {
    if (npc == null || !npc.isFlyable()) {
      return super.k_();
    }

    return false;
  }

  @Override
  public void t_() {
    super.t_();

    if (npc == null) {
      return;
    }

    updatePackets();
    if (npc.data().get(NPC.GRAVITY, false) && getBukkitEntity() != null && MathUtils.isLoaded(getBukkitEntity().getLocation())) {
      move(0.0D, -0.2D, 0.0D);
    }

    if (Math.abs(this.motX) < 0.00499999988824129D && Math.abs(this.motY) < 0.00499999988824129D && Math.abs(this.motZ) < 0.00499999988824129D) {
      this.motX = this.motY = this.motZ = 0.0D;
    }
    if (this.motX != 0.0D || this.motZ != 0.0D || this.motY != 0.0D) {
      g(0.0F, 0.0F);
    }

    if (noDamageTicks > 0) {
      noDamageTicks--;
    }

    if (npc.getTrait(LookClose.class) != null) {
      npc.getTrait(LookClose.class).run();
      if (!NMS.isNavigationFinished(navigation)) {
        NMS.updateNavigation(navigation);
      }
    }
    NMS.updateAI(this);

    npc.update();

  }

  private int ticks = 0;

  private void updatePackets() {
    if (ticks++ > 30) {
      ticks = 0;
      for (Entity player : getEntity().getNearbyEntities(64.0, 64.0, 64.0)) {
        if (player instanceof Player) {
          if (!(player instanceof PlayerNPC)) {
            Packet<?>[] packets = new Packet<?>[6];
            packets[5] = new PacketPlayOutEntityHeadRotation(this, (byte) MathHelper.d(aK * 256.0F / 360.0F));
            for (int i = 0; i < 5; i++) {
              packets[i] = new PacketPlayOutEntityEquipment(getId(), i, getEquipment(i));
            }

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(this));
            for (Packet<?> packet : packets) {
              ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
          }
        }
      }

      NMS.removeFromPlayerList(getBukkitEntity());
    }
  }
  @Override
  public Player getEntity() {
    return getBukkitEntity();
  }

  @Override
  public NPC getNPC() {
    return npc;
  }

  @Override
  public SkinPacketTracker getSkinTracker() {
    return skinTracker;
  }

  @Override
  public void setSkin(Skin skin) {
    if (skin != null) {
      skin.apply(this);
    }

    this.skin = skin;
  }

  public void updateAI() {
    controllerMove.a();
    controllerLook.a();
    controllerJump.b();
  }

  @Override
  public Skin getSkin() {
    return skin;
  }

  @Override
  public void setSkinFlags(byte flags) {
    try {
      getDataWatcher().watch(10, flags);
    } catch (NullPointerException e) {
      getDataWatcher().a(10, flags);
    }
  }

  public PlayerControllerJump getControllerJump() {
    return controllerJump;
  }

  public PlayerControllerMove getControllerMove() {
    return controllerMove;
  }

  public PlayerControllerLook getControllerLook() {
    return controllerLook;
  }

  static class PlayerNPC extends CraftPlayer implements SkinnableEntity, NPCHolder {

    private NPC npc;

    public PlayerNPC(EntityNPCPlayer entity) {
      super(entity.world.getServer(), entity);
      this.npc = entity.npc;
    }

    @Override
    public Player getEntity() {
      return this;
    }

    @Override
    public SkinPacketTracker getSkinTracker() {
      return ((SkinnableEntity) entity).getSkinTracker();
    }

    @Override
    public NPC getNPC() {
      return npc;
    }

    @Override
    public void setSkin(Skin skin) {
      ((SkinnableEntity) entity).setSkin(skin);
    }

    @Override
    public Skin getSkin() {
      return ((SkinnableEntity) entity).getSkin();
    }

    @Override
    public void setSkinFlags(byte flags) {
      ((SkinnableEntity) entity).setSkinFlags(flags);
    }
  }
}