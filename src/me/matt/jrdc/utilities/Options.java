package me.matt.jrdc.utilities;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Hashtable;

import me.matt.jrdc.Configuration;

public class Options implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9157341736166269646L;

    private InetAddress inetAddress;

    private double imageQuality = 1.0;

    private byte colorQuality = Configuration.Constants.cqFull;

    private int refreshRate = 500;

    private final ConnectionProperties properties;

    public Options(final InetAddress inetAddress,
            final Hashtable<String, Object> properties) {
        this.properties = new ConnectionProperties(properties);
        this.inetAddress = inetAddress;
    }

    public byte getColorQuality() {
        return colorQuality;
    }

    public ConnectionProperties getConnectionProperties() {
        return properties;
    }

    public double getImageQuality() {
        return imageQuality;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setColorQuality(final byte quality) {
        colorQuality = quality;
    }

    public void setImageQuality(final double quality) {
        imageQuality = quality;
    }

    public void setInetAddress(final InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setRefreshRate(final int ms) {
        refreshRate = ms;
    }
}