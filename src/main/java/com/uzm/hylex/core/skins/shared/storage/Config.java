package com.uzm.hylex.core.skins.shared.storage;

import com.google.common.collect.Lists;

import java.util.List;

public class Config {

  public static boolean DEFAULT_SKINS_ENABLED = false;
  public static boolean DEFAULT_SKINS_PREMIUM = false;
  public static List<String> DEFAULT_SKINS = Lists.newArrayList();
  public static int SKIN_EXPIRES_AFTER = 20;
  public static boolean USE_MYSQL = true;
  public static String MYSQL_HOST = "host";
  public static String MYSQL_PORT = "3306";
  public static String MYSQL_DATABASE = "db";
  public static String MYSQL_USERNAME = "user";
  public static String MYSQL_PASSWORD = "pass";
  public static String MYSQL_SKINTABLE = "skins";
  public static String MYSQL_PLAYERTABLE = "playerSkins";
  public static boolean DISABLE_ONJOIN_SKINS = true; // hidden
  public static boolean DISABLE_ONJOIN_SKINS_BUNGEE = false;// hidden
  public static boolean NO_SKIN_IF_LOGIN_CANCELED = true;
  public static boolean DEBUG = false;
}

