package me.matt.jrdc.server;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.gui.GUI;
import me.matt.jrdc.server.rmi.RMIServer;
import me.matt.jrdc.utilities.ConnectionProperties;
import me.matt.jrdc.utilities.Options;

public class Server extends Thread {

    /**
     * Connect a viewer to the server
     *
     * @param inetAddress
     *            The inetaddress of the viewer
     * @param properties
     *            The viewers properteies
     * @return The index of the viewer
     */
    public static synchronized int addViewer(final InetAddress inetAddress,
            final Hashtable<String, Object> properties) {
        final int index = Server.viewers.size();
        Server.viewers.put(index, new Options(inetAddress, properties));
        Server.gui.updateList();
        return index;
    }

    /**
     * Kick all of the viewers
     */
    public static synchronized void disconnectAllViewers() {
        final Enumeration<Integer> viewerEnum = Server.viewers.keys();
        while (viewerEnum.hasMoreElements()) {
            Server.removeViewer(viewerEnum.nextElement());
        }
    }

    /**
     * The viewers connected
     *
     * @return The viewers connected
     */
    public static synchronized Hashtable<Integer, InetAddress> getConnectedHosts() {
        final Hashtable<Integer, InetAddress> hosts = new Hashtable<Integer, InetAddress>();

        final Enumeration<Integer> viewerEnum = Server.viewers.keys();
        while (viewerEnum.hasMoreElements()) {
            final int key = viewerEnum.nextElement();
            hosts.put(key, Server.viewers.get(key).getInetAddress());
        }
        return hosts;
    }

    /**
     * Capture the servers screen
     *
     * @param index
     *            The index of the viewer (used for informational purposes)
     * @return The bytes of the screen capture
     */
    public static synchronized byte[] getScreenCapture(final int index) {
        if (!Server.viewers.containsKey(index)) {
            return new byte[0];
        }
        final byte[] screenCapture = Server.robot
                .captureScreenByteArray(Server.viewers.get(index));
        if (Server.viewers.containsKey(index)) {
            Server.getServerConnectionProperties().incReceivedData(
                    screenCapture.length);
            Server.viewers.get(index).getConnectionProperties()
                    .incSentData(screenCapture.length);
        }
        return screenCapture;
    }

    /**
     * Fetch the servers connection properties
     *
     * @return The servers connection properties
     */
    public static synchronized ConnectionProperties getServerConnectionProperties() {
        return Server.properties;
    }

    /**
     * Fetch the viewers connection properties
     *
     * @param index
     *            The viewers index
     * @return The viewers connection properties
     */
    public static synchronized ConnectionProperties getViewerConnectionProperties(
            final int index) {
        return Server.viewers.get(index).getConnectionProperties();
    }

    /**
     * Check if the server is running
     *
     * @return True if the server is running; otherwise false.
     */
    public static synchronized boolean isRunning() {
        return Server.running;
    }

    /**
     * Remove the viewer
     *
     * @param index
     *            The index of the viewer
     */
    public static synchronized void removeViewer(final int index) {
        Server.viewers.remove(index);
        Server.gui.updateList();
    }

    /**
     * Set the keyevents of the server
     *
     * @param events
     *            The key events the server should execute
     */
    public static synchronized void setKeyEvents(
            final ArrayList<KeyEvent> events) {
        Server.robot.setKeyEvents(events);
    }

    /**
     * Set the mouseevents of the server
     *
     * @param events
     *            The mouse events the server should execute
     */
    public static synchronized void setMouseEvents(
            final ArrayList<MouseEvent> events) {
        Server.robot.setMouseEvents(events);
    }

    /**
     * Kill the server
     */
    public static synchronized void terminate() {
        if (Server.running) {
            Server.running = false;
            Server.disconnectAllViewers();
        }
        RMIServer.Stop();
    }

    /**
     *
     * @param index
     *            The viewers index
     * @param options
     *            The options to update
     */
    public static synchronized void updateOptions(final int index,
            final Options options) {
        if (!Server.viewers.containsKey(index)) {
            return;
        }
        final Options opts = Server.viewers.get(index);
        opts.setColorQuality(options.getColorQuality());
        opts.setImageQuality(options.getImageQuality());
        opts.setInetAddress(options.getInetAddress());
        opts.setRefreshRate(options.getRefreshRate());
    }

    // private variable
    private static boolean running = false;

    private static ScreenController robot;

    private static Hashtable<Integer, Options> viewers = new Hashtable<Integer, Options>();

    private static GUI gui;

    private static ConnectionProperties properties;

    /**
     * Start a server based on the users settings
     *
     * @param gui
     *            The gui instance
     */
    public Server(final GUI gui) {
        Server.properties = new ConnectionProperties(
                Configuration.getLocalProperties());
        Server.gui = gui;
        Server.running = false;
        if (!RMIServer.Start(gui)) {
            return;
        }
        Server.running = true;
        try {
            Server.robot = new ScreenController();
        } catch (final AWTException e) {
            Server.terminate();
        }
    }
}