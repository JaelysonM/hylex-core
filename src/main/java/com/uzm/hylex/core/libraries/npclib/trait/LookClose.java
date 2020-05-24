package com.uzm.hylex.core.libraries.npclib.trait;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.nms.NMS;
import com.uzm.hylex.core.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;


/**
 * Persists the /npc lookclose metadata
 *
 */
public class LookClose extends NPCTrait  {

    private boolean enabled= false;

    private boolean enableRandomLook = false;
    private Player lookingAt;

    private int randomLookDelay = 60;

    private float[] randomPitchRange = { -10, 0 };

    private float[] randomYawRange = { 0, 360 };
    private double range = 5.0D;

    private boolean realisticLooking = true;
    private int t;
    private final NPC npc;

    public LookClose(NPC npc) {
        super(npc);this.npc=npc;
    }

    /**
     * Returns whether the target can be seen. Will use realistic line of sight if {@link #setRealisticLooking(boolean)}
     * is true.
     */
    public boolean canSeeTarget() {
        return realisticLooking && npc.getEntity() instanceof LivingEntity
                ? ((LivingEntity) npc.getEntity()).hasLineOfSight(lookingAt)
                : lookingAt != null && lookingAt.isValid();
    }

    /**
     * Finds a new look-close target
     */
    public void findNewTarget() {
        List<Player> nearby = new ArrayList<>();
        for (Entity entity : npc.getEntity().getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Player))
                continue;

            Player player = (Player) entity;
            if (NPCLibrary.getNPC(entity) != null || player.getGameMode() == GameMode.SPECTATOR
                    || entity.getLocation(CACHE_LOCATION).getWorld() != NPC_LOCATION.getWorld()
                    || player.hasPotionEffect(PotionEffectType.INVISIBILITY) || isPluginVanished((Player) entity))
                continue;
            nearby.add(player);
        }

        if (!nearby.isEmpty()) {
            nearby.sort((o1, o2) -> {
                Location l1 = o1.getLocation(CACHE_LOCATION);
                Location l2 = o2.getLocation(CACHE_LOCATION2);
                if (!NPC_LOCATION.getWorld().equals(l1.getWorld()) || !NPC_LOCATION.getWorld().equals(l2.getWorld())) {
                    return -1;
                }
                return Double.compare(l1.distanceSquared(NPC_LOCATION), l2.distanceSquared(NPC_LOCATION));
            });

            lookingAt = nearby.get(0);
        }
    }

    private boolean hasInvalidTarget() {
        if (lookingAt == null)
            return true;
        if (!lookingAt.isOnline() || lookingAt.getWorld() != npc.getEntity().getWorld()
                || lookingAt.getLocation(PLAYER_LOCATION).distanceSquared(NPC_LOCATION) > range * range) {
            lookingAt = null;
        }
        return lookingAt == null;
    }

    private boolean isPluginVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Enables/disables the trait
     */
    public void lookClose(boolean lookClose) {
        enabled = lookClose;
    }

    @Override
    public void onDespawn() {
        lookingAt = null;
    }

    private void randomLook() {
        Random rand = new Random();
        float pitch = rand.doubles(randomPitchRange[0], randomPitchRange[1]).iterator().next().floatValue(),
                yaw = rand.doubles(randomYawRange[0], randomYawRange[1]).iterator().next().floatValue();
        NMS.look(npc.getEntity(), yaw, pitch);
    }

    @Override
    public void run() {
        if (!enabled || !npc.isSpawned()) {
            return;
        }
       /* if (npc.getNavigator().isNavigating() && Setting.DISABLE_LOOKCLOSE_WHILE_NAVIGATING.asBoolean()) {
            return;
        }
        */

        // TODO: remove in a later version, defaults weren't saving properly
        if (randomPitchRange == null || randomPitchRange.length != 2) {
            randomPitchRange = new float[] { -10, 0 };
        }
        if (randomYawRange == null || randomYawRange.length != 2) {
            randomYawRange = new float[] { 0, 360 };
        }
        npc.getEntity().getLocation(NPC_LOCATION);
        if (hasInvalidTarget()) {
            findNewTarget();
        }

       if (lookingAt == null && enableRandomLook && t <= 0) {
            randomLook();
            t = randomLookDelay;
        }


        t--;
        if (lookingAt != null && canSeeTarget()) {
            Utils.faceEntity(npc.getEntity(), lookingAt);
        }
    }


    /**
     * Enables random looking - will look at a random {@link Location} every so often if enabled.
     */
    public void setRandomLook(boolean enableRandomLook) {
        this.enableRandomLook = enableRandomLook;
    }

    /**
     * Sets the delay between random looking in ticks
     */
    public void setRandomLookDelay(int delay) {
        this.randomLookDelay = delay;
    }

    public void setRandomLookPitchRange(float min, float max) {
        this.randomPitchRange = new float[] { min, max };
    }

    public void setRandomLookYawRange(float min, float max) {
        this.randomYawRange = new float[] { min, max };
    }

    /**
     * Sets the maximum range in blocks to look at other Entities
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Enables/disables realistic looking (using line of sight checks). More computationally expensive.
     */
    public void setRealisticLooking(boolean realistic) {
        this.realisticLooking = realistic;
    }


    @Override
    public String toString() {
        return "LookClose{" + enabled + "}";
    }

    private static final Location CACHE_LOCATION = new Location(null, 0, 0, 0);
    private static final Location CACHE_LOCATION2 = new Location(null, 0, 0, 0);
    private static final Location NPC_LOCATION = new Location(null, 0, 0, 0);
    private static final Location PLAYER_LOCATION = new Location(null, 0, 0, 0);
}