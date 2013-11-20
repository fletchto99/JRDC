package me.matt.jrdc.client.viewer;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.gui.ScreenPlayer;
import me.matt.jrdc.gui.ViewerGUI;
import me.matt.jrdc.utilities.InetAddrUtility;
import me.matt.jrdc.utilities.Options;

import javax.swing.*;

public class ScreenDisplay extends Thread {

    private boolean viewOnly = true;

    private Viewer viewer;
    private ViewerGUI gui;
    private ScreenPlayer screenPlayer;
    private EventsListener eventsListener;
    private Options viewerOptions;

    /**
     * Create an instance of the screen display
     * 
     * @param viewer
     *            The viewer instance
     */
    public ScreenDisplay(final Viewer viewer) {
        this.viewer = viewer;

        viewerOptions = new Options(InetAddrUtility.getLocalHost(),
                Configuration.getLocalProperties());
        screenPlayer = new ScreenPlayer();
        eventsListener = new EventsListener(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new ViewerGUI(ScreenDisplay.this);
            }
        });
        if (!viewer.isConnected()) {
            if (viewer.connect() == -1) {
                return;
            }
        }
        start();
    }

    @Override
    public void run() {
        /**
         * Continous tasks to run while connected
         * 
         * send and recieve data
         */
        while (viewer.isConnected()) {
            try {
                Thread.sleep(viewerOptions.getRefreshRate());
            } catch (final InterruptedException e) {
                break;
            }
            viewer.updateOptions();
            viewer.setMouseEvents();
            viewer.setKeyEvents();
            viewer.getScreenCapture();
        }
    }

    /**
     * Terminate the screen display
     */
    public void terminate() {
        viewOnly = true;
        if (gui.isFullScreenMode()) {
            gui.changeFullScreenMode();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.dispose();
            }
        });
        eventsListener.removeAdapters();
        if (viewer != null) {
            viewer.disconnect();
        }
        interrupt();
    }

    /**
     * Allow/Remove remote control
     * 
     * @param bool
     *            True to set remote only otherwise false;
     */
    public void setViewOnly(final boolean bool) {
        viewOnly = bool;
        if (viewOnly) {
            eventsListener.removeAdapters();
        } else {
            eventsListener.addAdapters();
        }
    }

    /**
     * Getters
     */

    public boolean isViewOnly() {
        return viewOnly;
    }

    public EventsListener getEventsListener() {
        return eventsListener;
    }

    public Options getViewerOptions() {
        return viewerOptions;
    }

    public ScreenPlayer getScreenPlayer() {
        return screenPlayer;
    }

    public Viewer getViewer() {
        return viewer;
    }
}
