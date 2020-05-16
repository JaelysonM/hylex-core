package com.uzm.hylex.core.utils;

import java.util.regex.Pattern;

public class Utils {
  public static String removeNumbers(String string) {
    return string.replaceAll("[0-9]", "");
  }
  private static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_\\-]+$");
  public static boolean validUsername(String username) {
    if (username.length() > 16)
      return false;

    return namePattern.matcher(username).matches();
  }
}
