package com.uzm.hylex.core.controllers;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.uzm.hylex.core.java.util.StringUtils;
import com.uzm.hylex.core.libraries.profile.InvalidMojangException;
import com.uzm.hylex.core.libraries.profile.Mojang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FakeController {

  private static final Map<String, String> fakeNames = new HashMap<>();

  private static final Pattern REAL_PATTERN = Pattern.compile("(?i)fakereal:\\w*");

  public static final List<String> blackListed;

  static {
    blackListed = Lists.newArrayList();
  }


  public static void apply(Player player, String fakeName) {
    //player.kickPlayer(StringUtils.formatColors("§6[HylexMC - Disfarçe]\n\n§cVocê foi expulso para que possamos aplicar seu disfarçe."));
    fakeNames.put(player.getName(), fakeName);
  }

  public static void remove(Player player) {
    //player.kickPlayer(StringUtils.formatColors("§6[HylexMC - Disfarçe]\n\n§cVocê foi expulso para que possamos remover seu disfarçe."));
    fakeNames.remove(player.getName());
  }

  public static String getCurrent(String playerName) {
    return has(playerName) ? getFake(playerName) : playerName;
  }

  public static String getFake(String playerName) {
    return fakeNames.get(playerName);
  }

  public static boolean has(String playerName) {
    return fakeNames.containsKey(playerName);
  }

  public static boolean isAvaliable(String name) {
    return !fakeNames.containsKey(name) && !fakeNames.containsValue(name) && Bukkit.getPlayerExact(name) == null && !blackListed.contains(name);
  }

  public static List<String> listNicked() {
    return new ArrayList<>(fakeNames.keySet());
  }

  public static String replaceNickedPlayers(String original, boolean toFake) {
    String replaced = original;

    for (String name : listNicked()) {
      Matcher matcher = Pattern.compile("(?i)" + (toFake ? name : getFake(name))).matcher(replaced);
      while (matcher.find()) {
        replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(toFake ? getFake(name) : name));
      }
    }

    Matcher matcher = REAL_PATTERN.matcher(replaced);
    while (matcher.find()) {
      replaced = replaced.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(
        fakeNames.entrySet().stream().filter(entry -> entry.getValue().equals(matcher.group().replace("fakereal:", ""))).map(Map.Entry::getKey).findFirst().orElse("")));
    }
    return replaced;
  }

  @Beta
  public static WrappedGameProfile cloneGameProfile(WrappedGameProfile profile) {
    WrappedGameProfile gameProfile = profile.withName(getFake(profile.getName()));

    return gameProfile;
  }
}
