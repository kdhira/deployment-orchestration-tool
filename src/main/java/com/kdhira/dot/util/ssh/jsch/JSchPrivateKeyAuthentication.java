package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Authentication implementation which sets up private key authentication.
 * @author Kevin Hira
 */
public class JSchPrivateKeyAuthentication implements JSchAuthentication {

    private String keyFile;
    protected String keyPass;

    public JSchPrivateKeyAuthentication(String keyFile) {
        this(keyFile, null);
    }

    public JSchPrivateKeyAuthentication(String keyFile, String keyPass) {
        this.keyFile = keyFile;
        this.keyPass = keyPass;
    }

    @Override
    public void configureClient(JSch jsch) throws SSHException {
        if (keyFile != null) {
            if (keyPass != null) {
                try {
                    jsch.addIdentity(keyFile, keyPass);
                } catch (JSchException e) {
                    throw new SSHException("Private key is invalid or passphrase incorrect.", e);
                }
            } else {
                try {
                    jsch.addIdentity(keyFile);
                } catch (JSchException e) {
                    throw new SSHException("Private key is invalid.", e);
                }
            }
        }
    }

    @Override
    public void configureSession(Session session) {

    }

}
