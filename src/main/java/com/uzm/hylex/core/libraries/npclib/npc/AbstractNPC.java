package com.uzm.hylex.core.libraries.npclib.npc;

import com.google.common.base.Preconditions;
import com.uzm.hylex.core.java.util.MathUtils;
import com.uzm.hylex.core.libraries.npclib.NPCLibrary;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.libraries.npclib.api.event.NPCDespawnEvent;
import com.uzm.hylex.core.libraries.npclib.api.event.NPCNeedsRespawnEvent;
import com.uzm.hylex.core.libraries.npclib.api.event.NPCSpawnEvent;
import com.uzm.hylex.core.libraries.npclib.api.metadata.MetadataStore;
import com.uzm.hylex.core.libraries.npclib.api.metadata.SimpleMetadataStore;
import com.uzm.hylex.core.libraries.npclib.api.npc.EntityController;
import com.uzm.hylex.core.libraries.npclib.npc.skin.SkinnableEntity;
import com.uzm.hylex.core.libraries.npclib.trait.CurrentLocation;
import com.uzm.hylex.core.libraries.npclib.trait.NPCTrait;
import com.uzm.hylex.core.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Maxter
 */
public class AbstractNPC implements NPC {

  private UUID uuid;
  private String name;
  private EntityController controller;

  private MetadataStore data;
  private Map<Class<? extends NPCTrait>, NPCTrait> traits;

  public boolean teamRegistred;

  public AbstractNPC(UUID uuid, String name, EntityController controller) {
    this.uuid = uuid;
    this.name = name;
    this.controller = controller;

    this.data = new SimpleMetadataStore();
    this.traits = new HashMap<>();
    addTrait(CurrentLocation.class);
  }

  @Override
  public boolean spawn(Location location) {
    Preconditions.checkNotNull(location, "A localizacao nao pode ser null!");
    Preconditions.checkState(!isSpawned(), "O npc ja esta spawnado!");

    getTrait(CurrentLocation.class).setLocation(location);
    controller.spawn(location, this);


    getEntity().setMetadata("NPC", new FixedMetadataValue(NPCLibrary.getPlugin(), this));


   boolean couldSpawn = !MathUtils.isLoaded(location) ? false : NMS.addEntityToWorld(getEntity(), SpawnReason.DEFAULT);
    System.out.println(couldSpawn);

    if (couldSpawn) {
      SkinnableEntity entity = NMS.getSkinnable(getEntity());
      if (entity != null) {
        entity.getSkinTracker().onSpawnNPC();
      }

    } else {
      Bukkit.getPluginManager().callEvent(new NPCNeedsRespawnEvent(this));
      controller.remove();
      return false;
    }
    getEntity().teleport(location);

    NMS.setHeadYaw(getEntity(), location.getYaw());
    NMS.setBodyYaw(getEntity(), location.getYaw());

    getTrait(CurrentLocation.class).setLocation(getEntity().getLocation());

    NPCSpawnEvent event = new NPCSpawnEvent(this);
    if (event.isCancelled()) {
      controller.remove();
      return false;
    }

    for (NPCTrait trait : traits.values()) {
      trait.onSpawn();
    }

    if (getEntity() instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) getEntity();
      entity.setRemoveWhenFarAway(false);

      if (NMS.getStepHeight(entity) < 1.0f) {
        NMS.setStepHeight(entity, 1.0f);
      }

      if (getEntity() instanceof Player) {
        NMS.replaceTrackerEntry((Player) getEntity());
      }
    }


