package com.kdhira.dot.resource;

import java.io.IOException;

import com.jcraft.jsch.JSch;
import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;
import com.kdhira.dot.util.ssh.jsch.JSchClient;
import com.kdhira.dot.util.ssh.jsch.JSchPasswordAuthentication;
import com.kdhira.dot.util.ssh.jsch.JSchPrivateKeyAuthentication;

/**
 * Host implementation utilising {@link JSchClient} and {@link JSch}.
 * @author Kevin Hira
 */
public class JSchHost extends AbstractHost {

    @Override
    protected SSHClient passwordAuthenticatedClient() throws SSHException, IOException {
        return new JSchClient(getHost(), getPort(), getUser(), new JSchPasswordAuthentication(getPassword()));
    }

    @Override
    protected SSHClient privateKeyAuthenticatedClient() throws SSHException, IOException {
        return new JSchClient(getHost(), getPort(), getUser(), new JSchPrivateKeyAuthentication(getKeyFile(), getKeyPass()));
    }

}
