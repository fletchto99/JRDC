package me.matt.jrdc.server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import me.matt.jrdc.Configuration;
import me.matt.jrdc.gui.GUI;
import me.matt.jrdc.utilities.InetAddrUtility;
import me.matt.jrdc.utilities.security.SecurityUtility;

public class RMIServer {

    /**
     * Start the rmi server
     *
     * @return True if successfully started; otherwise false
     */
    private static boolean Start() {
        InetAddrUtility.setDefaultAdr(Configuration.server_address);
        try {
            RMIServer.registry = LocateRegistry.createRegistry(RMIServer.gui
                    .getPort());
            RMIServer.serverImpl = new ServerImpl(
                    new MultihomeRMIClientSocketFactory(
                            InetAddrUtility.getLocalIPAdresses()));
            RMIServer.registry.rebind("jrdc", RMIServer.serverImpl);
        } catch (final RemoteException e) {
            RMIServer.Stop();
            if (e instanceof ExportException) {
                final ExportException ex = (ExportException) e;
                if (ex.getMessage().contains("Port already in use:")) {
                    Configuration
                            .displayError(
                                    "A RMI server is alread running on the select port. The server could not start please either change the port or stop the other RMI server.",
                                    "Error");
                    return false;
                }
            }
            Configuration.displayError(e, "Error");
            return false;
        }

        return true;
    }

    /**
     * Start the RMI server
     *
     * @param config
     *            The config options
     * @return True if started successfully
     */
    public static boolean Start(final GUI config) {
        RMIServer.gui = config;
        return RMIServer.Start();
    }

    /**
     * Stop the server
     */
    public static void Stop() {
        try {
            if (RMIServer.registry != null) {
                UnicastRemoteObject.unexportObject(RMIServer.registry, true);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        RMIServer.registry = null;
        RMIServer.serverImpl = null;
    }

    /**
     * Verify the username/password the client sends
     *
     * @param username
     *            The username to check
     * @param password
     *            The password to check
     * @return True if they match; otherwise false
     */
    public static boolean verify(final String username, final String password) {
        try {
            return RMIServer.gui.getPassword()
                    .equals(SecurityUtility.decrypt(password,
                            "JRDCSECURE123456789"))
                    && RMIServer.gui.getUsername().equals(username);
        } catch (final Exception e) {
            System.out.println("Exception verifying.");
            e.printStackTrace();
            return false;
        }
    }

    private static Registry registry;

    private static ServerImpl serverImpl;

    private static GUI gui;

}
