package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Authentication implementation which sets up password authentication.
 * @author Kevin Hira
 */
public class JSchPasswordAuthentication implements JSchAuthentication {

    protected String password;

    public JSchPasswordAuthentication(String password) {
        this.password = password;
    }

    @Override
    public void configureClient(JSch jsch) throws SSHException {

    }

    @Override
    public void configureSession(Session session) {
        session.setPassword(password);
    }

}
