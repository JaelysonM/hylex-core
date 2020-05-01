package com.uzm.hylex.core.libraries.npclib.npc.skin;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uzm.hylex.core.libraries.profile.InvalidMojangException;
import com.uzm.hylex.core.libraries.profile.Mojang;
import com.uzm.hylex.core.nms.NMS;
import org.bukkit.entity.Player;

import static com.uzm.hylex.core.Core.getLoader;

/**
 * @author Maxter
 */
public class Skin {

  private String value, signature;

  private Skin(String value, String signature) {
    this.value = value;
    this.signature = signature;
  }

  public String getValue() {
    return value;
  }
  public String getSignature() {
    return signature;
  }


  private Skin fetch(String name) {
    try {
      String id = Mojang.getUUID(name);
      if (id != null) {
        // premium valid username
        String property = Mojang.getSkinProperty(id);
        if (property != null) {
          // valid skin property
          this.value = property.split(" : ")[1];
          this.signature = property.split(" : ")[2];
        }
      }
    } catch (InvalidMojangException e) {
      System.out.println("Cannot fetch skin from name " + name + ": " + e.getMessage());
    }

    return this;
  }

  public void apply(SkinnableEntity entity) {
    NMS.setValueAndSignature(entity.getEntity(), value, signature);
  }

  public static Skin fromName(String name) {
    return new Skin(null, null).fetch(name);
  }


  public static Skin fromPlayer(Player player) {
    String[] textures = NMS.getPlayerTextures(player);
    return new Skin(textures[0], textures[1]);
  }

  public static Skin fromData(String value, String signature) {
    return new Skin(value, signature);
  }
}
