package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Functional definition for SSH operations.
 * @author Kevin Hira
 */
public interface SSHRunnable {

    int run(SSHClient client) throws SSHException, IOException;

    String commandString();

}
