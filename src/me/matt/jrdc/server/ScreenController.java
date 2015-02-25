package me.matt.jrdc.server;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import me.matt.jrdc.utilities.ImageUtility;
import me.matt.jrdc.utilities.Options;

public class ScreenController {

    private final Robot rt;

    private final Rectangle screenRect;

    /**
     * Create an instance of the robot to control the screen
     *
     * @throws AWTException
     *             Error initilizing the robot
     */
    public ScreenController() throws AWTException {
        rt = new Robot();
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    /**
     * Capture the screen
     *
     * @param viewerOptions
     *            The viewers options
     * @return The screen image
     */
    public BufferedImage captureScreen(final Options viewerOptions) {
        final BufferedImage screen = rt.createScreenCapture(screenRect);

        final BufferedImage bimage = new BufferedImage(screenRect.width,
                screenRect.height, viewerOptions.getColorQuality());
        final Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(screen, 0, 0, screenRect.width, screenRect.height, null);
        g2d.dispose();
        return bimage;
    }

    /**
     * Capture the screen as a bytearray
     *
     * @param viewerOptions
     *            The viewers options
     * @return The byte array of the screens image
     */
    public byte[] captureScreenByteArray(final Options viewerOptions) {
        return ImageUtility.toByteArray(this.captureScreen(viewerOptions),
                viewerOptions.getImageQuality());
    }

    /**
     * execute a key event
     *
     * @param event
     *            The event to execute
     */
    public void setKeyEvent(final KeyEvent event) {
        switch (event.getID()) {
            case KeyEvent.KEY_PRESSED:
                rt.keyPress(event.getKeyCode());
                break;
            case KeyEvent.KEY_RELEASED:
                rt.keyRelease(event.getKeyCode());
                break;
        }
    }

    /**
     * Execute an array of key events
     *
     * @param events
     *            The events to execute
     */
    public void setKeyEvents(final ArrayList<KeyEvent> events) {
        for (int i = 0; i < events.size(); i++) {
            this.setKeyEvent(events.get(i));
        }
    }

    /**
     * Execute a mouse event
     *
     * @param evt
     *            The event to execute
     */
    public void setMouseEvent(final MouseEvent evt) {
        final int x = evt.getX();
        final int y = evt.getY();
        rt.mouseMove(x, y);
        int buttonMask = 0;
        final int buttons = evt.getButton();
        if (buttons == MouseEvent.BUTTON1) {
            buttonMask = InputEvent.BUTTON1_MASK;
        }
        if (buttons == MouseEvent.BUTTON2) {
            buttonMask |= InputEvent.BUTTON2_MASK;
        }
        if (buttons == MouseEvent.BUTTON3) {
            buttonMask |= InputEvent.BUTTON3_MASK;
        }
        switch (evt.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                rt.mousePress(buttonMask);
                break;
            case MouseEvent.MOUSE_RELEASED:
                rt.mouseRelease(buttonMask);
                break;
            case MouseEvent.MOUSE_WHEEL:
                rt.mouseWheel(((MouseWheelEvent) evt).getUnitsToScroll());
                break;
        }
    }

    /**
     * Execute an array of mouse vents
     *
     * @param evts
     *            The events to execute
     */
    public void setMouseEvents(final ArrayList<MouseEvent> evts) {
        for (int i = 0; i < evts.size(); i++) {
            this.setMouseEvent(evts.get(i));
        }
    }
}