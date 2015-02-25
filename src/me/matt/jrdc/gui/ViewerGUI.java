package me.matt.jrdc.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.client.viewer.ScreenDisplay;

public class ViewerGUI extends JFrame {

    public static void kill() {
        if (ViewerGUI.recorder != null) {
            ViewerGUI.recorder.terminate();
        }
    }

    private static final long serialVersionUID = -4815895420008072546L;
    private static ScreenDisplay recorder;

    private boolean fullScreenMode = false;

    private JMenuBar menuBar;

    private JMenu fileMenu;

    private JMenuItem exitButton;

    private JMenu qualityMenu;

    private JMenuItem highestButton;

    private JMenuItem highButton;

    private JMenuItem mediumButton;
    private JMenuItem lowButton;
    private JMenu togglesMenu;
    private JCheckBoxMenuItem fullscreenToggle;
    private JCheckBoxMenuItem controlToggle;
    private JMenu refreshMenu;
    private JMenuItem fastestButton;
    private JMenuItem fastButton;
    private JMenuItem normalButton;
    private JMenuItem slowButton;
    private JMenu helpMenu;
    private JMenuItem sessionButton;
    private JScrollPane scrollPane;

    public ViewerGUI(final ScreenDisplay recorder) {
        ViewerGUI.recorder = recorder;
        this.initComponents();
    }

    public void changeFullScreenMode() {
        final GraphicsDevice device = this.getGraphicsConfiguration()
                .getDevice();
        if (!device.isFullScreenSupported()) {
            return;
        }

        fullScreenMode = !fullScreenMode;

        this.dispose();
        if (fullScreenMode) {
            this.setUndecorated(true);
            device.setFullScreenWindow(this);
            if (!fullscreenToggle.isSelected()) {
                fullscreenToggle.setSelected(true);
            }
        } else {
            if (fullscreenToggle.isSelected()) {
                fullscreenToggle.setSelected(false);
            }
            this.setUndecorated(false);
            device.setFullScreenWindow(null);
        }
        this.setVisible(true);
    }

    private void exit() {
        if (this.isFullScreenMode()) {
            this.changeFullScreenMode();
        }
        if (JOptionPane.showConfirmDialog(null, "Exit Viewer?",
                "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            ViewerGUI.recorder.terminate();
        }
    }

    private void initComponents() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        exitButton = new JMenuItem();
        qualityMenu = new JMenu();
        highestButton = new JMenuItem();
        highButton = new JMenuItem();
        mediumButton = new JMenuItem();
        lowButton = new JMenuItem();
        togglesMenu = new JMenu();
        fullscreenToggle = new JCheckBoxMenuItem();
        controlToggle = new JCheckBoxMenuItem();
        refreshMenu = new JMenu();
        fastestButton = new JMenuItem();
        fastButton = new JMenuItem();
        normalButton = new JMenuItem();
        slowButton = new JMenuItem();
        helpMenu = new JMenu();
        sessionButton = new JMenuItem();
        scrollPane = new JScrollPane();

        final Container contentPane = this.getContentPane();
        contentPane.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        {

            {
                fileMenu.setText("File");

                exitButton.setText("Exit");
                fileMenu.add(exitButton);
            }
            menuBar.add(fileMenu);

            {
                qualityMenu.setText("Quality");
                highestButton.setText("Highest");
                qualityMenu.add(highestButton);
                highButton.setText("High");
                qualityMenu.add(highButton);
                mediumButton.setText("Medium");
                qualityMenu.add(mediumButton);
                lowButton.setText("Low");
                qualityMenu.add(lowButton);
            }
            menuBar.add(qualityMenu);

            {
                refreshMenu.setText("Refresh Rate");

                fastestButton.setText("0 ms");
                refreshMenu.add(fastestButton);

                fastButton.setText("250 ms");
                refreshMenu.add(fastButton);

                normalButton.setText("500 ms");
                refreshMenu.add(normalButton);
                slowButton.setText("1 s");
                refreshMenu.add(slowButton);
            }
            menuBar.add(refreshMenu);

            {
                togglesMenu.setText("Toggles");

                fullscreenToggle.setText("Fullscreen");
                togglesMenu.add(fullscreenToggle);

                controlToggle.setText("Remote Control");
                togglesMenu.add(controlToggle);
            }
            menuBar.add(togglesMenu);

            {
                helpMenu.setText("Help");
                sessionButton.setText("Session Information");
                helpMenu.add(sessionButton);
            }
            menuBar.add(helpMenu);
        }
        this.setJMenuBar(menuBar);
        scrollPane.setViewportView(ViewerGUI.recorder.getScreenPlayer());
        this.add(scrollPane);

        fullscreenToggle.addActionListener(evt -> ViewerGUI.this
                .changeFullScreenMode());
        fastestButton.addActionListener(evt -> ViewerGUI.recorder
                .getViewerOptions().setRefreshRate(0));
        fastButton.addActionListener(evt -> ViewerGUI.recorder
                .getViewerOptions().setRefreshRate(250));
        normalButton.addActionListener(evt -> ViewerGUI.recorder
                .getViewerOptions().setRefreshRate(500));
        slowButton.addActionListener(evt -> ViewerGUI.recorder
                .getViewerOptions().setRefreshRate(1000));
        controlToggle.addActionListener(e -> ViewerGUI.recorder
                .setViewOnly(!ViewerGUI.recorder.isViewOnly()));
        exitButton.addActionListener(e -> ViewerGUI.this.exit());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(final java.awt.event.WindowEvent evt) {
                if (!ViewerGUI.recorder.isViewOnly()) {
                    ViewerGUI.recorder.getEventsListener().addAdapters();
                }
            }

            @Override
            public void windowClosing(final java.awt.event.WindowEvent evt) {
                if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
                    ViewerGUI.this.exit();
                }
            }

