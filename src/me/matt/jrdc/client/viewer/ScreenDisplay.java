package me.matt.jrdc.client.viewer;

import javax.swing.SwingUtilities;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.gui.ScreenPlayer;
import me.matt.jrdc.gui.ViewerGUI;
import me.matt.jrdc.utilities.InetAddrUtility;
import me.matt.jrdc.utilities.Options;

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
        SwingUtilities
                .invokeLater(() -> gui = new ViewerGUI(ScreenDisplay.this));
        if (!viewer.isConnected()) {
            if (viewer.connect() == -1) {
                return;
            }
        }
        this.start();
    }

    public EventsListener getEventsListener() {
        return eventsListener;
    }

    public ScreenPlayer getScreenPlayer() {
        return screenPlayer;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public Options getViewerOptions() {
        return viewerOptions;
    }

    /**
     * Getters
     */

    public boolean isViewOnly() {
        return viewOnly;
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
     * Terminate the screen display
     */
    public void terminate() {
        viewOnly = true;
        if (gui.isFullScreenMode()) {
            gui.changeFullScreenMode();
        }
        SwingUtilities.invokeLater(() -> gui.dispose());
        eventsListener.removeAdapters();
        if (viewer != null) {
            viewer.disconnect();
        }
        this.interrupt();
    }
}
