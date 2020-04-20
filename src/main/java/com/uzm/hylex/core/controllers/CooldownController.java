package com.uzm.hylex.core.controllers;

import com.google.common.collect.Maps;
import com.uzm.hylex.core.java.util.TimeMethods;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CooldownController {

    private HashMap<String, Long> cooldowns = Maps.newHashMap();

    public void deleteCooldown(String key) {
        cooldowns.remove(key);
    }

    public void createCooldown(String key, int seconds) {
            cooldowns.computeIfAbsent(key, list -> TimeMethods.generateTimeCurrent(TimeUnit.SECONDS, seconds));
    }

    public long getCooldown(String key) {
        if (cooldowns.containsKey(key)) {
            return cooldowns.get(key);
        } else {
            return 0;
        }
    }

    public boolean isInCooldown(String key) {
        if (cooldowns.containsKey(key)) {
            return cooldowns.get(key) >= System.currentTimeMillis();
        } else {
            return false;
        }
    }
}
