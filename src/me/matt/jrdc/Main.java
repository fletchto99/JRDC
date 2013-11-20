package me.matt.jrdc;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import me.matt.jrdc.Configuration.Paths;
import me.matt.jrdc.gui.ConnectionFrame;
import me.matt.jrdc.gui.GUI;
import me.matt.jrdc.gui.ViewerGUI;
import me.matt.jrdc.server.Server;
import me.matt.jrdc.utilities.ImageUtility;
import me.matt.jrdc.utilities.InetAddrUtility;
import me.matt.jrdc.utilities.Settings;

public class Main {

    // for private use only
    private static GUI main;
    private static Settings settings;

    /**
     * Initilize the application
     * 
     * @param args
     */
    public static void main(final String args[]) {
        ImageUtility.init();
        InetAddrUtility.clearDefaultAdr();
        final File dir = new File(Paths.getHomeDirectory());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Main.settings = new Settings();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                Main.main = new GUI();
                Main.main.setSettings(Main.settings);
            }
        });
    }

    /**
     * Kills the application
     */
    public static void kill() {
        if (JOptionPane.showConfirmDialog(null,
                "Are you sure you wish to exit Java Remote Desktop?",
                "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {// verify the user wishes to exit

            // shutdown the server
            if (Server.isRunning()) {
                Server.terminate();
            }

            // kill all GUI's and save settings
            ViewerGUI.kill();
            Main.main.dispose();
            ConnectionFrame.close();
            Main.settings.saveSettings(Main.main);
            InetAddrUtility.clearDefaultAdr();
            System.exit(0);
        }
    }
}