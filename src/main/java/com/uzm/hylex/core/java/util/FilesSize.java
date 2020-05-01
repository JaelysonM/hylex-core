package com.uzm.hylex.core.java.util;

import java.io.File;
import java.text.DecimalFormat;

public class FilesSize {

    String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };

    private File file;
    private String sizeFormat;

    public FilesSize(File file) {
        this.file = file;
        int unitIndex = (int) (Math.log10(file.length()) / 3);
        double unitValue = 1 << (unitIndex * 10);
        sizeFormat= new DecimalFormat("#,##0.#")
                .format(file.length() / unitValue) + " "
                + units[unitIndex];
    }


    public String getSizeFormat() {
        return sizeFormat;
    }

    public File getFile() {
        return file;
    }
}
