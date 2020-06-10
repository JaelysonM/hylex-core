package com.uzm.hylex.core.skins.shared.utils;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by McLive on 21.07.2019.
 */
public class SkinLogger {
  private java.util.logging.Logger logger;

  public SkinLogger() {
    this.load();
  }

  public boolean load() {
    logger = Logger.getLogger(SkinLogger.class.getName());
    return true;

        /*
        try {
            logger = ReflectionUtil.invokeMethod(Class.forName("org.bukkit.Bukkit"), null, "getLogger");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }*/
  }

  public Object getLogger() {
    return this.logger;
  }

  public void log(String message) {
    this.log(Level.INFO, message);
  }

  public void logAlways(String message) {
    this.logAlways(Level.INFO, message);
  }

  public void log(Level level, String message, Throwable thrown) {
    this.logAlways(level, message, thrown);
  }

  public void log(Level level, String message) {


    this.logAlways(level, message);
  }

  public void logAlways(Level level, String message) {
    this.logger.log(level, "[hylex-core Fork SkinsRestorer] " + message);
  }

  public void logAlways(Level level, String message, Throwable thrown) {
    this.logger.log(level, "[hylex-core Fork SkinsRestorer] " + message, thrown);
  }

}