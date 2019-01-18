package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Functional definition for SSH operations.
 * @author Kevin Hira
 */
@FunctionalInterface
public interface SSHRunnable {

    public int run(SSHClient client) throws SSHException, IOException;

}
