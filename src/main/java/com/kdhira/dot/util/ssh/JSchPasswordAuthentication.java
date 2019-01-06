package com.kdhira.dot.util.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSchPasswordAuthentication implements JSchAuthentication {
    private String password;

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
