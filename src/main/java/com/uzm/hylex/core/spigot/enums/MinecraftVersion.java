package com.uzm.hylex.core.spigot.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum MinecraftVersion {
  V1_8,
  V1_9,
  V1_10,
  V1_11,
  V1_12,
  V1_13,
  V1_14,
  V1_15;
  
  public static MinecraftVersion getVersion(String version) {
    return Arrays.stream(MinecraftVersion.values()).filter(result -> version.toUpperCase().contains(result.toString().toUpperCase())).collect(Collectors.toList()).get(0);
  }

  public boolean isAboveOrEqual(MinecraftVersion compare) {
    return ordinal() >= compare.ordinal();
  }
}
