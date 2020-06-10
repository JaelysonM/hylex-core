package com.uzm.hylex.core.api.interfaces;

public class Enums {

  public enum ArenaState {
    IN_WAITING("Aguardando"),
    STARTING("Iniciando"),
    PREPARE("Preparando para iniciar"),
    IN_GAME("Em jogo"),
    END("Fim de partida"),
    FULL("Partida cheia"),
    IDLE("Partida inativa");

    public String name;

    ArenaState(String name) {
      this.name = name;
    }

    public String toString() {
      return this.name;
    }
  }
}
