package com.uzm.hylex.core.api.container;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class LobbiesContainer extends AbstractContainer {

  private JSONObject lobbies;
  private boolean canBuild;

  public LobbiesContainer(DataContainer dataContainer) {
    super(dataContainer);
    this.lobbies = this.dataContainer.getAsJsonObject();
  }

  @Override
  public void gc() {
    super.gc();
    this.lobbies.clear();
    this.lobbies = null;
  }

  public void toggleCanBuild() {
    this.canBuild = !this.canBuild;
  }

  public void togglePlayersVisible() {
    ((JSONObject) this.lobbies.get("preferences")).put("players", !this.isPlayersVisible());
    this.dataContainer.set(this.lobbies.toString());
  }

  public boolean isCanBuild() {
    return this.canBuild;
  }

  public boolean isPlayersVisible() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("players");
  }

  public boolean isCanSendTell() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("tell");
  }
}
