package com.uzm.hylex.core.libraries.npclib.npc.skin;

import com.uzm.hylex.core.libraries.npclib.api.NPC;
import org.bukkit.entity.Player;


/**
 * @author Maxter
 */
public interface SkinnableEntity {

  public NPC getNPC();

  public Player getEntity();

  public SkinPacketTracker getSkinTracker();

  public void setSkin(Skin skin);

  public Skin getSkin();

  public void setSkinFlags(byte flags);
}
