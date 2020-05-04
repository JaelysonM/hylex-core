package com.uzm.hylex.core.api.container;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BedWarsStatisticsContainer extends AbstractContainer {

  private JSONObject object;

  public BedWarsStatisticsContainer(DataContainer dataContainer) {
    super(dataContainer);
    this.object = this.dataContainer.getAsJsonObject();
  }

  @Override
  public void gc() {
    super.gc();
    this.object.clear();
    this.object = null;
  }

  public void addLong(String stats, String sub) {
    this.addLong(stats, sub, 1L);
  }

  public void addLong(String stats, String sub, long amount) {
    ((JSONObject) this.object.get(stats)).put(sub, getLong(stats, sub) + amount);
    this.dataContainer.set(this.object.toString());
  }

  public void removeLong(String stats, String sub) {
    this.removeLong(stats, sub, 1L);
  }

  public void removeLong(String stats, String sub, long amount) {
    ((JSONObject) this.object.get(stats)).put(sub, getLong(stats, sub) - amount);
    this.dataContainer.set(this.object.toString());
  }

  public long getLong(String stats, String sub) {
    return (long) ((JSONObject) this.object.get(stats)).get(sub);
  }
}
