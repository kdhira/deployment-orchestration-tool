package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;
import com.kdhira.dot.util.ssh.exec.ExecSSHClient;
import com.kdhira.dot.util.ssh.exec.ExecSSHPasswordAuthentication;
import com.kdhira.dot.util.ssh.exec.ExecSSHPrivateKeyAuthentication;

public class SSHHost extends AbstractHost {

    @Override
    protected SSHClient passwordAuthenticatedClient() throws SSHException, IOException {
        return new ExecSSHClient(getHost(), getPort(), getUser(), new ExecSSHPasswordAuthentication(getPassword()));
    }

    @Override
    protected SSHClient privateKeyAuthenticatedClient() throws SSHException, IOException {
        return new ExecSSHClient(getHost(), getPort(), getUser(), new ExecSSHPrivateKeyAuthentication(getKeyFile()));
    }

}
