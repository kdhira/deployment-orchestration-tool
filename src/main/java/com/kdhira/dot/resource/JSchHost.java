package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;
import com.kdhira.dot.util.ssh.jsch.JSchClient;
import com.kdhira.dot.util.ssh.jsch.JSchPasswordAuthentication;
import com.kdhira.dot.util.ssh.jsch.JSchPrivateKeyAuthentication;

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
