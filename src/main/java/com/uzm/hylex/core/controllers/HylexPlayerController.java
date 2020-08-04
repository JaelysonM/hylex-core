package com.uzm.hylex.core.controllers;

import com.uzm.hylex.core.api.HylexPlayer;

public class HylexPlayerController {
  public static int getLevel(HylexPlayer hp) {
    int total = (int) ((hp.getBedWarsStatistics().getLong("exp", "global") / 5000) + 1);
    return total <= 0 ? 1 : total;
  }

  public static long getTotalExp(HylexPlayer hp) {
    return hp.getBedWarsStatistics().getLong("exp", "global");
  }

  public static long getExp(HylexPlayer hp) {
    return hp.getBedWarsStatistics().getLong("exp", "global") % 5000;
  }
}
