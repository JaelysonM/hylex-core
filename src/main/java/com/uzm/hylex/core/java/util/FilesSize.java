package com.uzm.hylex.core.java.util;

import java.io.File;
import java.text.DecimalFormat;

public class FilesSize {
  private static String[] UNITS;

  static {
    UNITS = new String[] {"B", "KB", "MB", "GB", "TB"};
  }

  private String sizeFormat;

  public FilesSize(File file) {
    long length = getFileLength(file);
    int unitIndex = (int) (Math.log10(length) / 3);
    double unitValue = 1 << (unitIndex * 10);
    sizeFormat = new DecimalFormat("#,##0.#").format(length / unitValue) + " " + UNITS[unitIndex];
  }

  public long getFileLength(File directory) {
    long length = 0;
    for (File file : directory.listFiles()) {
      if (file.isFile())
        length += file.length();
      else
        length += getFileLength(file);
    }
    return length;
  }

  public void destroy() {
    sizeFormat = null;
  }

  public String getSizeFormat() {
    String format = this.sizeFormat;
    destroy();
    return sizeFormat;
  }

}
