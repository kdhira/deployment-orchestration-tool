package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.schema.HostSchema;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Partial definition of Host.
 * Construction of the SSHClient implementations left to subclasses
 * @author Kevin Hira
 */
public abstract class AbstractHost implements Host, HostSchema {

    private String id;
    private String host;
    private int port = 22;
    private String user;
    private String password;
    private String keyFile;
    private String keyPass;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public final String getHost() {
        return host;
    }

    @Override
    public final void setHost(String host) {
        this.host = host;
    }

    @Override
    public final int getPort() {
        return port;
    }

    @Override
    public final void setPort(int port) {
        this.port = port;
    }

    @Override
    public final String getUser() {
        return user;
    }

    @Override
    public final void setUser(String user) {
        this.user = user;
    }

    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    public final void setPassword(String password) {
        this.password = password;
    }

    @Override
    public final String getKeyFile() {
        return keyFile;
    }

    @Override
    public final void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    @Override
    public final String getKeyPass() {
        return keyPass;
    }

    @Override
    public final void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    @Override
    public SSHClient createConnection() throws SSHException, IOException {
        StringBuilder errorRecord = new StringBuilder();

        boolean passwordSet = getPassword() != null && !getPassword().equals("");
        boolean keyFileSet = getKeyFile() != null && !getKeyFile().equals("");

        if (getHost() == null || getHost().equals("")) {
            errorRecord.append("Host is not set. ");
        }
        if (getPort() <= 0) {
            errorRecord.append("Port is invalid. ");
        }
        if (getUser() == null || getUser().equals("")) {
            errorRecord.append("User is not set. ");
        }
        if (passwordSet == keyFileSet) {
            errorRecord.append("Both or neither of password (password authentication) and keyFile set. Can not tell which authentication method to use. ");
        }
        if (errorRecord.toString().length() > 0) {
            throw new IllegalStateException(errorRecord.toString());
        }

        if (passwordSet) {
            return passwordAuthenticatedClient();
        }
        else if (keyFileSet) {
            return privateKeyAuthenticatedClient();
        }

        return null;
    }

    /**
     * Create a password authenticated client.
     * @return a client with password authentication
     * @throws SSHException
     * @throws IOException
     */
    protected abstract SSHClient passwordAuthenticatedClient() throws SSHException, IOException;

    /**
     * Create a private key authenticated client.
     * @return a client with private key authentication
     * @throws SSHException
     * @throws IOException
     */
    protected abstract SSHClient privateKeyAuthenticatedClient() throws SSHException, IOException;

}
