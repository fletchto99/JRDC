package me.matt.jrdc;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import me.matt.jrdc.utilities.InetAddrUtility;

public class Configuration {

    /**
     * All of the programs constants
     *
     * @author Matt
     *
     */
    public static final class Constants {
        public static final String defaultServerAddress = "127.0.0.1";
        public static final int defaultServerPort = 1099;
        public static final String defaultUsername = "admin";
        public static final String defaultPassword = "admin";

        public static final byte cqFull = BufferedImage.TYPE_INT_ARGB;
        public static final byte cq16Bit = BufferedImage.TYPE_USHORT_555_RGB;
        public static final byte cq256 = BufferedImage.TYPE_BYTE_INDEXED;
        public static final byte cqGray = BufferedImage.TYPE_BYTE_GRAY;
    }

    public static class Paths {
        /**
         * Windows AppData directory.
         *
         * @return The app data directory.
         */
        public static String getAppDir() {
            return System.getenv("APPDATA") + File.separator;
        }

        /**
         * The home directory for all of the programs files.
         *
         * @return The home directory.
         */
        public static String getHomeDirectory() {
            return Paths.getAppDir() + "JRDC";
        }
    }

    /**
     * Display an error in a popup box
     *
     * @param error
     *            The error to display
     * @param title
     *            The box title
     */
    public static void displayError(final String error, final String title) {
        if (error != null) {
            final JTextArea textArea = new JTextArea(error);
            final JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 200));
            JOptionPane.showMessageDialog(null, scrollPane, title,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display an error in a popup box
     *
     * @param error
     *            The error to display
     * @param title
     *            The box title
     */
    public static void displayError(final Throwable error, final String title) {
        if (error != null) {
            final Writer exception = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(exception);
            error.printStackTrace(printWriter);
            error.printStackTrace();
            Configuration.displayError(
                    "A fatal error has occured! Please report this to the dev:\n\n"
                            + exception.toString(), title);
        }
    }

    /**
     * Fetch the local systems properties
     *
     * @return The systems local properties.
     */
    public static Hashtable<String, Object> getLocalProperties() {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Hashtable<String, Object> localProperties = new Hashtable<String, Object>();
        localProperties.put("host-address", InetAddrUtility.getLocalHost()
                .toString());
        localProperties.put(
                "os",
                System.getProperty("os.name") + ", "
                        + System.getProperty("os.arch") + ", "
                        + System.getProperty("os.version"));
        localProperties.put("user.name", System.getProperty("user.name"));
        localProperties.put("screen.size", tk.getScreenSize());
        return localProperties;
    }

    /**
     * The programs main server address
     */
    public static String server_address = Configuration.Constants.defaultServerAddress;
}