            @Override
            public void windowDeactivated(final WindowEvent evt) {
                ViewerGUI.recorder.getEventsListener().removeAdapters();
            }

            @Override
            public void windowDeiconified(final WindowEvent evt) {
                ViewerGUI.recorder.getEventsListener().addAdapters();
            }

            @Override
            public void windowIconified(final WindowEvent evt) {
                ViewerGUI.recorder.getEventsListener().removeAdapters();
            }
        });
        sessionButton.addActionListener(e -> {
            if (ViewerGUI.this.isFullScreenMode()) {
                ViewerGUI.this.changeFullScreenMode();
            }
            ViewerGUI.recorder.getViewer().displayConnectionProperties();
        });
        highestButton.addActionListener(e -> ViewerGUI.this.setQuality(0));
        highButton.addActionListener(e -> ViewerGUI.this.setQuality(1));
        mediumButton.addActionListener(e -> ViewerGUI.this.setQuality(2));
        lowButton.addActionListener(e -> ViewerGUI.this.setQuality(3));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (!ViewerGUI.this.isFullScreenMode()) {
                    scrollPane.setBounds(
                            0,
                            0,
                            ViewerGUI.this.getWidth()
                                    - (int) scrollPane.getVerticalScrollBar()
                                            .getPreferredSize().getWidth(),
                            ViewerGUI.this.getHeight()
                                    - (int) (scrollPane
                                            .getHorizontalScrollBar()
                                            .getPreferredSize().getWidth() + menuBar
                                            .getPreferredSize().getHeight())
                                    + 10);
                } else {
                    scrollPane.setBounds(0, 0, ViewerGUI.this.getWidth(),
                            ViewerGUI.this.getHeight() - 20);
                }
            }
        });
        this.addWindowStateListener(e -> {
            if (!ViewerGUI.this.isFullScreenMode()) {
                scrollPane.setBounds(
                        0,
                        0,
                        ViewerGUI.this.getWidth()
                                - (int) scrollPane.getVerticalScrollBar()
                                        .getPreferredSize().getWidth(),
                        ViewerGUI.this.getHeight()
                                - (int) (scrollPane.getHorizontalScrollBar()
                                        .getPreferredSize().getWidth() + menuBar
                                        .getPreferredSize().getHeight()) + 10);
            } else {
                scrollPane.setBounds(0, 0, ViewerGUI.this.getWidth(),
                        ViewerGUI.this.getHeight() - 20);
            }
        });
        contentPane.setPreferredSize(new Dimension(925, 680));
        this.setMinimumSize(new Dimension(100, 100));
        this.setSize(925, 680);
        this.setLocationRelativeTo(this.getOwner());
        this.setVisible(true);
        this.setTitle("Remote Connection");
    }

    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    public void setQuality(final int index) {
        switch (index) {
            case 0:
                ViewerGUI.recorder.getViewerOptions().setColorQuality(
                        Configuration.Constants.cqFull);
                ViewerGUI.recorder.getViewerOptions().setImageQuality(
                        100 / 100.0);
                break;
            case 1:
                ViewerGUI.recorder.getViewerOptions().setColorQuality(
                        Configuration.Constants.cq16Bit);
                ViewerGUI.recorder.getViewerOptions().setImageQuality(
                        50 / 100.0);
                break;
            case 2:
                ViewerGUI.recorder.getViewerOptions().setColorQuality(
                        Configuration.Constants.cq256);
                ViewerGUI.recorder.getViewerOptions().setImageQuality(
                        25 / 100.0);
                break;
            case 3:
                ViewerGUI.recorder.getViewerOptions().setImageQuality(0 / 100);
                ViewerGUI.recorder.getViewerOptions().setColorQuality(
                        Configuration.Constants.cqGray);
                break;
        }
    }

}
