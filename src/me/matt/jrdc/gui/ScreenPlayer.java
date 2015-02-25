package me.matt.jrdc.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import me.matt.jrdc.utilities.ImageUtility;

public class ScreenPlayer extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6629820882800698921L;

    private BufferedImage screenImage = null;

    public ScreenPlayer() {
        this.setFocusable(true);
    }

    @Override
    public void paint(final Graphics g) {
        if (screenImage != null) {// draw the image
            g.drawImage(screenImage, 0, 0, screenImage.getWidth(),
                    screenImage.getHeight(), this);
        }
    }

    /**
     * Update the screen with the new image
     *
     * @param data
     *            The bytes of the image
     * @throws IOException
     *             Null image
     */
    public void UpdateScreen(final byte[] data) throws IOException {
        screenImage = ImageUtility.toBufferedImage(data);
        final Dimension dimension = new Dimension(screenImage.getWidth(),
                screenImage.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.repaint();
    }

}