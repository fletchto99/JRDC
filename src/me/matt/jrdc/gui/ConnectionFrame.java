package me.matt.jrdc.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import me.matt.jrdc.client.viewer.Connection;
import me.matt.jrdc.client.viewer.Viewer;

public class ConnectionFrame extends JFrame {

    public static void close() {
        if (ConnectionFrame.connection != null) {
            ConnectionFrame.connection.dispose();
        }
        ConnectionFrame.connection = null;
    }

    public static void create() {
        if (ConnectionFrame.connection != null) {
            return;
        }
        SwingUtilities
                .invokeLater(() -> ConnectionFrame.connection = new ConnectionFrame());
    }

    private static final long serialVersionUID = -5369689276669616289L;

    private static ConnectionFrame connection;

    private JLabel addressLabel;

    private JLabel portLabel;

    private JLabel usernameLabel;

    private JLabel passwordLabel;

    private JTextField addressBox;
    private JTextField portBox;
    private JTextField usernameBox;
    private JPasswordField passwordBox;
    private JButton cancelButton;
    private JButton connectButon;

    private ConnectionFrame() {
        this.initComponents();
    }

    private void connectAction(final ActionEvent evt) {
        try {
            Integer.parseInt(portBox.getText());
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Port!");
            return;
        }
        ConnectionFrame.close();
        new Viewer(new Connection(usernameBox.getText(), this.getPassword(),
                addressBox.getText(), Integer.parseInt(portBox.getText())));
    }

    private String getPassword() {
        String password = "";
        for (final char c : passwordBox.getPassword()) {
            password += c;
        }
        return password;
    }

    private void initComponents() {
        addressLabel = new JLabel();
        portLabel = new JLabel();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        addressBox = new JTextField();
        portBox = new JTextField();
        usernameBox = new JTextField();
        passwordBox = new JPasswordField();
        cancelButton = new JButton();
        connectButon = new JButton();

        this.setResizable(false);
        this.setTitle("Connection Settings");
        final Container contentPane = this.getContentPane();
        contentPane.setLayout(null);

        addressLabel.setText("Address:");
        contentPane.add(addressLabel);
        addressLabel
                .setBounds(20, 5, addressLabel.getPreferredSize().width, 20);

        portLabel.setText("Port:");
        contentPane.add(portLabel);
        portLabel.setBounds(40, 30, portLabel.getPreferredSize().width, 20);

        usernameLabel.setText("Username:");
        contentPane.add(usernameLabel);
        usernameLabel.setBounds(15, 55, usernameLabel.getPreferredSize().width,
                20);

        passwordLabel.setText("Password:");
        contentPane.add(passwordLabel);
        passwordLabel.setBounds(15, 80, passwordLabel.getPreferredSize().width,
                20);
        contentPane.add(addressBox);
        addressBox.setBounds(75, 5, 150, addressBox.getPreferredSize().height);
        contentPane.add(portBox);
        portBox.setBounds(75, 30, 150, portBox.getPreferredSize().height);
        contentPane.add(usernameBox);
        usernameBox.setBounds(75, 55, 150,
                usernameBox.getPreferredSize().height);
        contentPane.add(passwordBox);
        passwordBox.setBounds(75, 80, 150,
                passwordBox.getPreferredSize().height);

        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(15, 110, 100, 30);

        connectButon.setText("Connect");
        contentPane.add(connectButon);
        connectButon.setBounds(120, 110, 100, 30);

        contentPane.setPreferredSize(new Dimension(250, 185));
        this.setSize(250, 185);
        this.setLocationRelativeTo(this.getOwner());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
                    ConnectionFrame.close();
                }
            }
        });
        cancelButton.addActionListener(evt -> ConnectionFrame.close());
        connectButon.addActionListener(evt -> ConnectionFrame.this
                .connectAction(evt));
        this.setVisible(true);
        portBox.setText("1099");
        addressBox.setText("localhost");
        usernameBox.setText("admin");
        passwordBox.setText("admin");
    }
}
