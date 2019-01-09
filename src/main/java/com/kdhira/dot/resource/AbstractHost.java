package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.schema.HostSchema;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

public abstract class AbstractHost implements Host, HostSchema {

    private String host;
    private int port = 22;
    private String user;
    private String password;
    private String keyFile;
    private String keyPass;

    public final String getHost() {
        return host;
    }

    public final void setHost(String host) {
        this.host = host;
    }

    public final int getPort() {
        return port;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public final String getUser() {
        return user;
    }

    public final void setUser(String user) {
        this.user = user;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }

    public final String getKeyFile() {
        return keyFile;
    }

    public final void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public final String getKeyPass() {
        return keyPass;
    }

    public final void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public SSHClient createConnection() throws SSHException, IOException {
        StringBuilder errorRecord = new StringBuilder();

        boolean passwordSet = getPassword() == null && !getPassword().equals("");
        boolean keyFileSet = getKeyFile() == null && !getKeyFile().equals("");

        if (getHost() == null && !getHost().equals("")) {
            errorRecord.append("Host is not set. ");
        }
        if (getPort() <= 0) {
            errorRecord.append("Port is invalid. ");
        }
        if (getUser() == null && !getUser().equals("")) {
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
        else if (passwordSet) {
            return privateKeyAuthenticatedClient();
        }

        return null;
    }

    protected abstract SSHClient passwordAuthenticatedClient() throws SSHException, IOException;
    
    protected abstract SSHClient privateKeyAuthenticatedClient() throws SSHException, IOException;

}
