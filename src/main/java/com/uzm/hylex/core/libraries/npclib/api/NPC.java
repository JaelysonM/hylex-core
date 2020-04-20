package com.uzm.hylex.core.libraries.npclib.api;

import com.uzm.hylex.core.libraries.npclib.api.metadata.MetadataStore;
import com.uzm.hylex.core.libraries.npclib.trait.NPCTrait;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

/**
 * @author Maxter
 */
public interface NPC {

  boolean spawn(Location location);

  boolean despawn();

  void destroy();

  void update();

  void updateInstant();

  MetadataStore data();

  void addTrait(NPCTrait trait);

  void addTrait(Class<? extends NPCTrait> traitClass);

  void removeTrait(Class<? extends NPCTrait> traitClass);

  boolean isSpawned();

  boolean isProtected();

  <T extends NPCTrait> T getTrait(Class<T> traitClass);

  Entity getEntity();

  Location getCurrentLocation();

  UUID getUUID();

  String getName();

  public static final String PROTECTED_KEY = "protected",
    TAB_LIST_KEY = "hide-from-tablist",
    HIDE_BY_TEAMS_KEY = "hide-by-teams",
    FLYABLE = "flyable",
    GRAVITY = "gravity";
  public static final String AMBIENT_SOUND_METADATA = "ambient-sound";

  public static final String COLLIDABLE_METADATA = "collidable";

  public static final String DAMAGE_OTHERS_METADATA = "damage-others";

  public static final String DEATH_SOUND_METADATA = "death-sound";

  public static final String DEFAULT_PROTECTED_METADATA = "protected";

  public static final String DROPS_ITEMS_METADATA = "drops-items";

  public static final String FLYABLE_METADATA = "flyable";

  public static final String GLOWING_COLOR_METADATA = "glowing-color";

  public static final String GLOWING_METADATA = "glowing";

  public static final String HURT_SOUND_METADATA = "hurt-sound";

  public static final String ITEM_DATA_METADATA = "item-type-data";

  public static final String ITEM_ID_METADATA = "item-type-id";

  public static final String LEASH_PROTECTED_METADATA = "protected-leash";

  public static final String MINECART_ITEM_DATA_METADATA = "minecart-item-data";

  public static final String MINECART_ITEM_METADATA = "minecart-item-name";

  public static final String MINECART_OFFSET_METADATA = "minecart-item-offset";

  public static final String NAMEPLATE_VISIBLE_METADATA = "nameplate-visible";

  public static final String PATHFINDER_OPEN_DOORS_METADATA = "pathfinder-open-doors";


  public static final String PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA = "player-skin-textures";


  public static final String PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA = "player-skin-signature";


  public static final String PLAYER_SKIN_USE_LATEST = "player-skin-use-latest-skin";

  public static final String PLAYER_SKIN_UUID_METADATA = "player-skin-name";

  public static final String RESPAWN_DELAY_METADATA = "respawn-delay";

  public static final String SCOREBOARD_FAKE_TEAM_NAME_METADATA = "fake-scoreboard-team-name";

  public static final String SHOULD_SAVE_METADATA = "should-save";

  public static final String SILENT_METADATA = "silent-sounds";

  public static final String SWIMMING_METADATA = "swim";

  public static final String TARGETABLE_METADATA = "protected-target";

  public static final String PROFILE_NPC_SKIN = "profile-skin";
}
