package com.uzm.hylex.core.api.container;

import org.json.simple.JSONObject;

import java.util.Map;

@SuppressWarnings({"unchecked"})
public class BedWarsPreferencesContainer extends AbstractContainer {

  private JSONObject preferences;

  public BedWarsPreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);
    this.preferences = this.dataContainer.getAsJsonObject();
  }

  @Override
  public void gc() {
    super.gc();
    this.preferences.clear();
    this.preferences = null;
  }

  public void setQuickBuy(int slot, String id) {
    if (id == null) {
      ((JSONObject) this.preferences.get("quickBuy")).remove(String.valueOf(slot));
    } else {
      ((JSONObject) this.preferences.get("quickBuy")).put(String.valueOf(slot), id);
    }
    this.dataContainer.set(this.preferences.toString());
  }

  public boolean hasQuickBuy(int slot) {
    return ((JSONObject) this.preferences.get("quickBuy")).containsKey(String.valueOf(slot));
  }

  public boolean hasQuickBuy(String item) {
    return ((JSONObject) this.preferences.get("quickBuy")).containsValue(item);
  }

  public int getQuickBuy(String item) {
    Map.Entry<?, ?> entry =
      (Map.Entry<?, ?>) ((JSONObject) this.preferences.get("quickBuy")).entrySet().stream().filter(e -> ((Map.Entry<?, ?>) e).getValue().toString().equals(item)).findFirst()
        .orElse(null);
    if (entry != null) {
      return Integer.parseInt(entry.getKey().toString());
    }

    return -1;
  }

  public String getQuickBuy(int slot) {
    return ((JSONObject) this.preferences.get("quickBuy")).get(String.valueOf(slot)).toString();
  }
}
