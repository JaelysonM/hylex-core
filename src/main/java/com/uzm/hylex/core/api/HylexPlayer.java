package com.uzm.hylex.core.api;

import com.google.common.collect.ImmutableList;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.api.container.*;
import com.uzm.hylex.core.api.events.HylexPlayerLoadEvent;
import com.uzm.hylex.core.api.interfaces.IArena;
import com.uzm.hylex.core.api.interfaces.IArenaPlayer;
import com.uzm.hylex.core.controllers.TagController;
import com.uzm.hylex.core.utils.HylexMethods;
import com.uzm.hylex.services.lan.WebSocket;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class HylexPlayer {

  private String name;
  private Player player;
  private Group group;
  private Map<String, Map<String, DataContainer>> schemas;

  private Map<Player, Long> lastHit;

  private IArenaPlayer arenaPlayer;
  private boolean accountLoaded;

  private Object auxiler;
  private IArena abstractArena;
  private Location[] temporaryLocation = new Location[2];

  public HylexPlayer(Player player) {
    this.player = player;
    this.group = Group.getPlayerGroup(player);
    this.schemas = new HashMap<>();
    this.lastHit = new HashMap<>();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setArenaPlayer(IArenaPlayer arenaPlayer) {
    if (this.arenaPlayer != null) {
      this.arenaPlayer.destroy();
    }
    this.arenaPlayer = arenaPlayer;
  }

  public void loadAccount() {
    this.accountLoaded = true;
    Bukkit.getPluginManager().callEvent(new HylexPlayerLoadEvent(this));
  }

  public void computeData(String schema, JSONObject data) {
    Map<String, DataContainer> containerMap = new LinkedHashMap<>();
    data.forEach((key, value) -> {
      containerMap.put(key.toString(), new DataContainer(value.toString()));
    });
    this.schemas.put(schema, containerMap);
  }

  public void requestLoad(String... schemas) {
    JSONObject json = new JSONObject();
    JSONObject paramsArray = new JSONObject();
    paramsArray.put("schemasToRequire", schemas);
    paramsArray.put("clientName", "core-" + Core.SOCKET_NAME);
    json.put("parameters", paramsArray);

    JSONObject defaultBodyArray = new JSONObject();
    defaultBodyArray.put("uuid", this.player.getUniqueId());
    defaultBodyArray.put("name", this.player.getName());
    json.put("defaultBody", defaultBodyArray);

    WebSocket.get("core-" + Core.SOCKET_NAME).getSocket().emit("data-require", json);
  }

  public void save() {
    JSONObject json = new JSONObject();
    json.put("uuid", this.player.getUniqueId());
    JSONArray schemas = new JSONArray();
    this.schemas.forEach((key, value) -> {
      JSONObject object = new JSONObject();
      JSONObject data = new JSONObject();
      value.forEach((key2, value2) -> {
        Object o = value2.get();
        if (o instanceof String) {
          if (((String) o).startsWith("{")) {
            o = value2.getAsJsonObject();
          } else if ((((String) o).startsWith("["))) {
            o = value2.getAsJsonArray();
          }
        }

        data.put(key2, o);
      });

      object.put("schemaName", key);
      object.put("data", data);
      schemas.add(object);
    });
    json.put("schemas", schemas);

    WebSocket.get("core-" + Core.SOCKET_NAME).getSocket().emit("data-save", json);
  }

  public void destroy() {
    this.name = null;
    this.player = null;
    this.group = null;
    this.schemas.values().forEach(map -> map.values().forEach(DataContainer::gc));
    this.schemas.clear();
    this.schemas = null;
    this.lastHit.clear();
    this.lastHit = null;
    if (this.arenaPlayer != null) {
      this.arenaPlayer.destroy();
    }
    this.arenaPlayer = null;
    this.auxiler = null;
    this.abstractArena = null;
    this.temporaryLocation = null;
  }

  public void setAbstractArena(IArena abstractArena) {
    this.abstractArena = abstractArena;
  }

  public IArena getAbstractArena() {
    return this.abstractArena;
  }

  public void setLastHit(Player hitter) {
    this.setLastHit(hitter, 8);
  }

  public void setLastHit(Player hitter, long seconds) {
    this.lastHit.put(hitter, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
  }

  public void setAuxiler(Object auxiler) {
    this.auxiler = auxiler;
  }

  public void setTemporaryLocation(Location[] temporaryLocation) {
    this.temporaryLocation = temporaryLocation;
  }

  public void broadcastAction(HylexMethods.StaffAction action, String actionName, String aliase) {
    for (UUID uuid : STAFF) {
      Player staff = Bukkit.getPlayer(uuid);
      if (staff != null && staff != this.player) {
        staff.sendMessage(action.getMessage().replace("%player%", this.player.getName()).replace("%loc%", aliase).replace("%actionname%", actionName));
      }
    }
  }

  public void setupPlayer() {
    this.player.setLevel(0);
    this.player.setExp(0.0F);
    this.player.setExhaustion(0.0F);
    this.player.setHealth(20.0);
    this.player.setFoodLevel(20);
    this.player.setGameMode(GameMode.ADVENTURE);
    this.player.getActivePotionEffects().forEach(effect -> this.player.removePotionEffect(effect.getType()));

    this.player.closeInventory();
    this.player.getInventory().clear();
    this.player.getInventory().setArmorContents(new ItemStack[4]);

    if (this.player.hasPermission("hylex.fly")) {
      this.player.setAllowFlight(true);
      this.player.setFlying(true);
    }

    this.player.updateInventory();

    TagController controller = TagController.create(this.player);
    controller.setOrder(this.group.getOrder());
    controller.setPrefix(this.group.getDisplay());
    controller.update();

    this.player.setDisplayName(this.group.getDisplay() + this.player.getName());
  }

  public DataContainer getDataContainer(String schema, String key) {
    return this.schemas.get(schema).get(key);
  }

  public <T extends AbstractContainer> T getAbstractContainer(String schema, String key, Class<T> containerClass) {
    return this.getDataContainer(schema, key).getContainer(containerClass);
  }

  public BedWarsPreferencesContainer getBedWarsPreferences() {
    return this.getAbstractContainer("BedWarsData", "preferences", BedWarsPreferencesContainer.class);
  }

  public BedWarsStatisticsContainer getBedWarsStatistics() {
    return this.getAbstractContainer("BedWarsData", "statistics", BedWarsStatisticsContainer.class);
  }

  public LobbiesContainer getLobbiesContainer() {
    return this.getAbstractContainer("Global_Profile", "lobbys", LobbiesContainer.class);
  }

  public String getName() {
    return this.name;
  }

  public Player getPlayer() {
    return this.player;
  }

  public Group getGroup() {
    return this.group;
  }

  public IArenaPlayer getArenaPlayer() {
    return this.arenaPlayer;
  }

  public boolean isAccountLoaded() {
    return this.accountLoaded;
  }

  public Object getAuxiler() {
    return this.auxiler;
  }

  public Location[] getTemporaryLocation() {
    return this.temporaryLocation;
  }

  public List<HylexPlayer> getLastHitters() {
    List<HylexPlayer> hitters = this.lastHit.entrySet().stream().filter(entry -> getByPlayer(entry.getKey()) != null).sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
      .map(entry -> getByPlayer(entry.getKey())).collect(Collectors.toList());
    this.lastHit.clear();
    return hitters;
  }

  public static final List<UUID> STAFF = new ArrayList<>();
  private static final Map<UUID, HylexPlayer> PLAYERS = new HashMap<>();

  public static HylexPlayer create(Player player) {
    PLAYERS.computeIfAbsent(player.getUniqueId(), list -> new HylexPlayer(player));
    if (!STAFF.contains(player.getUniqueId()) && player.hasPermission("hylex.staff")) {
      STAFF.add(player.getUniqueId());
    }
    return getByPlayer(player);
  }

  public static HylexPlayer remove(Player player) {
    STAFF.remove(player.getUniqueId());
    return PLAYERS.remove(player.getUniqueId());
  }

  public static HylexPlayer getByUUID(UUID uuid) {
    return PLAYERS.getOrDefault(uuid, null);
  }

  public static HylexPlayer getByPlayer(Player player) {
    return getByUUID(player.getUniqueId());
  }

  public static Collection<HylexPlayer> listPlayers() {
    return ImmutableList.copyOf(PLAYERS.values());
  }
}
