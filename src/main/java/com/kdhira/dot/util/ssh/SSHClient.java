package com.kdhira.dot.util.ssh;

import java.io.IOException;

/**
 * Interface for SSH client
 * Uses Jcraft's JSch implementation
 * @author Kevin Hira
 */
public interface SSHClient {

    SSHClient withConfig(String property, String value);

    SSHClient connect() throws SSHException;

    void disconnect() throws SSHException;

    boolean isConnected();

    void push(String localPath, String remotePath) throws SSHException, IOException;

    void pull(String remotePath, String localPath) throws SSHException, IOException;

}
