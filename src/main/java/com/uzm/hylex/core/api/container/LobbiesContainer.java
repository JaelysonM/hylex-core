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

  public void toggleTell() {
    ((JSONObject) this.lobbies.get("preferences")).put("tell", !this.canSendTell());
    this.dataContainer.set(this.lobbies.toString());
  }

  public void toggleReport() {
    ((JSONObject) this.lobbies.get("preferences")).put("report", !this.canSendReport());
    this.dataContainer.set(this.lobbies.toString());
  }

  public void toggleParty() {
    ((JSONObject) this.lobbies.get("preferences")).put("party", !this.canSendParty());
    this.dataContainer.set(this.lobbies.toString());
  }

  public void updateLastLogin() {
    ((JSONObject) this.lobbies.get("preferences")).put("last_login", System.currentTimeMillis());
    this.dataContainer.set(this.lobbies.toString());
  }

  public void setPlayersVisible(boolean b) {
    ((JSONObject) this.lobbies.get("preferences")).put("players", b);
    this.dataContainer.set(this.lobbies.toString());
  }

  public long getLastLogin() {
    return (long) ((JSONObject) this.lobbies.get("preferences")).get("last_login");
  }


  public long getFirstLogin() {
    return (long) ((JSONObject) this.lobbies.get("preferences")).get("first_login");
  }

  public void setToggleTell(boolean b) {
    ((JSONObject) this.lobbies.get("preferences")).put("tell", b);
    this.dataContainer.set(this.lobbies.toString());
  }

  public void setToggleReport(boolean b) {
    ((JSONObject) this.lobbies.get("preferences")).put("report", b);
    this.dataContainer.set(this.lobbies.toString());
  }

  public void setToggleParty(boolean b) {
    ((JSONObject) this.lobbies.get("preferences")).put("party", b);
    this.dataContainer.set(this.lobbies.toString());
  }

  public boolean canBuild() {
    return this.canBuild;
  }


  public boolean isPlayersVisible() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("players");
  }

  public boolean canSendTell() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("tell");
  }

  public boolean canSendReport() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("report");
  }

  public boolean canSendParty() {
    return (boolean) ((JSONObject) this.lobbies.get("preferences")).get("party");
  }
}
