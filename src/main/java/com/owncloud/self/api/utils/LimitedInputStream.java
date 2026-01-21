package com.owncloud.self.api.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends FilterInputStream {

    private long remaining;

    public LimitedInputStream(InputStream in, long limit) {
        super(in);
        this.remaining = limit;
    }

    @Override
    public int read() throws IOException {
        if (remaining <= 0) return -1;
        int result = super.read();
        remaining--;
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (remaining <= 0) return -1;
        len = (int) Math.min(len, remaining);
        int result = super.read(b, off, len);
        if (result > 0) remaining -= result;
        return result;
    }

    public static String longToSizeString(Long bytes) {
        if (bytes == null || bytes < 0) {
            return "0 B";
        }

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }

}
