package me.matt.jrdc.utilities;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import me.matt.jrdc.Configuration;

public class InetAddrUtility {

    public static void clearDefaultAdr() {
        System.getProperties().remove("java.rmi.server.hostname");
    }

    /**
     * Get the localhost
     *
     * @return localhost
     */
    public static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            Configuration.displayError(e, "Error");
            return null;
        }
    }

    /**
     * Fetch all local IP addresses across all network interfaces
     *
     * @return All local ip addresses for the current machine
     */
    public static String[] getLocalIPAdresses() {
        try {
            InetAddress inetAddress;
            final ArrayList<String> hosts = new ArrayList<String>();
            final Enumeration<NetworkInterface> ifaces = NetworkInterface
                    .getNetworkInterfaces();
            // enumerate through all network interfaces
            while (ifaces.hasMoreElements()) {
                final NetworkInterface iface = ifaces.nextElement();
                final Enumeration<InetAddress> addrs = iface.getInetAddresses();
                // enumerate through all ip addresses
                while (addrs.hasMoreElements()) {
                    inetAddress = addrs.nextElement();
                    // verify and add the address
                    if (inetAddress instanceof Inet4Address) {
                        hosts.add(inetAddress.getHostAddress());
                    }
                    if (inetAddress instanceof Inet6Address) {
                        hosts.add(inetAddress.getHostAddress());
                    }
                }
            }
            return hosts.toArray(new String[hosts.size()]);
        } catch (final Exception e) {
            Configuration.displayError(e, "Error");
            return new String[] { "127.0.0.1" };// error - return localhost only
        }
    }

    // Java RMI properties
    public static void setDefaultAdr(final String address) {
        System.setProperty("java.rmi.server.hostname", address);
    }
}
