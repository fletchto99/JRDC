package me.matt.jrdc.client.viewer;

import me.matt.jrdc.gui.ScreenPlayer;

import java.awt.event.*;
import java.util.ArrayList;

public class EventsListener {

    private final ScreenPlayer player;

    private final KeyAdapter keyAdapter;
    private final MouseAdapter mouseAdapter;
    private final MouseWheelListener mouseWheelListener;
    private final MouseMotionAdapter mouseMotionAdapter;

    private ArrayList<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
    private ArrayList<MouseEvent> mouseEvents = new ArrayList<MouseEvent>();

    /**
     * Add the adapters to listen for events
     * 
     * @param recorder
     *            The parent screen display
     */
    public EventsListener(final ScreenDisplay display) {
        player = display.getScreenPlayer();

        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                keyEvents.add(e);
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                keyEvents.add(e);
            }
        };

        mouseWheelListener = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                mouseEvents.add(e);
            }
        };

        mouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                mouseEvents.add(e);
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                mouseEvents.add(e);
            }
        };

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                mouseEvents.add(e);
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                mouseEvents.add(e);
            }
        };
    }

    public ArrayList<MouseEvent> getMouseEvents() {
        ArrayList<MouseEvent> events = new ArrayList<MouseEvent>();

        synchronized (mouseEvents) {
            events = mouseEvents;
            mouseEvents = new ArrayList<MouseEvent>();
        }

        return events;
    }

    public ArrayList<KeyEvent> getKeyEvents() {
        ArrayList<KeyEvent> events = new ArrayList<KeyEvent>();

        synchronized (keyEvents) {
            events = keyEvents;
            keyEvents = new ArrayList<KeyEvent>();
        }

        return events;
    }

    /**
     * Add the adapters to listen for events
     */
    public void addAdapters() {
        player.addKeyListener(keyAdapter);
        player.addMouseWheelListener(mouseWheelListener);
        player.addMouseMotionListener(mouseMotionAdapter);
        player.addMouseListener(mouseAdapter);
    }

    /**
     * remove the adapters that listen for events
     */
    public void removeAdapters() {
        player.removeKeyListener(keyAdapter);
        player.removeMouseWheelListener(mouseWheelListener);
        player.removeMouseMotionListener(mouseMotionAdapter);
        player.removeMouseListener(mouseAdapter);
    }

}
