package com.uzm.hylex.core.nms.versions.v_1_8_R3.entity.npcs;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.libraries.npclib.api.event.NPCEnderTeleportEvent;
import com.uzm.hylex.core.libraries.npclib.api.event.NPCPushEvent;
import com.uzm.hylex.core.libraries.npclib.npc.AbstractNPC;
import com.uzm.hylex.core.libraries.npclib.npc.ai.NPCHolder;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftGuardian;
import org.bukkit.entity.Guardian;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;

public class GuardianController extends MobEntityController {
    public GuardianController() {
        super(EntityGuardianNPC.class);
    }

    @Override
    public Guardian getBukkitEntity() {
        return (Guardian) super.getBukkitEntity();
    }

    public static class EntityGuardianNPC extends EntityGuardian implements NPCHolder {
        private final AbstractNPC npc;

        public EntityGuardianNPC(World world) {
            this(world, null);
        }

        public EntityGuardianNPC(World world, NPC npc) {
            super(world);
            this.npc = (AbstractNPC) npc;
            if (npc != null) {
                NMS.clearPathfinderGoals(this);
            }
        }

        @Override
        protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag, block, blockposition);
            }
        }

        @Override
        protected String bo() {
            return NMS.getSoundEffect(npc, super.bo(), NPC.HURT_SOUND_METADATA);
        }

        @Override
        protected String bp() {
            return NMS.getSoundEffect(npc, super.bp(), NPC.DEATH_SOUND_METADATA);
        }

        @Override
        public boolean cc() {
            if (npc == null)
                return super.cc();
            boolean protectedDefault = npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
            if (!protectedDefault || !npc.data().get(NPC.LEASH_PROTECTED_METADATA, protectedDefault))
                return super.cc();
            if (super.cc()) {
                unleash(true, false); // clearLeash with client update
            }
            return false; // shouldLeash
        }

        @Override
        public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null) {
                Utils.callCollisionEvent(npc, entity.getBukkitEntity());
            }
        }

        @Override
        public boolean d(NBTTagCompound save) {
            return npc == null && super.d(save);
        }

        @Override
        protected void D() {
            if (npc == null) {
                super.D();
            }
        }

        @Override
        public void e(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.e(f, f1);
            }
        }

        @Override
        public void enderTeleportTo(double d0, double d1, double d2) {
            if (npc == null)
                super.enderTeleportTo(d0, d1, d2);
            NPCEnderTeleportEvent event = new NPCEnderTeleportEvent(npc);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                super.enderTeleportTo(d0, d1, d2);
            }
        }

        @Override
        public void g(double x, double y, double z) {
            if (npc == null) {
                super.g(x, y, z);
                return;
            }
            if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                    super.g(x, y, z);
                return;
            }
            Vector vector = new Vector(x, y, z);
            NPCPushEvent event = Utils.callPushEvent(npc, vector);
            if (!event.isCancelled()) {
                vector = event.getCollisionVector();
                super.g(vector.getX(), vector.getY(), vector.getZ());
            }
            // when another entity collides, this method is called to push the
            // NPC so we prevent it from doing anything if the event is
            // cancelled.
        }

        @Override
        public void g(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.g(f, f1);
            } else {
                NMS.flyingMoveLogic(this, f, f1);
            }
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder))
                bukkitEntity = new GuardianNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public boolean k_() {
            if (npc == null || !npc.isFlyable()) {
                return super.k_();
            } else {
                return false;
            }
        }

        @Override
        public void m() {
            if (npc == null) {
                super.m();
            } else {
                npc.update();
            }
        }

        @Override
        public void setElder(boolean flag) {
            float oldw = width;
            float oldl = length;
            super.setElder(flag);
            if (oldw != width || oldl != length) {
                this.setPosition(locX - 0.01, locY, locZ - 0.01);
                this.setPosition(locX + 0.01, locY, locZ + 0.01);
            }
        }

        @Override
        public void setSize(float f, float f1) {
            if (npc == null) {
                super.setSize(f, f1);
            } else {
                NMS.setSize(this, f, f1, justCreated);
            }
        }

        @Override
        protected String z() {
            return NMS.getSoundEffect(npc, super.z(), NPC.AMBIENT_SOUND_METADATA);
        }
    }

    public static class GuardianNPC extends CraftGuardian implements NPCHolder {
        private final AbstractNPC npc;

        public GuardianNPC(EntityGuardianNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}