package me.matt.jrdc.utilities;

public class FileUtility {

    /**
     * Convert file sizes to proper sizes
     *
     * @param size
     *            The size to convert
     * @param measureUnits
     *            The units to convert to
     * @return The converted string
     */
    public static String getSizeHumanFormat(final long size,
            final String[] measureUnits) {
        final int measureQuantity = 1024;
        if (size <= 0) {
            return null;
        }
        if (size < measureQuantity) {
            return size + measureUnits[0];
        }
        int i = 1;
        double d = size;
        while ((d = d / measureQuantity) > measureQuantity - 1) {
            i++;
        }
        final long l = (long) (d * 100);
        d = (double) l / 100;
        if (i < measureUnits.length) {
            return d + measureUnits[i];
        }
        return String.valueOf(size);
    }

    /**
     * File sizes
     */
    public static final String[] BYTES = { " B", " kB", " MB", " GB", " TB",
            " PB", " EB", " ZB", " YB" };

    /**
     * Transfer speeds
     */
    public static final String[] BYTES_PER_SECOND = { " B/s", " kB/s", " MB/s",
            " GB/s", " TB/s", " PB/s", " EB/s", " ZB/s", " YB/s" };

}