    return true;
  }

  @Override
  public boolean despawn() {
    Preconditions.checkState(isSpawned(), "O npc nao esta spawnado!");
    NPCDespawnEvent event = new NPCDespawnEvent(this);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return false;
    }

    for (NPCTrait trait : traits.values()) {
      trait.onDespawn();
    }

    this.controller.remove();
    Bukkit.getOnlinePlayers().forEach(player -> {
      Scoreboard sb = player.getScoreboard();
      Team team = sb.getTeam("mNPCS");
      if (team != null) {
        team.removeEntry(this.name);
        if (team.getSize() == 0) {
          team.unregister();
        }
      }
    });
    return true;
  }

  @Override
  public void destroy() {
    if (isSpawned()) {
      despawn();
    }

    Bukkit.getOnlinePlayers().forEach(player -> {
      Scoreboard sb = player.getScoreboard();
      Team team = sb.getTeam("mNPCS");
      if (team != null) {
        team.removeEntry(this.name);
        if (team.getSize() == 0) {
          team.unregister();
        }
      }
    });
    this.uuid = null;
    this.name = null;
    this.controller = null;
    this.traits.clear();
    this.traits = null;
    NPCLibrary.unregister(this);
  }

  @Override
  public MetadataStore data() {
    return data;
  }



  private int ticksToUpdate;

  @Override
  public void update() {
    if (isSpawned()) {

      if (ticksToUpdate++ > 30) {
        ticksToUpdate = 0;

        Entity entity = controller.getBukkitEntity();
        if (entity instanceof Player) {
          for (Player players : Bukkit.getServer().getOnlinePlayers()) {

            if (!NPCLibrary.isNPC(players)) {
              Scoreboard sb = players.getScoreboard();
              Team team = sb.getTeam("mNPCS");
              if (data().get(HIDE_BY_TEAMS_KEY, false)) {
                if (team == null) {
                  team = sb.registerNewTeam("mNPCS");
                  team.setNameTagVisibility(NameTagVisibility.NEVER);
                  team.setPrefix("§8[NPC] ");
                }

                if (!team.hasPlayer((Player) controller.getBukkitEntity())) {
                  team.addPlayer((Player) controller.getBukkitEntity());
                  teamRegistred = true;
                }

              }

              if (team != null && team.getSize() == 0) {
                team.unregister();
              }

            }
          }
        }
      }

    }

  }

  public void updateInstant() {
    if (isSpawned()) {
      Entity entity = controller.getBukkitEntity();
      if (entity instanceof Player) {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
          if (!NPCLibrary.isNPC(players)) {
            Scoreboard sb = players.getScoreboard();
            Team team = sb.getTeam("mNPCS");
            if (data().get(HIDE_BY_TEAMS_KEY, false)) {
              if (team == null) {
                team = sb.registerNewTeam("mNPCS");
                team.setNameTagVisibility(NameTagVisibility.NEVER);
                team.setPrefix("§8[NPC] ");
              }

              if (!team.hasPlayer((Player) controller.getBukkitEntity())) {
                team.addPlayer((Player) controller.getBukkitEntity());
              }

              continue;
            }

            if (team != null && team.getSize() == 0) {
              team.unregister();
              System.out.println("UNREGISTER");
            }
          }
        }
      }

    }
  }

  @Override
  public void addTrait(NPCTrait trait) {
    traits.put(trait.getClass(), trait);
    trait.onAttach();
  }

  @Override
  public void addTrait(Class<? extends NPCTrait> traitClass) {
    try {
      NPCTrait trait = (NPCTrait) traitClass.getDeclaredConstructors()[0].newInstance(this);
      traits.put(traitClass, trait);
      trait.onAttach();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Falha ao adicionar Trait " + traitClass.getName(), e);
    }
  }

  @Override
  public void removeTrait(Class<? extends NPCTrait> traitClass) {
    NPCTrait trait = traits.get(traitClass);
    if (trait != null) {
      trait.onRemove();
      traits.remove(traitClass);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends NPCTrait> T getTrait(Class<T> traitClass) {
    return (T) traits.get(traitClass);
  }

  @Override
  public boolean isSpawned() {
    return controller != null && controller.getBukkitEntity() != null && controller.getBukkitEntity().isValid();
  }

  @Override
  public boolean isProtected() {
    return data().get(PROTECTED_KEY, true);
  }

  @Override
  public Entity getEntity() {
    return controller == null ? null : controller.getBukkitEntity();
  }

  @Override
  public Location getCurrentLocation() {
    return getTrait(CurrentLocation.class).getLocation().getWorld() != null ? getTrait(CurrentLocation.class).getLocation() : isSpawned() ? getEntity().getLocation() : null;
  }

  @Override
  public UUID getUUID() {
    return uuid;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isFlyable() {
    return data().get(NPC.FLYABLE, false);
  }

  public void setName(String name) {
    this.name = name;
    if (!isSpawned())
      return;
    Entity bukkitEntity = getEntity();
    if (bukkitEntity instanceof LivingEntity) {
      ((LivingEntity) bukkitEntity).setCustomName(name);
    }
    if (bukkitEntity.getType() == EntityType.PLAYER) {
      Location old = bukkitEntity.getLocation();
      despawn();
      spawn(old);
    }
  }
}
