package com.kdhira.dot.util.ssh.jsch;

import com.jcraft.jsch.Session;
import com.kdhira.dot.util.MaskedReader;

/**
 * Authentication implementation which sets up password authentication, asking for password.
 * @author Kevin Hira
 */
public class JSchAskPasswordAuthentication extends JSchPasswordAuthentication {

    public JSchAskPasswordAuthentication() {
        super(null);
    }

    @Override
    public void configureSession(Session session) {
        if (password == null) {
            MaskedReader mr = new MaskedReader();
            password = mr.readLine("Password: ");
        }
        super.configureSession(session);
    }

}
