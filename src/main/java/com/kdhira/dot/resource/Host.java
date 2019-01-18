package com.kdhira.dot.resource;

import java.io.IOException;

import com.kdhira.dot.util.ssh.SSHClient;
import com.kdhira.dot.util.ssh.SSHException;

/**
 * Functional interface for hosts.
 * @author Kevin Hira
 */
public interface Host extends Resource {

    /**
     * Create a SSHClient.
     * @return a {@link SSHClient} instance
     * @throws SSHException
     * @throws IOException
     */
    SSHClient createConnection() throws SSHException, IOException;

}
