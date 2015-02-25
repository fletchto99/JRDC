package me.matt.jrdc.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import me.matt.jrdc.Configuration;

public class ImageUtility {

    /**
     * Set default image params
     */
    public static void init() {
        ImageIO.setUseCache(false);
        final Iterator<ImageWriter> writers = ImageIO
                .getImageWritersBySuffix("jpeg");
        ImageUtility.writer = writers.next();
        ImageUtility.param = ImageUtility.writer.getDefaultWriteParam();
        ImageUtility.param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        ImageUtility.param.setCompressionQuality(1);
    }

    /**
     * Reads an image from an input stream
     *
     * @param in
     *            The input stream to read from
     * @return The buffered image
     * @throws IOException
     *             Error reading from null stream
     */
    public static BufferedImage read(final InputStream in) throws IOException {
        final BufferedImage image = ImageIO.read(in);
        if (image == null) {
            throw new IOException("Null");
        }
        return image;
    }

    /**
     * Converts an array of bytes into a buffered image
     *
     * @param bytes
     *            The array of bytes
     * @return The array of bytes as a buffered image
     * @throws IOException
     *             the bytes are null
     */
    public static BufferedImage toBufferedImage(final byte[] bytes)
            throws IOException {
        return ImageUtility.read(new ByteArrayInputStream(bytes));
    }

    /**
     * Converts a buffered image to an array of bytes
     *
     * @param image
     *            The image to convert
     * @param quality
     *            The quality of the image
     * @return The image as an array of bytes.
     */
    public static byte[] toByteArray(final BufferedImage image,
            final double quality) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageUtility.write(image, (float) quality, out); // write with compression
            return out.toByteArray();
        } catch (final IOException e) {
            Configuration.displayError(e, "Error");
            return new byte[] {};
        }
    }

    /**
     * Writes an image to a stream
     *
     * @param image
     *            The image to write
     * @param quality
     *            The quality to write with
     * @param out
     *            The stream to write to
     * @throws IOException
     *             Error writing to the stream
     */
    public static void write(final BufferedImage image, final float quality,
            final OutputStream out) throws IOException {
        final ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        ImageUtility.writer.setOutput(ios);
        ImageUtility.param.setCompressionQuality(quality);
        ImageUtility.writer.write(null, new IIOImage(image, null, null),
                ImageUtility.param);
        ios.close();
    }

    private static ImageWriter writer = null;

    private static ImageWriteParam param = null;
}