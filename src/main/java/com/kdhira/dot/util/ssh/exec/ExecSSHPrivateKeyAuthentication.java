package com.kdhira.dot.util.ssh.exec;

/**
 * Authentication implementation for private key authentication.
 * @author Kevin Hira
 */
public class ExecSSHPrivateKeyAuthentication implements ExecSSHAuthentication {

    private String keyFile;

    public ExecSSHPrivateKeyAuthentication(String keyFile) {
        this.keyFile = keyFile;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getKeyFile() {
        return keyFile;
    }

}
