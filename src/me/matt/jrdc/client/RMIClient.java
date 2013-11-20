package me.matt.jrdc.client;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.client.viewer.Connection;
import me.matt.jrdc.server.rmi.ServerInterface;
import me.matt.jrdc.utilities.InetAddrUtility;
import me.matt.jrdc.utilities.security.SecurityUtility;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;

public class RMIClient {

    private final Connection con;
    private Registry registry;
    private ServerInterface rmiServer;

    private int index = -1;
    private boolean connected = false;
    private final Hashtable<String, Object> properties;

    /**
     * Create an instance of the RMI client
     * 
     * @param con
     *            The connection information
     * @param properties
     *            The clients connection properties
     */
    public RMIClient(final Connection con,
            final Hashtable<String, Object> properties) {
        this.properties = properties;
        this.con = con;
    }

    /**
     * Check if connected to the server
     * 
     * @return True if connected; otherwise false
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Start the RMIClient
     */
    public void Start() {
        connect();
        if (!connected) {
            disconnect();
        }
    }

    /**
     * Connect to the rmi server
     * 
     * @return The index of the viewer, if connected
     */
    public int connect() {
        connected = false;

        try {
            registry = LocateRegistry.getRegistry(con.getAddress(),
                    con.getPort());
            rmiServer = (ServerInterface) registry.lookup("jrdc");
            index = rmiServer.startViewer(InetAddrUtility.getLocalHost(), con
                    .getUsername(), SecurityUtility.encrypt(con.getPassword(),
                    "JRDCSECURE123456789", 2), properties);

            switch (index) {
                case -1:
                    Configuration.displayError(
                            "Error - Invalid username/password entered!",
                            "Authentication Failed");
                    return -1;
            }
            connected = true;
            return index;
        } catch (final Exception e) {
            Configuration.displayError(e, "Fatal Error");
            return -1;
        }
    }

    /**
     * Fetch the instance of the ServerInterface
     * 
     * @return The server interface
     */
    public ServerInterface getServer() {
        return rmiServer;
    }

    /**
     * Stop the client and disconnect from the server
     */
    public void disconnect() {
        connected = false;
        try {
            if (rmiServer != null && index > -1) {
                rmiServer.stopViewer(index);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        rmiServer = null;
        registry = null;
    }
}