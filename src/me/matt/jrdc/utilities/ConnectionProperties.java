package me.matt.jrdc.utilities;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class ConnectionProperties implements Serializable {

    private static final long serialVersionUID = -1622790794445924003L;

    // private variables
    private final long previous = 0;
    private long startedAt = 0;
    private long duration = 0;
    private long dataSize = 0;
    private long sentData = 0;
    private long receivedData = 0;
    private long transferSpeed = 0;
    private final Hashtable<String, Object> properties;

    public ConnectionProperties(final Hashtable<String, Object> properties) {
        this.properties = properties;
        startedAt = System.currentTimeMillis();
        duration = 0;
        dataSize = 0;
        sentData = 0;
        receivedData = 0;
        transferSpeed = 0;
    }

    /**
     * Display the connection properties
     */
    public void display() {
        this.refresh();
        final Dimension size = (Dimension) properties.get("screen.size");
        JOptionPane.showMessageDialog(
                null,
                "Host: \t" + properties.get("host-address") + "\n" +

                "OS: \t" + properties.get("os") + "\n" +

                "User's name: \t" + properties.get("user.name") + "\n" +

                "Screen resolution: \t" + String.valueOf(size.width) + "x"
                        + String.valueOf(size.height) + "\n" + "Duration: \t"
                        + this.getDuration() + "\n\n" + "Sent data: \t"
                        + this.getSize(sentData) + "\n" + "Received data: \t"
                        + this.getSize(receivedData) + "\n\n"
                        + "Total data size: \t" + this.getSize(dataSize) + "\n"
                        + "Transfer speed: \t" + this.getSpeed(),
                "Remote host properties", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * The connection time
     *
     * @return The time connected
     */
    private String getDuration() {
        final long h = duration / 3600000;
        final long m = duration % 3600000 / 60000;
        final long s = duration % 60000 / 1000;
        return h + ":" + m + ":" + s;
    }

    /**
     * Convert to proper size
     *
     * @param size
     *            the size to convert
     * @return The size as a string
     */
    private String getSize(final long size) {
        return FileUtility.getSizeHumanFormat(size, FileUtility.BYTES);
    }

    /**
     * Fetch the file transfer speed
     *
     * @return The speed
     */
    private String getSpeed() {
        return FileUtility.getSizeHumanFormat(transferSpeed,
                FileUtility.BYTES_PER_SECOND);
    }

    public void incReceivedData(final long size) {
        receivedData += size;
    }

    public void incSentData(final long size) {
        sentData += size;
    }

    /**
     * update the information
     */
    private void refresh() {
        duration = previous + System.currentTimeMillis() - startedAt;
        dataSize = sentData + receivedData;
        transferSpeed = dataSize * 1000 / duration;
    }

}
