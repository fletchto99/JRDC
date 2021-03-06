package me.matt.jrdc.utilities;

import java.io.File;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.gui.GUI;
import me.matt.jrdc.utilities.security.SecureProperties;

public class Settings {

    private final File config;
    private final SecureProperties properties = new SecureProperties();

    private String password = null;
    private String username = null;
    private int port = -1;

    /**
     * load the settings
     */
    public Settings() {
        config = new File(Configuration.Paths.getHomeDirectory(),
                "settings.dat");
        this.loadConfig();
    }

    /**
     * The unencrypted password
     *
     * @return The unencrypted password
     */
    public String getPassword() {
        return password != null ? password
                : Configuration.Constants.defaultPassword;
    }

    /**
     * The unencrypted port
     *
     * @return The unencrypted port
     */
    public int getPort() {
        return port != -1 ? port : Configuration.Constants.defaultServerPort;
    }

    /**
     * The unencrypted username
     *
     * @return The unencrypted username
     */
    public String getUsername() {
        return username != null ? username
                : Configuration.Constants.defaultUsername;
    }

    /**
     * Load the encrypted config file
     */
    private void loadConfig() {
        if (config.canRead()) {
            try {
                // load the settings
                properties.load(config, "jrdcSalt");
                username = properties.getProperty("username");
                password = properties.getProperty("password");
                try {
                    port = Integer.parseInt(properties.getProperty("port"));
                } catch (final Exception e) {
                    port = -1;
                }
            } catch (final Exception e) {
                Configuration
                        .displayError(
                                "There was an error loading your settings! They will be reset.",
                                "Error");
            }
        } else {
            this.saveDefaultSettings();
        }
    }

    /**
     * Save the default username/password combo
     */
    public void saveDefaultSettings() {
        try {
            config.createNewFile();
            properties.setProperty("username", this.getUsername());
            properties.setProperty("password", this.getPassword());
            properties.setProperty("port", String.valueOf(this.getPort()));
            properties.store(config, "jrdcSalt");
        } catch (final Exception e) {
        }
    }

    /**
     * Save the settings within the GUI
     *
     * @param gui
     *            The gui to save the settings for
     */
    public void saveSettings(final GUI gui) {
        try {
            config.createNewFile();
            properties.setProperty("username", gui.getUsername());
            properties.setProperty("password", gui.getPassword());
            properties.setProperty("port", String.valueOf(gui.getPort()));
            properties.store(config, "jrdcSalt");
        } catch (final Exception e) {
            Configuration
                    .displayError(
                            "There was an error saving your settings! They will be reset upon next start.",
                            "Error");
        }
    }
}
