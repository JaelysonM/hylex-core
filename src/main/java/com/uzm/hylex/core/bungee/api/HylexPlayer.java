package com.uzm.hylex.core.bungee.api;

import com.google.common.collect.ImmutableList;
import com.uzm.hylex.core.api.container.*;
import com.uzm.hylex.services.lan.WebSocket;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class HylexPlayer {

  private String name;
  private ProxiedPlayer player;
  private com.uzm.hylex.core.bungee.api.Group group;
  private Map<String, Map<String, DataContainer>> schemas;

  private Map<ProxiedPlayer, Long> lastTell;

  private boolean accountLoaded;

  public HylexPlayer(ProxiedPlayer player) {
    this.player = player;
    this.group = com.uzm.hylex.core.bungee.api.Group.getPlayerGroup(player);
    this.schemas = new HashMap<>();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void loadAccount() {
    this.accountLoaded = true;
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
    paramsArray.put("clientName", "core-bungee");
    json.put("parameters", paramsArray);

    JSONObject defaultBodyArray = new JSONObject();
    defaultBodyArray.put("uuid", this.player.getUniqueId());
    defaultBodyArray.put("name", this.player.getName());
    json.put("defaultBody", defaultBodyArray);

    WebSocket.get("core-bungee").getSocket().emit("data-require", json);
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

    WebSocket.get("core-bungee").getSocket().emit("data-save", json);
  }

  public void destroy() {
    this.name = null;
    this.player = null;
    this.group = null;
    this.schemas.values().forEach(map -> map.values().forEach(DataContainer::gc));
    this.schemas.clear();
    this.schemas = null;
  }

  public List<HylexPlayer> getLastMessager() {
    List<HylexPlayer> tellers = this.lastTell.entrySet().stream().filter(entry -> getByPlayer(entry.getKey()) !=null).sorted((a,b) -> Long.compare(b.getValue(), a.getValue())).map(entry-> getByPlayer(entry.getKey())).collect(
      Collectors.toList());
    this.lastTell.clear();
    return tellers;
  }

  public void setLastMessager(ProxiedPlayer hitter) {
    this.setLastMessager(hitter, 8);
  }

  public void setLastMessager(ProxiedPlayer hitter, long seconds) {
    this.lastTell.put(hitter, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
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

  public ProxiedPlayer getPlayer() {
    return this.player;
  }

  public Group getGroup() {
    return this.group;
  }

  public boolean isAccountLoaded() {
    return this.accountLoaded;
  }



  public static final List<UUID> STAFF = new ArrayList<>();
  private static final Map<UUID, HylexPlayer> PLAYERS = new HashMap<>();

  public static HylexPlayer create(ProxiedPlayer player) {
    PLAYERS.computeIfAbsent(player.getUniqueId(), list -> new HylexPlayer(player));
    if (!STAFF.contains(player.getUniqueId()) && player.hasPermission("hylex.staff")) {
      STAFF.add(player.getUniqueId());
    }
    return getByPlayer(player);
  }

  public static HylexPlayer remove(ProxiedPlayer player) {
    STAFF.remove(player.getUniqueId());
    return PLAYERS.remove(player.getUniqueId());
  }

  public static HylexPlayer getByUUID(UUID uuid) {
    return PLAYERS.getOrDefault(uuid, null);
  }

  public static HylexPlayer getByPlayer(ProxiedPlayer player) {
    return getByUUID(player.getUniqueId());
  }

  public static Collection<HylexPlayer> listPlayers() {
    return ImmutableList.copyOf(PLAYERS.values());
  }
}
