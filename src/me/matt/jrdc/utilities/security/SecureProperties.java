package me.matt.jrdc.utilities.security;

import me.matt.jrdc.Configuration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Save settings for your program securely. It is recommended you ask your user for a password upon your applications opening and use this as the salt to prevent dictionary attacks.
 * 
 * @author Matt
 * 
 */
public class SecureProperties {

    private final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

    public void load(final File file) throws Exception {
        load(file,
                System.getProperty("user.name")
                        + System.getProperty("user.language"));
    }

    public void load(final File file, final String salt) throws Exception {
        load(file, salt, 5);
    }

    public void load(final File file, final String salt, final int iterations)
            throws Exception {
        final FileInputStream fis = new FileInputStream(file);
        final byte[] bytearray = SecureProperties.read(fis);
        fis.close();
        String toParse = null;
        try {
            toParse = SecurityUtility.decrypt(new String(bytearray, "UTF-8"),
                    salt, iterations);
        } catch (final Exception e) {
            Configuration.displayError(e, "Error");
        }
        if (toParse == null) {
            throw new Exception("Error loading file. File invalid?");
        }
        final String[] split = toParse.split("\\r?\\n");
        for (final String s : split) {
            if (s.startsWith("#") || !s.contains("=")) {
                continue;
            }
            setProperty(s.substring(0, s.indexOf("=")),
                    s.substring(s.indexOf("=") + 1, s.length()));
        }
    }

    /**
     * Read the bytes from an input stream.
     * 
     * @param is
     *            The inputstream to read from.
     * @return The bytes of the open stream.
     */
    private static byte[] read(final InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            final byte[] temp = new byte[4096];
            int read;
            while ((read = is.read(temp)) != -1) {
                buffer.write(temp, 0, read);
            }
        } catch (final IOException ignored) {
            try {
                buffer.close();
            } catch (final IOException ignored2) {
            }
            buffer = null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (final IOException ignored) {
            }
        }
        return buffer == null ? null : buffer.toByteArray();
    }

    public void store(final File file) throws IOException {
        store(file,
                System.getProperty("user.name")
                        + System.getProperty("user.language"));
    }

    public void store(final File file, final String salt) throws IOException {
        store(file, salt, 5);
    }

    public void store(final File file, final String salt, final int iterations)
            throws IOException {
        String toStore = "#"
                + new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss aa")
                        .format(new Date()) + "\r\n";
        for (final String key : map.keySet()) {
            toStore += key + "=" + map.get(key) + "\r\n";
        }
        final FileWriter fw = new FileWriter(file);
        try {
            fw.write(SecurityUtility.encrypt(toStore, salt, iterations));
        } catch (final Exception e) {
            Configuration.displayError(e, "Error");
        }
        fw.flush();
        fw.close();
    }

    public String getProperty(final String key) {
        return map.containsKey(key) ? map.get(key) : null;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(final String key, final String defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void setProperty(final String key, final String value) {
        map.put(key, value);
    }

    /**
     * List the properties
     * 
     * @param out
     *            The output stream to list to
     */
    public void list(final PrintStream out) {
        out.println("--Listing Properties--");
        for (final String key : map.keySet()) {
            out.println(key + "=" + map.get(key));
        }
    }

    /**
     * The map of settings
     * 
     * @return Settings map
     */
    public LinkedHashMap<String, String> getSettingsMap() {
        return map;
    }
}
