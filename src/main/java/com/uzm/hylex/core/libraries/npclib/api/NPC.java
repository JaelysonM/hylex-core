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

    boolean teamRegistred = false;

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

    boolean isFlyable();

    public static final String PROTECTED_KEY = "protected",
            TAB_LIST_KEY = "hide-from-tablist",
            HIDE_BY_TEAMS_KEY = "hide-by-teams",
            FLYABLE = "flyable",
            GRAVITY = "gravity";
    static final String AMBIENT_SOUND_METADATA = "ambient-sound";

    static final String COLLIDABLE_METADATA = "collidable";

    static final String DAMAGE_OTHERS_METADATA = "damage-others";

    static final String DEATH_SOUND_METADATA = "death-sound";

    static final String DEFAULT_PROTECTED_METADATA = "protected";

    static final String DROPS_ITEMS_METADATA = "drops-items";

    static final String FLYABLE_METADATA = "flyable";

    static final String GLOWING_COLOR_METADATA = "glowing-color";

    static final String GLOWING_METADATA = "glowing";

    static final String HURT_SOUND_METADATA = "hurt-sound";

    static final String ITEM_DATA_METADATA = "item-type-data";

    static final String ITEM_ID_METADATA = "item-type-id";

    static final String LEASH_PROTECTED_METADATA = "protected-leash";

    static final String MINECART_ITEM_DATA_METADATA = "minecart-item-data";

    static final String MINECART_ITEM_METADATA = "minecart-item-name";

    static final String MINECART_OFFSET_METADATA = "minecart-item-offset";

    static final String NAMEPLATE_VISIBLE_METADATA = "nameplate-visible";

    static final String PATHFINDER_OPEN_DOORS_METADATA = "pathfinder-open-doors";


    static final String PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA = "player-skin-textures";


    static final String PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA = "player-skin-signature";


    static final String PLAYER_SKIN_USE_LATEST = "player-skin-use-latest-skin";

    static final String PLAYER_SKIN_UUID_METADATA = "player-skin-name";

    static final String RESPAWN_DELAY_METADATA = "respawn-delay";

    static final String SCOREBOARD_FAKE_TEAM_NAME_METADATA = "fake-scoreboard-team-name";

    static final String SHOULD_SAVE_METADATA = "should-save";

    static final String SILENT_METADATA = "silent-sounds";

    static final String SWIMMING_METADATA = "swim";

    static final String TARGETABLE_METADATA = "protected-target";

    static final String PROFILE_NPC_SKIN = "profile-skin";

    static final String NPC_NAME = "npc-name";

    static final String SERVER_ITEM = "server-item";
    static final String PUSHABLE = "pushable";
}
