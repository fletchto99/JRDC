package me.matt.jrdc.server.rmi;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;

import me.matt.jrdc.client.viewer.Viewer;
import me.matt.jrdc.server.Server;
import me.matt.jrdc.utilities.ConnectionProperties;
import me.matt.jrdc.utilities.Options;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    /**
     *
     */
    private static final long serialVersionUID = -3678843007640565983L;

    public ServerImpl(final RMIClientSocketFactory csf) throws RemoteException {
        super(0, csf, null);
    }

    @Override
    public ConnectionProperties getConnectionProperties()
            throws RemoteException {
        return Server.getServerConnectionProperties();
    }

    @Override
    public ConnectionProperties getConnectionProperties(final int index)
            throws RemoteException {
        return Server.getViewerConnectionProperties(index);
    }

    @Override
    public ArrayList<KeyEvent> getKeyEvents(final int index)
            throws RemoteException {
        return Viewer.getKeyEvents(index);
    }

    @Override
    public ArrayList<MouseEvent> getMouseEvents(final int index)
            throws RemoteException {
        return Viewer.getMouseEvents(index);
    }

    @Override
    public byte[] getScreenCapture(final int index) throws RemoteException {
        return Server.getScreenCapture(index);
    }

    @Override
    public void setKeyEvents(final ArrayList<KeyEvent> events)
            throws RemoteException {
        Server.setKeyEvents(events);
    }

    @Override
    public void setMouseEvents(final ArrayList<MouseEvent> events)
            throws RemoteException {
        Server.setMouseEvents(events);
    }

    @Override
    public void setScreenCapture(final byte[] data, final int index)
            throws RemoteException {
        Viewer.setScreenCapture(data, index);
    }

    @Override
    public int startViewer(final InetAddress inetAddress,
            final String username, final String password,
            final Hashtable<String, Object> properties) throws RemoteException {
        if (!RMIServer.verify(username, password)) {
            return -1;
        }
        return Server.addViewer(inetAddress, properties);
    }

    @Override
    public void stopViewer(final int index) throws RemoteException {
        Server.removeViewer(index);
    }

    @Override
    public void updateOptions(final Options options, final int index)
            throws RemoteException {
        Server.updateOptions(index, options);
    }
}
