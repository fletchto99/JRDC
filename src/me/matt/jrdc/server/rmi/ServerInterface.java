package me.matt.jrdc.server.rmi;

import me.matt.jrdc.utilities.ConnectionProperties;
import me.matt.jrdc.utilities.Options;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract interface ServerInterface extends Remote {

    public abstract ConnectionProperties getConnectionProperties(int index)
            throws RemoteException;

    public abstract int startViewer(InetAddress inetAddress, String username,
            String password, Hashtable<String, Object> properties)
            throws RemoteException;

    public abstract void stopViewer(int index) throws RemoteException;

    public abstract byte[] getScreenCapture(int index) throws RemoteException;

    public abstract void setScreenCapture(byte[] data, int index)
            throws RemoteException;

    public abstract void updateOptions(Options options, int index)
            throws RemoteException;

    public abstract ArrayList<MouseEvent> getMouseEvents(int index)
            throws RemoteException;

    public abstract void setMouseEvents(ArrayList<MouseEvent> events)
            throws RemoteException;

    public abstract ArrayList<KeyEvent> getKeyEvents(int index)
            throws RemoteException;

    public abstract void setKeyEvents(ArrayList<KeyEvent> events)
            throws RemoteException;

    public abstract ConnectionProperties getConnectionProperties()
            throws RemoteException;
}
