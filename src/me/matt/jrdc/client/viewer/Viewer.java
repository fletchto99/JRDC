package me.matt.jrdc.client.viewer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.client.RMIClient;

public class Viewer {

    public static ArrayList<KeyEvent> getKeyEvents(final int index) {
        if (!Viewer.viewers.containsKey(index)) {
            return new ArrayList<KeyEvent>();
        }
        return Viewer.viewers.get(index).getEventsListener().getKeyEvents();
    }

    public static ArrayList<MouseEvent> getMouseEvents(final int index) {
        if (!Viewer.viewers.containsKey(index)) {
            return new ArrayList<MouseEvent>();
        }
        return Viewer.viewers.get(index).getEventsListener().getMouseEvents();
    }

    public static void setScreenCapture(final byte[] data, final int index) {
        try {
            if (!Viewer.viewers.containsKey(index)) {
                return;
            }
            Viewer.viewers.get(index).getScreenPlayer().UpdateScreen(data);
            Viewer.viewers.get(index).getViewerOptions()
                    .getConnectionProperties().incReceivedData(data.length);
        } catch (final Exception e) {
            return;
        }
    }

    private int index = -1;

    private ScreenDisplay recorder;

    private final RMIClient client;

    private boolean connected = false;

    private static Hashtable<Integer, ScreenDisplay> viewers = new Hashtable<Integer, ScreenDisplay>();

    /**
     * Create an instance of the viewer
     *
     * @param con
     *            The connection information
     */
    public Viewer(final Connection con) {
        client = new RMIClient(con, Configuration.getLocalProperties());
        this.connect();
        if (connected) {
            recorder = new ScreenDisplay(this);
        } else {
            this.Stop();
        }
    }

    /**
     * Connect the viewer to the server
     *
     * @return The index of the connection
     */
    public int connect() {
        connected = false;

        index = client.connect();
        if (index == -1) {
            return -1;
        }
        connected = true;
        return index;
    }

    /*
     *
     * Below are ServerInterface methods used to connect via RMI with the server --Self explanitory--
     */

    /**
     * Disconnect the viewer from the server
     */
    public void disconnect() {
        connected = false;
        client.disconnect();
    }

    public void displayConnectionProperties() {
        try {
            client.getServer().getConnectionProperties().display();
        } catch (final RemoteException e) {
            Configuration.displayError(e, "Error");
        }
    }

    public void getScreenCapture() {
        try {
            recorder.getScreenPlayer().UpdateScreen(
                    client.getServer().getScreenCapture(index));
        } catch (final Exception e) {// unable to recieve data
            recorder.terminate();
            Configuration
                    .displayError(
                            "The connection to the server has been lost. Your remote session is now over.",
                            "Session Over");
        }
    }

    /**
     * Check if we are connected to the server
     *
     * @return True if connected; otherwise false
     */
    public boolean isConnected() {
        return connected;
    }

    public void setKeyEvents() {
        try {
            client.getServer().setKeyEvents(
                    recorder.getEventsListener().getKeyEvents());
        } catch (final RemoteException e) {
            Configuration.displayError(e, "Error");
        }
    }

    public void setMouseEvents() {
        try {
            client.getServer().setMouseEvents(
                    recorder.getEventsListener().getMouseEvents());
        } catch (final RemoteException e) {
            Configuration.displayError(e, "Error");
        }
    }

    /**
     * Stop the viewer and disconnect from the server
     */
    public void Stop() {
        this.disconnect();
    }

    public void updateOptions() {
        try {
            client.getServer()
                    .updateOptions(recorder.getViewerOptions(), index);
        } catch (final Exception e) {// unable to recieve data
            Configuration.displayError(e, "Error");
        }
    }

}