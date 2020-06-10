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

public interface ISkinFactory {

  void applySkin(Player p, Object props);

  void removeSkin(Player p);

  void updateSkin(Player p);




}

