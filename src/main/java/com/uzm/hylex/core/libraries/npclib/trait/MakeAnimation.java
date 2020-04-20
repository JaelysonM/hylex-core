package com.uzm.hylex.core.libraries.npclib.trait;

import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.libraries.npclib.api.NPC;
import com.uzm.hylex.core.nms.NMS;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Maxter
 */
public class MakeAnimation extends NPCTrait {

  private Location location = new Location(null, 0, 0, 0);

  public MakeAnimation(NPC npc) {
    super(npc);
   

  }


  @Override
  public void onSpawn() {
    super.onSpawn();

    new BukkitRunnable() {

      @Override
      public void run() {
        NMS.playAnimation(getNPC().getEntity(), 1);
      }
    }.runTaskLaterAsynchronously(Core.getInstance(), 20*2);




  }

}
