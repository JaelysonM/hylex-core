package com.uzm.hylex.core.api.interfaces;

public class Enums {

  public enum ArenaState {
    IN_WAITING("Aguardando",false,false),
    STARTING("Iniciando",false,false),
    PREPARE("Preparando para iniciar",false,false),
    IN_GAME("Em jogo",true,true),
    PRE_GAME("Pre-jogo",true,true),
    END("Fim de partida",false,true),
    FULL("Partida cheia",false,true),
    IDLE("Partida inativa",false,true);

    public String name;
    public boolean inGame;
    public boolean locked;


    ArenaState(String name, boolean inGame,boolean locked) {
      this.name = name;
      this.inGame=inGame;
      this.locked=locked;
    }

    public boolean isLocked() {
      return locked;
    }

    public String toString() {
      return this.name;
    }

    public boolean isInGame() {
      return inGame;
    }
  }
}
