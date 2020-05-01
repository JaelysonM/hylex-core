package com.uzm.hylex.core.api.interfaces;

import com.uzm.hylex.core.api.HylexPlayer;

import java.util.List;

public interface IArena {

  void start();

  void setState(Enums.ArenaState state);

  void join(HylexPlayer player);

  void leave(HylexPlayer player);

  String getArenaName();

  Enums.ArenaState getState();

  List<IArenaPlayer> getArenaPlayers();

  List<IArenaPlayer> getPlayingPlayers();
}
