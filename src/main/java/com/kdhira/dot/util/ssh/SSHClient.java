package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Interface for SSH client
 * Uses Jcraft's JSch implementation
 * @author Kevin Hira
 */
public interface SSHClient extends AutoCloseable {

    int push(String localPath, String remotePath) throws SSHException, IOException;

    int pull(String remotePath, String localPath) throws SSHException, IOException;

    int execute(String command) throws SSHException, IOException;

    void close() throws SSHException;

}
