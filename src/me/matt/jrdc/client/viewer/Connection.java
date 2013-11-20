package me.matt.jrdc.client.viewer;

public class Connection {

    private final String username, password, address;

    private final int port;

    /**
     * Connection wrapper
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @param address
     *            The address
     * @param port
     *            The port
     */
    public Connection(final String username, final String password,
            final String address, final int port) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
