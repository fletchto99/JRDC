package me.matt.jrdc.gui;

import me.matt.jrdc.Main;
import me.matt.jrdc.server.Server;
import me.matt.jrdc.utilities.Settings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;

public class GUI extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -5442244796182531051L;
    DefaultListModel<InetAddress> listModel = new DefaultListModel<InetAddress>();
    Hashtable<Integer, Integer> viewerKeys;

    public GUI() {
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        mainPanel = new JPanel();
        startButton = new JButton();
        connectButton = new JButton();
        exitButton = new JButton();
        scrollPane2 = new JScrollPane();
        statusArea = new JTextPane();
        connectPanel = new JPanel();
        kickButton = new JButton();
        informationButton = new JButton();
        refreshButton = new JButton();
        kickAllButton = new JButton();
        scrollPane3 = new JScrollPane();
        connectionsList = new JList<InetAddress>();
        settingsPanel = new JPanel();
        usernameBox = new JTextField();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        passwordBox = new JPasswordField();
        authLabel = new JLabel();
        portLabel = new JLabel();
        portBox = new JTextField();

        setTitle("Home - Java Remote Desktop");
        setResizable(false);
        final Container contentPane = getContentPane();
        contentPane.setLayout(null);
        {
            {
                mainPanel.setLayout(null);

                startButton.setText("Start Server");
                mainPanel.add(startButton);
                startButton.setBounds(210, 10, 115, 40);

                connectButton.setText("Connect");
                mainPanel.add(connectButton);
                connectButton.setBounds(210, 90, 115, 40);

                exitButton.setText("Exit");
                mainPanel.add(exitButton);
                exitButton.setBounds(210, 170, 115, 40);

                {
                    scrollPane2.setViewportView(statusArea);
                    statusArea.setEditable(false);
                }
                mainPanel.add(scrollPane2);
                scrollPane2.setBounds(5, 5, 195, 215);
            }
            tabbedPane.addTab("Home", mainPanel);

            {
                connectPanel.setLayout(null);

                kickButton.setText("Kick");
                connectPanel.add(kickButton);
                kickButton.setBounds(215, 10, 110, 35);

                informationButton.setText("Information");
                connectPanel.add(informationButton);
                informationButton.setBounds(215, 70, 110, 35);

                refreshButton.setText("Refresh");
                connectPanel.add(refreshButton);
                refreshButton.setBounds(215, 125, 110, 35);

                kickAllButton.setText("Kick All");
                connectPanel.add(kickAllButton);
                kickAllButton.setBounds(215, 180, 110, 35);

                {
                    scrollPane3.setViewportView(connectionsList);
                    connectionsList.setModel(listModel);
                }
                connectPanel.add(scrollPane3);
                scrollPane3.setBounds(5, 5, 195, 215);
            }
            tabbedPane.addTab("Connections", connectPanel);

            {
                settingsPanel.setLayout(null);
                settingsPanel.add(usernameBox);
                usernameBox.setBounds(100, 60, 225,
                        usernameBox.getPreferredSize().height);

                usernameLabel.setText("Username:");
                settingsPanel.add(usernameLabel);
                usernameLabel.setBounds(15, 60,
                        usernameLabel.getPreferredSize().width, 20);

                passwordLabel.setText("Password:");
                settingsPanel.add(passwordLabel);
                passwordLabel.setBounds(15, 100,
                        passwordLabel.getPreferredSize().width, 20);
                settingsPanel.add(passwordBox);
                passwordBox.setBounds(100, 100, 225,
                        passwordBox.getPreferredSize().height);

                authLabel.setText("Authentication");
                authLabel.setFont(authLabel.getFont().deriveFont(
                        authLabel.getFont().getStyle() | Font.BOLD,
                        authLabel.getFont().getSize() + 10f));
                settingsPanel.add(authLabel);
                authLabel.setBounds(new Rectangle(new Point(80, 10), authLabel
                        .getPreferredSize()));

                portLabel.setText("Port:");
                settingsPanel.add(portLabel);
                portLabel.setBounds(35, 140,
                        portLabel.getPreferredSize().width, 20);
                settingsPanel.add(portBox);
                portBox.setBounds(100, 140, 225,
                        portBox.getPreferredSize().height);
            }
            tabbedPane.addTab("Settings", settingsPanel);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
                    Main.kill();
                }
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                exitAction(evt);
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                connectAction(evt);
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                serverAction(evt);
            }
        });

        kickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                disconnectViewerAction(evt);
            }
        });

        kickAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                disconnectAllViewersAction(evt);
            }
        });

        informationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                viewerPropertiesAction(evt);
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                refreshAction(evt);
            }
        });

        contentPane.add(tabbedPane);
        tabbedPane.setBounds(0, 0, 350, 260);

        contentPane.setPreferredSize(new Dimension(355, 285));
        setSize(355, 285);
        setLocationRelativeTo(getOwner());
        updateList();
        updateStatus();
        setVisible(true);
    }

    public void setSettings(final Settings s) {
        usernameBox.setText(s.getUsername());
        passwordBox.setText(s.getPassword());
        portBox.setText(String.valueOf(s.getPort()));
    }

    private void jTabbedPaneStateChanged(final ChangeEvent evt) {
        setTitle(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())
                + " - Java Remote Desktop");
    }

    private void serverAction(final ActionEvent evt) {
        if (Server.isRunning()) {
            Server.terminate();
        } else {
            new Server(this);
        }
        updateStatus();
    }

    private void connectAction(final ActionEvent evt) {
        ConnectionFrame.create();
    }

    private void exitAction(final ActionEvent evt) {
        Main.kill();
    }

    public void updateStatus() {
        if (Server.isRunning()) {
            statusArea.setText("Running.");
            startButton.setText("Stop");
        } else {
            statusArea.setText("Server not running.");
            startButton.setText("Start");
        }
    }

    public String getPassword() {
        String password = "";
        for (final char c : passwordBox.getPassword()) {
            password += c;
        }
        return password;
    }

    public String getUsername() {
        return usernameBox.getText();
    }

    public int getPort() {
        return Integer.parseInt(portBox.getText());
    }

    private void disconnectAllViewersAction(final ActionEvent evt) {
        if (viewerKeys.size() == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "Disconnect viewers ? ",
                "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
            return;
        }
        Server.disconnectAllViewers();
        updateList();
    }

    private void refreshAction(final ActionEvent evt) {
        updateList();
    }

    private void disconnectViewerAction(final ActionEvent evt) {
        final int index = connectionsList.getSelectedIndex();
        if (index == -1) {
            return;
        }
        final InetAddress inetAddress = connectionsList.getSelectedValue();

        if (JOptionPane.showConfirmDialog(this,
                "Disconnect " + inetAddress.toString() + " ? ",
                "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
            return;
        }

        Server.removeViewer(viewerKeys.get(index));
        updateList();
    }

    private void viewerPropertiesAction(final ActionEvent evt) {
        final int index = connectionsList.getSelectedIndex();
        if (index == -1) {
            return;
        }
        Server.getViewerConnectionProperties(index).display();
    }

    public void updateList() {
        Hashtable<Integer, InetAddress> hosts;
        hosts = Server.getConnectedHosts();

        final Enumeration<Integer> viewersKeys = hosts.keys();
        viewerKeys = new Hashtable<Integer, Integer>();
        int i = 0;
        listModel.clear();

        while (viewersKeys.hasMoreElements()) {
            final int key = viewersKeys.nextElement();
            viewerKeys.put(i++, key);
            listModel.addElement(hosts.get(key));
        }
    }

    private JTabbedPane tabbedPane;
    private JPanel mainPanel;
    private JButton startButton;
    private JButton connectButton;
    private JButton exitButton;
    private JScrollPane scrollPane2;
    private JTextPane statusArea;
    private JPanel connectPanel;
    private JButton kickButton;
    private JButton informationButton;
    private JButton refreshButton;
    private JButton kickAllButton;
    private JScrollPane scrollPane3;
    private JList<InetAddress> connectionsList;
    private JPanel settingsPanel;
    private JTextField usernameBox;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordBox;
    private JLabel authLabel;
    private JLabel portLabel;
    private JTextField portBox;
}
