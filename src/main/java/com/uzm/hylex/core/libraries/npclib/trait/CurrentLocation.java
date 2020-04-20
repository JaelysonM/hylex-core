package com.uzm.hylex.core.libraries.npclib.trait;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.Location;

/**
 * @author Maxter
 */
public class CurrentLocation extends NPCTrait {

  private Location location = new Location(null, 0, 0, 0);

  public CurrentLocation(NPC npc) {
    super(npc);


  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return location;
  }
}
